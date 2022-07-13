package org.southy.rl;

import org.southy.rl.asciipanel.AsciiFont;
import org.southy.rl.asciipanel.AsciiPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("BusyWait")
public class Application extends JFrame {

    private final int screenWidth = 80;
    private final int screenHeight = 50;

    private final int mapWidth = 80;

    private final int mapHeight = 45;

    private final int roomMaxSize = 10;
    private final int roomMinSize = 6;
    private final int maxRooms = 30;

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

    public GameMap generateDungeon(int maxRooms, int roomMinSize, int roomMaxSize, int mapWidth, int mapHeight, Entity player) {
        var dungeon = new GameMap(mapWidth, mapHeight);

        var rooms = new ArrayList<RectangularRoom>();

        for (int i = 0; i < maxRooms; i++) {
            var roomWidth = RandomUtils.randomInt(roomMinSize, roomMaxSize);
            var roomHeight = RandomUtils.randomInt(roomMinSize, roomMaxSize);

            var x = RandomUtils.randomInt(0, dungeon.width - roomWidth - 1);
            var y = RandomUtils.randomInt(0, dungeon.height - roomHeight - 1);

            var newRoom = new RectangularRoom(x, y, roomWidth, roomHeight);

            boolean intersect = false;

            for (RectangularRoom room : rooms) {
                if (newRoom.intersects(room)) {
                    intersect = true;
                    break;
                }
            }

            if (intersect) {
                continue;
            }

            dungeon.dig(newRoom);

            if (rooms.size() == 0) {
                int centre = newRoom.centre(mapWidth);
                player.x = centre % mapWidth;
                player.y = centre / mapWidth;
            } else {
                dungeon.digTunnel(newRoom, rooms.get(rooms.size() - 1));
            }

            rooms.add(newRoom);
        }

//        var roomOne = new RectangularRoom(20, 15, 10, 15);
//        var roomTwo = new RectangularRoom(35, 5, 10, 10);
//
//        dungeon.dig(roomOne);
//        dungeon.dig(roomTwo);
//        dungeon.digTunnel(roomOne, roomTwo);

        return dungeon;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void execute() throws InterruptedException {
        var player = new Entity(screenWidth / 2, screenHeight / 2, '@', Color.WHITE);
        var npc = new Entity((screenWidth / 2) - 5, screenHeight / 2, '@', Color.YELLOW);

        var entities = java.util.List.of(npc, player);

        var gameMap = generateDungeon(maxRooms, roomMinSize, roomMaxSize, mapWidth, mapHeight, player);

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
