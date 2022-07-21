package org.southy.rl.ui;

import org.southy.rl.ColorUtils;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;

import java.awt.*;
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

    public static void renderUIBorders(AsciiPanel panel, GameMap gameMap) {
        try {
            for (String[] data : UI) {
                panel.write((char) Integer.parseInt(data[2]), Integer.parseInt(data[0]), Integer.parseInt(data[1]), Color.decode(data[3]), Color.decode(data[4]));
            }
        } catch (Throwable e) {
            throw new RuntimeException("Unable to build UI");
        }
    }

    public static void renderBar(AsciiPanel panel, int currentValue, int maxValue, int totalWidth) {

        int barWidth = (int) ((float) currentValue / maxValue * totalWidth);
        panel.write("HP:" + currentValue + "/" + maxValue, 2, 2, ColorUtils.BAR_TEXT);
//        for (int i = 0; i < totalWidth; i++) {
//            if (i >= 1 && (i - 1) < str.length) {
//                panel.write(str[i - 1], i + 2, 2, ColorUtils.BAR_TEXT, i < barWidth ? ColorUtils.BAR_FILLED : ColorUtils.BAR_EMPTY);
//            } else {
//                panel.write(' ', i + 2, 2, ColorUtils.BAR_TEXT, i < barWidth ? ColorUtils.BAR_FILLED : ColorUtils.BAR_EMPTY);
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
