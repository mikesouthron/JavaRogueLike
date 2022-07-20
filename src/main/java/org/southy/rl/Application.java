package org.southy.rl;

import org.southy.rl.asciipanel.AsciiFont;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.EntityFactory;
import org.southy.rl.gen.Procgen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@SuppressWarnings("BusyWait")
public class Application extends JFrame {

    public static final int screenWidth = 80;
    public static final int screenHeight = 50;

    public static final int mapWidth = 80;

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
    public void execute() throws InterruptedException {
        var player = EntityFactory.player();
        var engine = new Engine(player);
//        engine.gameMap = Procgen.generateDungeon(engine, 1, roomMinSize, roomMaxSize, mapWidth, mapHeight, 1);
        engine.gameMap = Procgen.generateDungeon(engine, maxRooms, roomMinSize, roomMaxSize, mapWidth, mapHeight, maxMonstersPerRoom);

        engine.logger.addMessage("Hello and welcome, adventurer, to yet another dungeon!", ColorUtils.WELCOME_TEXT);

        engine.updateFov();

        while (true) {
            engine.eventHandler.handleEvents(keyEvent);
            engine.eventHandler.onRender(panel);
            if (engine.fastMove != null) {
                Thread.sleep(15);
            } else {
                keyEvent = null;
            }
            while (keyEvent == null) {
                Thread.sleep(5);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var app = new Application();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setLocationRelativeTo(null);
//        app.setExtendedState(JFrame.MAXIMIZED_BOTH);
        app.setVisible(true);
        app.execute();
    }

}
