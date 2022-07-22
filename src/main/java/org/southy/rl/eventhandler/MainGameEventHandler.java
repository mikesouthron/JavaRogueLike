package org.southy.rl.eventhandler;

import org.southy.rl.Application;
import org.southy.rl.BaseAction;
import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Entity;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.gen.Procgen;
import org.southy.rl.map.GameMap;
import org.southy.rl.ui.Render;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MainGameEventHandler implements EventHandler {

    public static class Direction {
        public int x;
        public int y;

        public Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final Map<Integer, Direction> MOVE_KEYS = new HashMap<>();
    static {
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD4, new Direction(-1, 0));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD6, new Direction(1, 0));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD8, new Direction(0, -1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD2, new Direction(0, 1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD3, new Direction(1, 1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD1, new Direction(-1, 1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD7, new Direction(-1, -1));
        MOVE_KEYS.put(KeyEvent.VK_NUMPAD9, new Direction(1, -1));
        MOVE_KEYS.put(KeyEvent.VK_LEFT, new Direction(-1, 0));
        MOVE_KEYS.put(KeyEvent.VK_RIGHT, new Direction(1, 0));
        MOVE_KEYS.put(KeyEvent.VK_UP, new Direction(0, -1));
        MOVE_KEYS.put(KeyEvent.VK_DOWN, new Direction(0, 1));
        MOVE_KEYS.put(KeyEvent.VK_PAGE_DOWN, new Direction(1, 1));
        MOVE_KEYS.put(KeyEvent.VK_END, new Direction(-1, 1));
        MOVE_KEYS.put(KeyEvent.VK_HOME, new Direction(-1, -1));
        MOVE_KEYS.put(KeyEvent.VK_PAGE_UP, new Direction(1, -1));
    }
    private static final Set<Integer> WAIT_KEYS = Set.of(KeyEvent.VK_NUMPAD5, KeyEvent.VK_PERIOD, KeyEvent.VK_CLEAR);

    Engine engine;

    public MainGameEventHandler(Engine engine) {
        this.engine = engine;
    }

    public void handleEvents(KeyEvent event) throws Impossible {
        var option = keyDown(event);
        if (option.isPresent()) {
            if (option.get().perform()) {
                engine.handleEnemyTurns();
                engine.updateFov();
            }
        }
    }

    @Override
    public Engine engine() {
        return engine;
    }

    public Optional<BaseAction> keyDown(KeyEvent event) {
        if (event == null) {
            return Optional.empty();
        }

        var player = engine.player;

        if (MOVE_KEYS.containsKey(event.getKeyCode())) {
            var dir = MOVE_KEYS.get(event.getKeyCode());
            return Optional.of(new BaseAction.BumpAction(player, dir.x, dir.y, event.isShiftDown()));
        }

        if (event.getKeyCode() == KeyEvent.VK_COMMA) {
            return Optional.of(new BaseAction.PickupAction(player));
        }

        if (WAIT_KEYS.contains(event.getKeyCode())) {
            return Optional.of(new BaseAction.WaitAction(player));
        }

        if (event.isControlDown() && event.getKeyCode() ==KeyEvent.VK_R) {
            try {
                engine.gameMap = Procgen.generateDungeon(engine, Application.maxRooms, Application.roomMinSize, Application.roomMaxSize,
                        Application.mapWidth, Application.mapHeight, Application.maxMonstersPerRoom, Application.maxItemsPerRoom);
            } catch (Impossible e) {
                engine.logger.addMessage("Unable to generate new map", ColorUtils.IMPOSSIBLE);
            }
            return Optional.of(new BaseAction.WaitAction(player));
        }

        if (event.getKeyCode() == KeyEvent.VK_V) {
            engine.eventHandler = new HistoryViewerEventHandler(engine);
        }

        if (event.getKeyCode() == KeyEvent.VK_L) {
            engine.eventHandler = new LookModeEventHandler(engine);
        }

        if (event.getKeyCode() == KeyEvent.VK_I) {
            engine.eventHandler = new InventoryEventHandler(engine);
        }

        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            return Optional.of(new BaseAction.EscapeAction(player));
        }

        return Optional.empty();
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void onRender(AsciiPanel panel) {
        EventHandler.super.onRender(panel);

        panel.write("Explore", 87, 1, Color.WHITE);
        List<Entity> entities = Render.getNamesAtLocation(engine.player.x, engine.player.y, engine.gameMap);

        if (entities.size() > 0) {
            var lines = new ArrayList<String>();
            for (Entity entity : entities) {
                if (entity != engine.player) {
                    var text = Logger.wrap(entity.name, 16);
                    lines.addAll(text);
                }
            }
            var yOffset = lines.size() - 1;
            for (int i = lines.size() - 1; i >= 0; i--) {
                panel.write(lines.get(i), 82, yOffset + 2, Color.WHITE);
                yOffset--;
            }
        }
    }
}
