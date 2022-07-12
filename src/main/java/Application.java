import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@SuppressWarnings("BusyWait")
public class Application extends JFrame {
    static class Location {
        int x = 0;
        int y = 0;
    }

    Location player = new Location();

    int keyPressed = -1;

    private final AsciiPanel panel;

    public Application() throws HeadlessException {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                keyPressed = e.getKeyCode();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        panel = new AsciiPanel(80, 24, AsciiFont.CP437_16x16);
        add(panel);
        pack();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void execute() throws InterruptedException {
        while (true) {
            switch (keyPressed) {
                case 100 -> player.x--;
                case 102 -> player.x++;
                case 104 -> player.y--;
                case 98 -> player.y++;
                case 99 -> {
                    player.x++;
                    player.y++;
                }
                case 97 -> {
                    player.x--;
                    player.y++;
                }
                case 103 -> {
                    player.x--;
                    player.y--;
                }
                case 105 -> {
                    player.x++;
                    player.y--;
                }
                default -> System.out.println(keyPressed);
            }

            panel.clear();
            panel.write("@", player.x, player.y);
            panel.repaint();

            keyPressed = -1;
            while (keyPressed < 0) {
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
