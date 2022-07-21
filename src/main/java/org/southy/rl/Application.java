package org.southy.rl;

import org.southy.rl.asciipanel.AsciiFont;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.EntityFactory;
import org.southy.rl.gen.Procgen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("BusyWait")
public class Application extends JFrame {

    public static final int screenWidth = 100;
    public static final int screenHeight = 60;

    public static final int mapWidth = 60;

    public static final int mapHeight = 43;

    public static final int roomMaxSize = 10;
    public static final int roomMinSize = 6;
    public static final int maxRooms = 30;

    public static final int maxMonstersPerRoom = 2;

    KeyEvent keyEvent = null;

    private final AsciiPanel panel;

    public Application() throws HeadlessException {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != 16 && e.getKeyCode() != 17) {
                    keyEvent = e;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel = new AsciiPanel(screenWidth, screenHeight, AsciiFont.CP437_16x16);
        add(panel);
        pack();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void execute() throws InterruptedException, IOException, ClassNotFoundException {
        Engine engine;
        if (Files.exists(Paths.get("game.save"))) {
            try (var os = new ObjectInputStream(new FileInputStream("game.save"))) {
                engine = (Engine) os.readObject();
            }
        } else {
            engine = new Engine(EntityFactory.player());
            engine.gameMap = Procgen.generateDungeon(engine, maxRooms, roomMinSize, roomMaxSize, mapWidth, mapHeight, maxMonstersPerRoom);
            engine.logger.addMessage("Hello and welcome, adventurer, to yet another dungeon!", ColorUtils.WELCOME_TEXT);
        }

        engine.updateFov();

        while (true) {
            if (engine.player.isAlive()) {
                long start = System.currentTimeMillis();
                try (var os = new ObjectOutputStream(new FileOutputStream("game.save"))) {
                    os.writeObject(engine);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Saved in " + (System.currentTimeMillis() - start) + "ms");
            } else {
                Files.deleteIfExists(Paths.get("game.save"));
            }
            engine.eventHandler.handleEvents(keyEvent);
            engine.eventHandler.onRender(panel);
            if (engine.fastMove != null) {
                Thread.sleep(10);
            } else {
                keyEvent = null;
            }
            while (keyEvent == null) {
                Thread.sleep(5);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        var app = new Application();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setLocationRelativeTo(null);
//        app.setExtendedState(JFrame.MAXIMIZED_BOTH);
        app.setVisible(true);
        app.execute();
    }

}
