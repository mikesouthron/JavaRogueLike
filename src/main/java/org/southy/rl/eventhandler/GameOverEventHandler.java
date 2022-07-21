package org.southy.rl.eventhandler;

import org.southy.rl.Engine;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameOverEventHandler implements EventHandler {

    Engine engine;

    public GameOverEventHandler(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void handleEvents(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            try {
                Files.deleteIfExists(Paths.get("game.save"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
