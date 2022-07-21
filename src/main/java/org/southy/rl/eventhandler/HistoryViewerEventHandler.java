package org.southy.rl.eventhandler;

import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.map.GameMap;

import java.awt.event.KeyEvent;
import java.util.HashMap;
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
    public void handleEvents(KeyEvent event) {
        if (Y_KEYS.containsKey(event.getKeyCode())) {
            var adjust = Y_KEYS.get(event.getKeyCode());
            cursor = Math.max(0, Math.min(cursor + adjust, engine.logger.log.size() - 1));
        } else {
            engine.eventHandler = new MainGameEventHandler(engine);
        }
    }

    @Override
    public void onRender(AsciiPanel panel) {
        EventHandler.super.onRender(panel);
        var messages = engine.logger.log;
        Logger.renderMessages(panel, GameMap.MAP_OFFSET_X, GameMap.MAP_OFFSET_Y, engine.gameMap.width, engine.gameMap.height / 2, messages.subList(0, cursor + 1));
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
