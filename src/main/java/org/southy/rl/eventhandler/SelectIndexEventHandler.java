package org.southy.rl.eventhandler;

import org.southy.rl.Action;
import org.southy.rl.Engine;
import org.southy.rl.exceptions.Impossible;
import org.southy.sdl.SDL;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SelectIndexEventHandler implements EventHandler {

    static final Map<Integer, MainGameEventHandler.Direction> MOVE_KEYS = new HashMap<>();
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

    static final Set<Integer> CONFIRM_KEYS = Set.of(KeyEvent.VK_ENTER, KeyEvent.VK_KP_ENTER);

    Engine engine;

    int cursorX;
    int cursorY;

    public SelectIndexEventHandler(Engine engine) {
        this.engine = engine;
        cursorX = engine.player.x;
        cursorY = engine.player.y;
    }

    @Override
    public void handleEvents(SDL sdl) throws Impossible {
        var keyEvent = sdl.SDLGetEvent();
        if (keyEvent == null) {
            return;
        }
        if (MOVE_KEYS.containsKey(keyEvent.getKeyCode())) {
            var dir = MOVE_KEYS.get(keyEvent.getKeyCode());
            cursorX = Math.max(0, Math.min(cursorX + (keyEvent.isShiftDown() ? dir.x * 5 : dir.x), engine.gameMap.width - 1));
            cursorY = Math.max(0, Math.min(cursorY + (keyEvent.isShiftDown() ? dir.y * 5 : dir.y), engine.gameMap.height - 1));
            return;
        }
        if (CONFIRM_KEYS.contains(keyEvent.getKeyCode())) {
            var action = onIndexSelected();
            if (action != null) {
                if (action.perform()) {
                    engine.handleEnemyTurns();
                    engine.updateFov();
                    engine.endOfTurn();
                }
            }
        }
        engine.eventHandler = new MainGameEventHandler(engine);
    }

    @Override
    public void onRender(SDL sdl) {
        EventHandler.super.onRender(sdl);
    }

    @Override
    public Engine engine() {
        return engine;
    }

    public Action onIndexSelected() throws Impossible {
        throw new Impossible("Not implemented");
    }
}
