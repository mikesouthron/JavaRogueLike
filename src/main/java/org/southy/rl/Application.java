package org.southy.rl;

import org.southy.rl.asciipanel.AsciiFont;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.exceptions.Impossible;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

@SuppressWarnings("BusyWait")
public class Application extends JFrame {

    public static final int screenWidth = 100;
    public static final int screenHeight = 60;

    public static final int mapWidth = 120;

    public static final int mapHeight = 86;

    public static final int roomMaxSize = 10;
    public static final int roomMinSize = 6;
    public static final int maxRooms = 30;

    public static final int maxMonstersPerRoom = 2;
    public static final int maxItemsPerRoom = 2;

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

    public static Engine engine;
    public static Camera normalCamera = new Camera();
    public static Camera fullMapCamera = new Camera();
    public static Camera camera = normalCamera;

    @SuppressWarnings("InfiniteLoopStatement")
    public void execute() throws InterruptedException, IOException, ClassNotFoundException, Impossible {
        fullMapCamera.height = screenHeight;
        fullMapCamera.width = screenWidth;
        engine = new Engine();

        while (true) {
            try {
                engine.eventHandler.handleEvents(keyEvent);
            } catch (Impossible e) {
                engine.logger.addMessage(e.getMessage(), ColorUtils.IMPOSSIBLE, true);
            }
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

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, Impossible {
        var app = new Application();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
        app.setTitle("Dread Dungeon");
        app.execute();
    }

}
