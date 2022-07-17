package org.southy.rl.eventhandler;

import org.southy.rl.*;
import org.southy.rl.gen.Procgen;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    }
    private static final Set<Integer> WAIT_KEYS = Set.of(KeyEvent.VK_NUMPAD5, KeyEvent.VK_PERIOD, KeyEvent.VK_CLEAR);


    Engine engine;
    Logger logger;

    public MainGameEventHandler(Engine engine, Logger logger) {
        this.engine = engine;
        this.logger = logger;
    }

    public void handleEvents(KeyEvent event) {
        var option = keyDown(event);
        if (option.isPresent()) {
            if (option.get().perform()) {
                engine.handleEnemyTurns();
                engine.updateFov();
            }
        }
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

        if (WAIT_KEYS.contains(event.getKeyCode())) {
            return Optional.of(new BaseAction.WaitAction(player));
        }

        if (event.isControlDown() && event.getKeyCode() ==KeyEvent.VK_R) {
            player.gameMap = engine.gameMap = Procgen.generateDungeon(engine, Application.maxRooms, Application.roomMinSize, Application.roomMaxSize, Application.mapWidth, Application.mapHeight, Application.maxMonstersPerRoom);
            return Optional.of(new BaseAction.WaitAction(player));
        }

        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            return Optional.of(new BaseAction.EscapeAction(player));
        }

        return Optional.empty();
    }

}
