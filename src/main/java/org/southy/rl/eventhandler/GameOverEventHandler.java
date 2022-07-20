package org.southy.rl.eventhandler;

import org.southy.rl.Engine;
import org.southy.rl.asciipanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class GameOverEventHandler implements EventHandler {

    Engine engine;

    public GameOverEventHandler(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void handleEvents(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
