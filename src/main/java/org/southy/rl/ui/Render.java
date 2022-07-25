package org.southy.rl.ui;

import org.southy.rl.ColorUtils;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;
import org.southy.sdl.SDL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Render {

    static java.util.List<String[]> UI = new ArrayList<>();
    static {
        try {
            var path = Path.of(Render.class.getClassLoader().getResource("UIBorders.txt").toURI());
            for (String line : Files.readAllLines(path)) {
                var data = line.split(",");
                if (!data[2].equals("32")) {
                    UI.add(data);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void renderUIBorders(SDL sdl, GameMap gameMap) {
        try {
            for (String[] data : UI) {
                sdl.write((char) Integer.parseInt(data[2]), Integer.parseInt(data[0]), Integer.parseInt(data[1]), ColorUtils.decode(data[3]), ColorUtils.decode(data[4]));
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to build UI");
        }
    }

    public static void renderBar(SDL sdl, int currentValue, int maxValue, int totalWidth, int dungeonLevel) {
        int barWidth = (int) ((float) currentValue / maxValue * totalWidth);
        sdl.write("DLvl " + dungeonLevel, 2, 2, ColorUtils.BAR_TEXT);
        sdl.write("HP   " + currentValue + "/" + maxValue, 2, 3, ColorUtils.BAR_TEXT);
//        for (int i = 0; i < totalWidth; i++) {
//            if (i >= 1 && (i - 1) < str.length) {
//                sdl.write(str[i - 1], i + 2, 2, ColorUtils.BAR_TEXT, i < barWidth ? ColorUtils.BAR_FILLED : ColorUtils.BAR_EMPTY);
//            } else {
//                sdl.write(' ', i + 2, 2, ColorUtils.BAR_TEXT, i < barWidth ? ColorUtils.BAR_FILLED : ColorUtils.BAR_EMPTY);
//            }
//        }
    }

    public static java.util.List<Entity> getNamesAtLocation(int x, int y, GameMap gameMap) {
        var list = new ArrayList<Entity>();

        for (Entity entity : gameMap.entities) {
            if (entity.x == x && entity.y == y) {
                list.add(entity);
            }
        }

        return list;
    }

    //TODO: Create Interface to show enemy symbols -> names on the right hand of the screen.

}
