package org.southy.rl.gen;

import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.RandomUtils;
import org.southy.rl.components.*;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Entity;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.GameMap;
import org.southy.rl.map.Location;
import org.southy.rl.map.RectangularRoom;
import org.southy.rl.map.Tile;
import org.southy.rl.ui.Render;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Procgen {

    static class ItemDesc {
        public int id;
        public String name;
        public BodyPart bodyPart;
        public int atkLow, atkHigh, defLow, defHigh;
        public double strMod, defMod;
        public int level; //Minimum dlvl Item can appear on
    }
    //For items with attack the floor level > 1 can cause strMod to appear + 1 == strMod of 1.025, + 2 == strMod of 1.05

    public enum EnemyType {
        SCOUT("Scout", 's'), PATROL("Wanderer", 'w'), PATROL_LEADER("Leader", 'L'), DEFENDER("Defender", 'D');

        String name;
        char symbol;

        EnemyType(String name, char symbol) {
            this.name = name;
            this.symbol = symbol;
        }
    }

    public static class EnemyDesc {
        char str;
        int strength, agility, constitution, intelligence;
    }

    public static Map<Integer, ItemDesc> itemDescMap = new HashMap<>();

    public static Map<EnemyType, EnemyDesc> enemyMap = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
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
            current.level = Integer.parseInt(lines.get(startIdx + 5));
            itemDescMap.put(current.id, current);
            startIdx += 7;
        }

        /*
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
         */


        path = Path.of(Render.class.getClassLoader().getResource("enemies.txt").toURI());
        lines = Files.readAllLines(path);

        startIdx = 0;
        while (startIdx + 2 < lines.size()) {
            EnemyDesc enemyDesc = new EnemyDesc();
            EnemyType type = EnemyType.valueOf(lines.get(startIdx));
            enemyDesc.str = lines.get(startIdx + 1).toCharArray()[0];
            var stats = lines.get(startIdx + 2).split(" ");
            enemyDesc.strength = Integer.parseInt(stats[0]);
            enemyDesc.constitution = Integer.parseInt(stats[1]);
            enemyDesc.agility = Integer.parseInt(stats[2]);
            enemyDesc.intelligence = Integer.parseInt(stats[3]);

            enemyMap.put(type, enemyDesc);

            startIdx += 4;
        }
    }

    static {
        try {
            parseLists();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void spawnEquipableFromItemDesc(ItemDesc itemDesc, int level, GameMap gameMap, int x, int y) {
        double strMod = itemDesc.strMod;
        double defMod = itemDesc.defMod;

        String name = itemDesc.name;

        if (level > itemDesc.level) {
            if (itemDesc.atkHigh > 0) {
                if (strMod == 0) {
                    strMod = 1;
                }
                strMod += itemDesc.level * 0.1;
            }
            if (itemDesc.defHigh > 0) {
                if (defMod == 0) {
                    defMod = 1;
                }
                defMod += itemDesc.level * 0.1;
            }
            name = name + " +" + (level - itemDesc.level);
        }
        new Equipable(gameMap, x, y, itemDesc.bodyPart.symbol, ColorUtils.WHITE, name, itemDesc.bodyPart, itemDesc.atkLow, itemDesc.atkHigh, strMod, itemDesc.defLow, itemDesc.defHigh, defMod, 0, 0);
    }

    private static Equipable createEquipableFromItemDesc(ItemDesc itemDesc, int level) {
        double strMod = itemDesc.strMod;
        double defMod = itemDesc.defMod;

        String name = itemDesc.name;

        if (level > itemDesc.level) {
            if (itemDesc.atkHigh > 0) {
                if (strMod == 0) {
                    strMod = 1;
                }
                strMod += itemDesc.level * 0.05;
            }
            if (itemDesc.defHigh > 0) {
                if (defMod == 0) {
                    defMod = 1;
                }
                defMod += itemDesc.level * 0.05;
            }
            name = name + " +" + (level - itemDesc.level);
        }
        return new Equipable(null, 0, 0, itemDesc.bodyPart.symbol, ColorUtils.WHITE, name, itemDesc.bodyPart, itemDesc.atkLow, itemDesc.atkHigh, strMod, itemDesc.defLow, itemDesc.defHigh, defMod, 0, 0);
    }

    public static void spawnEnemyFromEnemyDesc(EnemyDesc enemyDesc, EnemyType type, int level, GameMap gameMap, int x, int y) {

        int strength = enemyDesc.strength;
        int agility = enemyDesc.agility;
        int constitution = enemyDesc.constitution;
        int intelligence = enemyDesc.intelligence;
        if (level > 1) {
            strength *= (level / 2.0);
            agility *= (level / 2.0);
            constitution *= (level / 2.0);
            intelligence *= (level / 2.0);
        }
        var actor = new Actor(gameMap, x, y, type.symbol, ColorUtils.color(0, 200, 200), type.name, new Fighter(strength, agility, constitution, intelligence), HostileEnemy.class, new Inventory(0), 0);
        var weapon = createEquipableFromItemDesc(itemDescMap.get(1), level);
        weapon.setParent(actor);
        actor.equipment.items[EquipSlot.RIGHT_HAND.idx] = weapon;
    }

    public static GameMap generateDungeon(Engine engine, int level, int maxRooms, int roomMinSize, int roomMaxSize, int mapWidth, int mapHeight,
                                          int maxMonstersPerRoom, int maxItemsPerRoom)
            throws Impossible {

        var itemPool = itemDescMap.values().stream().filter(i -> level >= i.level).collect(Collectors.toList());

        //Tweak all of these numbers!
        int maxItemsPerItemRoom = level * 2;
        int itemRooms = level * 3; //FIXME: Maybe make this number better?
        int monsterSpawns = level * 4;
        int monsterGenRooms = (int) Math.ceil(level / 2.0);
        int genRoomCount = 0;

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

            int centre = newRoom.centre(mapWidth);

            if (rooms.size() == 0) {
                player.place(centre % mapWidth, centre / mapWidth, dungeon);
            } else {
                dungeon.digTunnel(newRoom, rooms.get(rooms.size() - 1));
            }

            if (i > 0 && i < maxRooms - 1) {
                switch (RandomUtils.randomInt(0, 2)) {
                    case 0:
                        if (itemRooms > 0 && itemPool.size() > 0) {
                            spawnItems(maxItemsPerItemRoom, itemPool, level, dungeon, newRoom);
                            itemRooms--;
                        }
                        break;
                    case 1:
//                        if (monsterSpawns > 0) {
//                            spawnMonsters(level, dungeon, newRoom);
//                            monsterSpawns--;
//                        }
                        break;
                    case 2:
                        if (monsterGenRooms > 0) {
                            System.out.println("Portal Created " + centre % mapWidth + "," + centre / mapWidth);
                            dungeon.portalList.add(new Location(centre % mapWidth, centre / mapWidth));
                            monsterGenRooms--;
                            genRoomCount++;
                        }
                        break;
                }
            }

            if (i == maxRooms - 2 && genRoomCount == 0) {
                System.out.println("Portal Created " + centre % mapWidth + "," + centre / mapWidth);
                dungeon.portalList.add(new Location(centre % mapWidth, centre / mapWidth));
                monsterGenRooms--;
                genRoomCount++;
            }

            lastRoomCentre = newRoom.centre(mapWidth);

            rooms.add(newRoom);
        }

        dungeon.tiles[lastRoomCentre] = Tile.upStairs();
        dungeon.upstairsX = lastRoomCentre % mapWidth;
        dungeon.upstairsY = lastRoomCentre / mapWidth;

        for (Location location : dungeon.portalList) {
            dungeon.tiles[location.x + location.y * mapWidth] = Tile.portal();
        }

        return dungeon;
    }

    private static void spawnItems(int maxItemsPerItemRoom, List<ItemDesc> itemPool, int level, GameMap map, RectangularRoom room) {
        int x = room.x1 + 1;
        int y = room.y1 + 1;
        int numItems = RandomUtils.randomInt(1, maxItemsPerItemRoom);
        for (int j = 0; j < numItems; j++) {
            if (itemPool.size() > 0) {
                var item = itemPool.get(RandomUtils.randomInt(0, itemPool.size() - 1));
                itemPool.remove(item); //Only 1 item of each type is possible to appear
                spawnEquipableFromItemDesc(item, level, map, x, y);
                x++;
            }
        }

        int centre = room.centre(map.width);
        for (int i = 0; i < Math.min(level - 1, 5) ; i++) {
            spawnEnemyFromEnemyDesc(enemyMap.get(EnemyType.DEFENDER), EnemyType.DEFENDER, level, map, (centre % map.width) + i, (centre / map.width) + i);
        }

    }

    private static void spawnMonsters(int level, GameMap map, RectangularRoom room) {
        int centre = room.centre(map.width);
        int monsterType = RandomUtils.randomInt(0, 1);

        switch (monsterType) {
            case 0:
                spawnEnemyFromEnemyDesc(enemyMap.get(EnemyType.SCOUT), EnemyType.SCOUT, level, map, centre % map.width, centre / map.width);
                break;
            case 1:
                spawnEnemyFromEnemyDesc(enemyMap.get(EnemyType.PATROL), EnemyType.PATROL, level, map, centre % map.width, centre / map.width);
                break;
        }
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
