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

    private final int screenWidth = 80;
    private final int screenHeight = 50;

    private final int mapWidth = 80;

    private final int mapHeight = 45;

    private final int roomMaxSize = 10;
    private final int roomMinSize = 6;
    private final int maxRooms = 30;

    private final int maxMonstersPerRoom = 2;

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
        var player = EntityFactory.player.copy();
        var engine = new Engine(player, logger);
        engine.gameMap = Procgen.generateDungeon(engine, maxRooms, roomMinSize, roomMaxSize, mapWidth, mapHeight, maxMonstersPerRoom);
        engine.updateFov();

        while (true) {
            engine.eventHandler.handleEvents(keyEvent);
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
