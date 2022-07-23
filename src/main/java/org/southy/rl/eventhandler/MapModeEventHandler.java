package org.southy.rl.eventhandler;

import org.southy.rl.Application;
import org.southy.rl.Engine;
import org.southy.rl.asciipanel.AsciiPanel;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class MapModeEventHandler implements EventHandler {

    Engine engine;

    public MapModeEventHandler(Engine engine) {
        this.engine = engine;
    }

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

    @Override
    public void handleEvents(KeyEvent event) {
        if (MOVE_KEYS.containsKey(event.getKeyCode())) {
            var dir = MOVE_KEYS.get(event.getKeyCode());
            Application.camera.x += event.isShiftDown() ? dir.x * 10 : dir.x * 5;
            Application.camera.y += event.isShiftDown() ? dir.y * 10 : dir.y * 5;
            return;
        }
        Application.camera = Application.normalCamera;
        engine.gameMap.fullMap = false;
        Application.camera.x = engine.player.x;
        Application.camera.y = engine.player.y;
        Application.swapPanel(false);
        engine.eventHandler = new MainGameEventHandler(engine);
    }

    @Override
    public void onRender(AsciiPanel panel) {
        EventHandler.super.onRender(panel);
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
