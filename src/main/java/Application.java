import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

@SuppressWarnings("BusyWait")
public class Application extends JFrame {

    static class LogEntry {
        final String message;
        final Long timestamp;

        public LogEntry(String message, Long timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }

        public void print() {
            System.out.println(message);
        }
    }

    static class Location {
        int x = 0;
        int y = 0;

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }


        public Location() {
        }
    }

    private final int screenWidth = 80;
    private final int screenHeight = 50;

    Location player = new Location(screenWidth / 2, screenHeight / 2);

    KeyEvent keyEvent = null;

    private final AsciiPanel panel;

    private final static java.util.List<LogEntry> log = new ArrayList<>();

    public static void log(String message) {
        var entry = new LogEntry(message, System.currentTimeMillis());
        log.add(entry);
        entry.print();
    }

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

    public void execute() throws InterruptedException {
        while (true) {
            var option = EventHandler.keyDown(keyEvent);
            if (option.isPresent()) {
                var action = option.get();
                if (action instanceof Action.EscapeAction) {
                    log("Escape Action");
                    break;
                }

                if (action instanceof Action.MovementAction) {
                    log("Movement Action");
                    player.x += ((Action.MovementAction) action).dx();
                    player.y += ((Action.MovementAction) action).dy();
                }
            }

            panel.clear();
            panel.write("@", player.x, player.y);
            panel.repaint();

            keyEvent = null;
            while (keyEvent == null) {
                Thread.sleep(5);
            }
        }
        log("Exiting Game");
        System.exit(0);
    }

    public static void main(String[] args) throws InterruptedException {
        var app = new Application();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        app.execute();
    }

}
