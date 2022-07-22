package org.southy.rl.eventhandler;

import org.southy.rl.Application;
import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.EntityFactory;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.gen.Procgen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MainMenuHandler implements EventHandler {

    enum MenuOptions {
        NEW("New Game"), CONTINUE("Continue"), QUIT("Quit");

        public String display;

        MenuOptions(String display) {
            this.display = display;
        }
    }

    Engine engine;

    int selectedIdx;

    boolean saveGameAvailable;

    List<MenuOptions> options = List.of(MenuOptions.NEW, MenuOptions.CONTINUE, MenuOptions.QUIT);

    public MainMenuHandler(Engine engine) {
        this.engine = engine;
        saveGameAvailable = Files.exists(Path.of("game.save"));
    }

    @Override
    public void handleEvents(KeyEvent event) throws Impossible {
        if (event == null) {
            return;
        }
        if (event.getKeyCode() == KeyEvent.VK_UP) {
            selectedIdx--;
            if (selectedIdx >= 0 && options.get(selectedIdx) == MenuOptions.CONTINUE && !saveGameAvailable) {
                selectedIdx--;
            }
        }
        if (event.getKeyCode() == KeyEvent.VK_DOWN) {
            selectedIdx++;
            if (selectedIdx < options.size() && options.get(selectedIdx) == MenuOptions.CONTINUE && !saveGameAvailable) {
                selectedIdx++;
            }
        }

        if (selectedIdx < 0) {
            selectedIdx = options.size() - 1;
        }
        if (selectedIdx >= options.size()) {
            selectedIdx = 0;
        }

        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (options.get(selectedIdx)) {
                case NEW:
                    engine.eventHandler = new MainGameEventHandler(engine);
                    engine.player = EntityFactory.player();
                    engine.gameMap = Procgen.generateDungeon(engine, Application.maxRooms, Application.roomMinSize, Application.roomMaxSize, Application.mapWidth, Application.mapHeight, Application.maxMonstersPerRoom, Application.maxItemsPerRoom);
                    engine.updateFov();
                    break;
                case CONTINUE:
                    if (saveGameAvailable) {
                        try (var os = new ObjectInputStream(new FileInputStream("game.save"))) {
                            Application.engine = (Engine) os.readObject();
                            Application.camera.x = Application.engine.player.x;
                            Application.camera.y = Application.engine.player.y;
                            Application.engine.updateFov();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                            throw new Impossible(e.getMessage());
                        }
                    }
                    break;
                case QUIT:
                    System.exit(0);
                    break;
            }
        }
    }

    @Override
    public void onRender(AsciiPanel panel) {
        panel.clear();
        int yOffset = panel.getHeightInCharacters() / 2 - options.size();
        panel.write("Dread Dungeon", panel.getWidthInCharacters() / 2 - "Dread Dungeon".length() / 2, yOffset - 10, panel.getDefaultForegroundColor(), ColorUtils.color(100, 0, 0));
        for (MenuOptions option : options) {
            int xOffset = panel.getWidthInCharacters() / 2 - option.display.length() / 2;
            boolean selected = option == options.get(selectedIdx);
            if (selected) {
                xOffset -= 2;
            }
            var display =  selected ? "* " + option.display + " *" : option.display;
            var color = Color.WHITE;
            if (option == MenuOptions.CONTINUE && !saveGameAvailable) {
                color = Color.GRAY;
            }
            panel.write(display, xOffset, yOffset, color, Color.BLACK);
            yOffset += 2;
        }
        panel.repaint();
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
