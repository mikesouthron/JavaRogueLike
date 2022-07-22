package org.southy.rl.eventhandler;

import org.southy.rl.Engine;
import org.southy.rl.asciipanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameOverEventHandler implements EventHandler {

    Engine engine;

    public GameOverEventHandler(Engine engine) {
        this.engine = engine;
        try {
            Files.deleteIfExists(Paths.get("game.save"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvents(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            engine.eventHandler = new MainMenuHandler(engine);
        }
    }

    @Override
    public void onRender(AsciiPanel panel) {
        panel.clear();
        var str = "Game Over";
        //TODO: Add run stats here
        panel.write(str, (panel.getWidthInCharacters() /  2) - (str.length() / 2), panel.getHeightInCharacters() / 2, Color.RED);
        panel.repaint();
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
