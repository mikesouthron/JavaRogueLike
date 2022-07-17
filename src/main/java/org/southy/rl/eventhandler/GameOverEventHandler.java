package org.southy.rl.eventhandler;

import java.awt.event.KeyEvent;

public class GameOverEventHandler implements EventHandler {
    @Override
    public void handleEvents(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
}
