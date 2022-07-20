package org.southy.rl.eventhandler;

import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Entity;
import org.southy.rl.gen.Procgen;
import org.southy.rl.ui.Render;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LookModeEventHandler implements EventHandler {

    private static final Map<Integer, MainGameEventHandler.Direction> MOVE_KEYS = new HashMap<>();
    static {
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD4, new MainGameEventHandler.Direction(-1, 0));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD6, new MainGameEventHandler.Direction(1, 0));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD8, new MainGameEventHandler.Direction(0, -1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD2, new MainGameEventHandler.Direction(0, 1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD3, new MainGameEventHandler.Direction(1, 1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD1, new MainGameEventHandler.Direction(-1, 1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD7, new MainGameEventHandler.Direction(-1, -1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD9, new MainGameEventHandler.Direction(1, -1));
        MOVE_KEYS.put(KeyEvent.VK_LEFT, new MainGameEventHandler.Direction(-1, 0));
        MOVE_KEYS.put(KeyEvent.VK_RIGHT, new MainGameEventHandler.Direction(1, 0));
        MOVE_KEYS.put(KeyEvent.VK_UP, new MainGameEventHandler.Direction(0, -1));
        MOVE_KEYS.put(KeyEvent.VK_DOWN, new MainGameEventHandler.Direction(0, 1));
    }

    Engine engine;

    int cursorX;
    int cursorY;

    public LookModeEventHandler(Engine engine) {
        this.engine = engine;
        cursorX = engine.player.x;
        cursorY = engine.player.y;
    }

    @Override
    public void handleEvents(KeyEvent event) {
        if (MOVE_KEYS.containsKey(event.getKeyCode())) {
            var dir = MOVE_KEYS.get(event.getKeyCode());
            cursorX = Math.max(0, Math.min(cursorX + dir.x, engine.gameMap.width));
            cursorY = Math.max(0, Math.min(cursorY + dir.y, engine.gameMap.height));
            return;
        }
        engine.eventHandler = new MainGameEventHandler(engine);

    }

    @Override
    public Engine engine() {
        return engine;
    }

    @Override
    public void onRender(AsciiPanel panel) {
        EventHandler.super.onRender(panel);
        var tile = engine.gameMap.tiles[cursorX + cursorY * engine.gameMap.width];
        if (tile.fov) {
            List<Entity> entities = Render.getNamesAtLocation(cursorX, cursorY, engine.gameMap);

            if (entities.size() > 0) {
                var yOffset = entities.size() - 1;
                for (int i = entities.size() - 1; i >= 0; i--) {
                    var entity = entities.get(i);
                    var lines = Logger.wrap(entity.name, 40);
                    for (int j = lines.size() - 1; j >= 0; j--) {
                        panel.write(lines.get(j), 0, yOffset, Color.WHITE);
                        yOffset--;
                    }
                }

                var entity = entities
                        .stream()
                        .sorted(Comparator.comparing(a -> a.renderOrder.ordinal()))
                        .collect(Collectors.toList()).get(entities.size() - 1);

                panel.write(entity.str, entity.x, entity.y, entity.fg, Color.CYAN);
            } else {
                panel.write(tile.name, 0, 0, Color.WHITE);
                panel.write(tile.light.ch, cursorX, cursorY, tile.light.fg, Color.CYAN);
            }
        } else {
            panel.write(tile.dark.ch, cursorX, cursorY, tile.dark.fg, Color.CYAN);
            panel.write("Not Visible", 0, 0, Color.WHITE);
        }
    }
}
