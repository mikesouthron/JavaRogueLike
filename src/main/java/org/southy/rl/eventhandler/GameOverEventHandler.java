package org.southy.rl.eventhandler;

import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.sdl.SDL;

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
    public void handleEvents(SDL sdl) {
        var keyEvent = sdl.SDLGetEvent();
        if (keyEvent == null) {
            return;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            engine.eventHandler = new MainMenuHandler(engine);
        }
    }

    @Override
    public void onRender(SDL sdl) {
        var str = "Game Over";
        //TODO: Add run stats here
        sdl.write(str, (sdl.getWidthInCharacters() /  2) - (str.length() / 2), sdl.getHeightInCharacters() / 2, ColorUtils.color(255, 0, 0));
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
