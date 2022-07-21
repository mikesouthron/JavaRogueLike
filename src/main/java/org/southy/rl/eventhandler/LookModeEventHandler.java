package org.southy.rl.eventhandler;

import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;
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
        MOVE_KEYS.put(KeyEvent.VK_PAGE_DOWN, new MainGameEventHandler.Direction(1, 1));
        MOVE_KEYS.put(KeyEvent.VK_END, new MainGameEventHandler.Direction(-1, 1));
        MOVE_KEYS.put(KeyEvent.VK_HOME, new MainGameEventHandler.Direction(-1, -1));
        MOVE_KEYS.put(KeyEvent.VK_PAGE_UP, new MainGameEventHandler.Direction(1, -1));
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
            cursorX = Math.max(0, Math.min(cursorX + (event.isShiftDown() ? dir.x * 5 : dir.x), engine.gameMap.width - 1));
            cursorY = Math.max(0, Math.min(cursorY + (event.isShiftDown() ? dir.y * 5 : dir.y), engine.gameMap.height - 1));
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
                        panel.write(lines.get(j), 81, yOffset + 1, Color.WHITE);
                        yOffset--;
                    }
                }

                var entity = entities
                        .stream()
                        .sorted(Comparator.comparing(a -> a.renderOrder.ordinal()))
                        .collect(Collectors.toList()).get(entities.size() - 1);

                panel.write(entity.str, entity.x + GameMap.MAP_OFFSET_X, entity.y + GameMap.MAP_OFFSET_Y, entity.fg, Color.CYAN);
            } else {
                panel.write(tile.name, 81, 1, Color.WHITE);
                panel.write(tile.light.ch, cursorX + GameMap.MAP_OFFSET_X, cursorY + GameMap.MAP_OFFSET_Y, tile.light.fg, Color.CYAN);
            }
        } else {
            panel.write(tile.dark.ch, cursorX + GameMap.MAP_OFFSET_X, cursorY + GameMap.MAP_OFFSET_Y, tile.dark.fg, Color.CYAN);
            panel.write("Not Visible", 81, 1, Color.WHITE);
        }
    }
}
