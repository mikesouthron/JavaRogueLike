package org.southy.rl;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@SuppressWarnings("BusyWait")
public class Application extends JFrame {

    private final int screenWidth = 80;
    private final int screenHeight = 50;

    private final int mapWidth = 80;

    private final int mapHeight = 45;

    KeyEvent keyEvent = null;

    private final AsciiPanel panel;

    private final Logger logger = new Logger();

    public Application() throws HeadlessException {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                keyEvent = e;
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
        var player = new Entity(screenWidth / 2, screenHeight / 2, '@', Color.WHITE);
        var npc = new Entity((screenWidth / 2) - 5, screenHeight / 2, '@', Color.YELLOW);

        var entities = java.util.List.of(npc, player);

        var gameMap = new GameMap(mapWidth, mapHeight);

        var engine = new Engine(entities, player, gameMap, logger);

        while (true) {
            engine.handleEvent(keyEvent);
            engine.render(panel);
            keyEvent = null;
            while (keyEvent == null) {
                Thread.sleep(5);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var app = new Application();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        app.execute();
    }

}
