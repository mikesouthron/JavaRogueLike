package org.southy.rl.gen;

import org.southy.rl.Color;
import org.southy.rl.Engine;
import org.southy.rl.RandomUtils;
import org.southy.rl.components.BodyPart;
import org.southy.rl.entity.Entity;
import org.southy.rl.entity.EntityFactory;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.GameMap;
import org.southy.rl.map.RectangularRoom;
import org.southy.rl.map.Tile;
import org.southy.rl.ui.Render;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Procgen {

    static class ItemDesc {
        public int id;
        public String name;
        public BodyPart bodyPart;
        public int atkLow, atkHigh, defLow, defHigh;
        public double strMod, defMod;
    }

    static class Floor {
        int floor;
        int itemSet;
        int statMultiplier;
        int chance;

        public Floor(int floor, int itemSet, int statMultiplier, int chance) {
            this.floor = floor;
            this.itemSet = itemSet;
            this.statMultiplier = statMultiplier;
            this.chance = chance;
        }
    }

    static class EnemyDesc {
        Map<Integer, Floor> floorMap = new HashMap<>();
        char str;
        String name;
        Color color;
        int strength, agility, constitution, intelligence;
        int hpRestore;
        String aiName;
    }

    static Map<Integer, ItemDesc> itemDescMap = new HashMap<>();
    static Map<Integer, Set<ItemDesc>> itemSetMap = new HashMap<>();

    static Map<Integer, Set<EnemyDesc>> floorToEnemyMap = new HashMap<>();

    private static void parseLists() throws URISyntaxException, IOException {
        var path = Path.of(Render.class.getClassLoader().getResource("items.txt").toURI());
        var lines = Files.readAllLines(path);

        int startIdx = 0;
        while (startIdx + 4 < lines.size()) {
            ItemDesc current = new ItemDesc();
            current.id = Integer.parseInt(lines.get(startIdx));
            current.name = lines.get(startIdx + 1);
            current.bodyPart = BodyPart.valueOf(lines.get(startIdx + 2));
            var s = lines.get(startIdx + 3).split(" ");
            current.atkLow = Integer.parseInt(s[0]);
            current.atkHigh = Integer.parseInt(s[1]);
            current.defLow = Integer.parseInt(s[2]);
            current.defHigh = Integer.parseInt(s[3]);
            s = lines.get(startIdx + 4).split(" ");
            current.strMod = Double.parseDouble(s[0]);
            current.defMod = Double.parseDouble(s[1]);
            itemDescMap.put(current.id, current);
            startIdx += 6;
        }

        path = Path.of(Render.class.getClassLoader().getResource("itemsets.txt").toURI());
        lines = Files.readAllLines(path);

        startIdx = 0;
        while (startIdx + 1 < lines.size()) {
            int id = Integer.parseInt(lines.get(startIdx));
            var set = new HashSet<ItemDesc>();
            for (String s : lines.get(startIdx + 1).split(" ")) {
                set.add(itemDescMap.get(Integer.parseInt(s)));
            }
            itemSetMap.put(id, set);
            startIdx += 3;
        }


        path = Path.of(Render.class.getClassLoader().getResource("enemies.txt").toURI());
        lines = Files.readAllLines(path);

        startIdx = 0;
        while (startIdx + 6 < lines.size()) {

            int id = Integer.parseInt(lines.get(startIdx));
            var set = new HashSet<ItemDesc>();
            for (String s : lines.get(startIdx + 1).split(" ")) {
                set.add(itemDescMap.get(Integer.parseInt(s)));
            }
            itemSetMap.put(id, set);

            EnemyDesc enemyDesc = new EnemyDesc();

            var s = lines.get(startIdx).split(" ");
            int floorIdx = 0;
            while (floorIdx < s.length) {
                int floor = Integer.parseInt(s[floorIdx]);
                int statMultiplier = Integer.parseInt(s[floorIdx + 1]);
                int itemSet = Integer.parseInt(s[floorIdx + 2]);
                int chance = Integer.parseInt(s[floorIdx + 3]);

                Floor floorObj = new Floor(floor, itemSet, statMultiplier, chance);

                enemyDesc.floorMap.put(floor, floorObj);

                var enemySet = floorToEnemyMap.getOrDefault(floor, new HashSet<>());
                enemySet.add(enemyDesc);
                floorToEnemyMap.putIfAbsent(floor, enemySet);

                floorIdx += 4;
            }


            //char
            //name
            //color
            //stats
            //ainame

            startIdx += 8;
        }
        /*
        FLOOR STAT_MULTIPLIER ITEM_SET %CHANCE
char
name
color
str agi con int
hp_restore
AI_NAME
         */






    }

    static {
        try {
            parseLists();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GameMap generateDungeon(Engine engine, int maxRooms, int roomMinSize, int roomMaxSize, int mapWidth, int mapHeight,
            int maxMonstersPerRoom, int maxItemsPerRoom)
            throws Impossible {


        var player = engine.player;
        var entities = new ArrayList<Entity>();
        var dungeon = new GameMap(engine, mapWidth, mapHeight, entities);

        var rooms = new ArrayList<RectangularRoom>();

        int lastRoomCentre = 0;

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
                player.place(centre % mapWidth, centre / mapWidth, dungeon);
            } else {
                dungeon.digTunnel(newRoom, rooms.get(rooms.size() - 1));
            }

            placeEntities(newRoom, dungeon, maxMonstersPerRoom, maxItemsPerRoom);

            lastRoomCentre = newRoom.centre(mapWidth);

            rooms.add(newRoom);
        }

        dungeon.tiles[lastRoomCentre] = Tile.upStairs();
        dungeon.upstairsX = lastRoomCentre % mapWidth;
        dungeon.upstairsY = lastRoomCentre / mapWidth;

        return dungeon;
    }

    private static void placeEntities(RectangularRoom room, GameMap map, int maxMonstersPerRoom, int maxItemsPerRoom) {
        var numberOfMonsters = RandomUtils.randomInt(0, maxMonstersPerRoom);
        for (int i = 0; i < numberOfMonsters; i++) {
            var x = RandomUtils.randomInt(room.x1 + 1, room.x2 - 1);
            var y = RandomUtils.randomInt(room.y1 + 1, room.y2 - 1);

            boolean existingEntity = false;
            for (Entity entity : map.entities) {
                if (entity.x == x && entity.y == y) {
                    existingEntity = true;
                    break;
                }
            }
            if (!existingEntity) {
                if (RandomUtils.randomInt(0, 10) < 8) {
                    EntityFactory.orc(map, x, y);
                } else {
                    EntityFactory.troll(map, x, y);
                }
            }
        }


        for (int x = room.x1 + 1; x < room.x2; x++) {
            var y = room.y1 + 1;
            int idx = x - room.x1;
            if (idx == 1) {
                EntityFactory.sword(map, x, y);
            }
            if (idx == 2) {
                EntityFactory.ringOfAtk(map, x, y);
            }
            if (idx == 3) {
                EntityFactory.ringOfDef(map, x, y);
            }
            if (idx == 4) {
                EntityFactory.ringOfAtkDef(map, x, y);
            }
        }

        /*
        var numberOfItems = RandomUtils.randomInt(0, maxItemsPerRoom);
        for (int i = 0; i < numberOfItems; i++) {
            var x = RandomUtils.randomInt(room.x1 + 1, room.x2 - 1);
            var y = RandomUtils.randomInt(room.y1 + 1, room.y2 - 1);

            boolean existingEntity = false;
            for (Entity entity : map.entities) {
                if (entity.x == x && entity.y == y) {
                    existingEntity = true;
                    break;
                }
            }
            if (!existingEntity) {
                int rand = RandomUtils.randomInt(0, 10);
                if (rand > 9) {
                    EntityFactory.sword(map, x, y);
                }
            }
        }
         */
    }

    public static List<Integer> tunnel(int start, int end, int mapWidth) {
        int x1 = start % mapWidth;
        int y1 = start / mapWidth;

        int x2 = end % mapWidth;
        int y2 = end / mapWidth;

        int cornerX = x2;
        int cornerY = y1;

        if (RandomUtils.randomInt(0, 10) > 5) {
            cornerX = x1;
            cornerY = y2;
        }

        var line = new ArrayList<Integer>();

        var b = new Bresenham(x1, cornerX, y1, cornerY);
        var p = new Position();
        while (!b.step(p)) {
            line.add(toIdx(p.x, p.y, mapWidth));
        }
        b = new Bresenham(cornerX, x2, cornerY, y2);
        while (!b.step(p)) {
            line.add(toIdx(p.x, p.y, mapWidth));
        }

        return line;
    }

    public static List<Integer> plotPixel(int x1, int y1, int x2, int y2, int dx, int dy, int decide, int mapWidth) {
        List<Integer> plot = new ArrayList<>();
        //pk is initial decision making parameter
        //Note:x1&y1,x2&y2, dx&dy values are interchanged
        //and passed in plotPixel function so
        //it can handle both cases when m>1 & m<1
        int pk = 2 * dy - dx;
        for (int i = 0; i <= dx; i++) {
            plot.add(toIdx(x1, y1, mapWidth));
            //checking either to decrement or increment the value
            //if we have to plot from (0,100) to (100,0)
            if (x1 < x2)
                x1++;
            else
                x1--;
            if (pk < 0) {
                //decision value will decide to plot
                //either  x1 or y1 in x's position
                if (decide == 0) {
                    pk = pk + 2 * dy;
                } else
                    pk = pk + 2 * dy;
            } else {
                if (y1 < y2)
                    y1++;
                else
                    y1--;
                pk = pk + 2 * dy - 2 * dx;
            }
        }

        return plot;
    }

    public static List<Integer> drawLine(int x1, int y1, int x2, int y2, int mapWidth) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        //If slope is less than one
        if (dx > dy) {
            //passing argument as 0 to plot(x,y)
            return plotPixel(x1, y1, x2, y2, dx, dy, 0, mapWidth);
        }
        //if slope is greater than or equal to 1
        else {
            //passing argument as 1 to plot (y,x)
            return plotPixel(y1, x1, y2, x2, dy, dx, 1, mapWidth);
        }
    }

    public static int toIdx(int x, int y, int mapWidth) {
        return (x + y * mapWidth);
    }

}
