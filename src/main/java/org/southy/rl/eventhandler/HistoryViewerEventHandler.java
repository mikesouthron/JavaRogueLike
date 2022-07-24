package org.southy.rl.eventhandler;

import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.map.GameMap;
import org.southy.sdl.SDL;

import java.util.Map;

public class HistoryViewerEventHandler implements EventHandler {

    private static final Map<Integer, Integer> Y_KEYS = Map.of(
            KeyEvent.VK_UP, -1,
            KeyEvent.VK_DOWN, 1,
            KeyEvent.VK_PAGE_UP, -10,
            KeyEvent.VK_PAGE_DOWN, 10);

    Engine engine;

    int cursor;

    public HistoryViewerEventHandler(Engine engine) {
        this.engine = engine;
        cursor = engine.logger.log.size() - 1;
    }

    @Override
    public void handleEvents(SDL sdl) {
        var keyEvent = sdl.SDLGetEvent();
        if (keyEvent == null) {
            return;
        }
        if (Y_KEYS.containsKey(keyEvent.getKeyCode())) {
            var adjust = Y_KEYS.get(keyEvent.getKeyCode());
            cursor = Math.max(0, Math.min(cursor + adjust, engine.logger.log.size() - 1));
        } else {
            engine.eventHandler = new MainGameEventHandler(engine);
        }
    }

    @Override
    public void onRender(SDL sdl) {
        EventHandler.super.onRender(sdl);
        var messages = engine.logger.log;
        Logger.renderMessages(sdl, GameMap.MAP_OFFSET_X, GameMap.MAP_OFFSET_Y, engine.gameMap.width, engine.gameMap.height / 2, messages.subList(0, cursor + 1));
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
