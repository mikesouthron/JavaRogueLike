package org.southy.rl.ui;

import org.southy.rl.Application;
import org.southy.rl.ColorUtils;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;

import java.awt.*;
import java.util.ArrayList;

public class Render {

    public static void renderUIBorders(AsciiPanel panel, GameMap gameMap) {
        panel.write("+", GameMap.MAP_OFFSET_X - 1, GameMap.MAP_OFFSET_Y - 1, ColorUtils.UI_OUTLINE_COLOR);
        panel.write("+", GameMap.MAP_OFFSET_X - 1, gameMap.height + GameMap.MAP_OFFSET_Y, ColorUtils.UI_OUTLINE_COLOR);
        panel.write("+", gameMap.width + GameMap.MAP_OFFSET_X, GameMap.MAP_OFFSET_Y - 1, ColorUtils.UI_OUTLINE_COLOR);
        panel.write("+", gameMap.width + GameMap.MAP_OFFSET_X, gameMap.height + GameMap.MAP_OFFSET_Y, ColorUtils.UI_OUTLINE_COLOR);
        for (int i = 0; i < gameMap.width; i++) {
            panel.write((char)196, i + GameMap.MAP_OFFSET_X, GameMap.MAP_OFFSET_Y - 1, ColorUtils.UI_OUTLINE_COLOR);
            panel.write((char)196, i + GameMap.MAP_OFFSET_X, gameMap.height + GameMap.MAP_OFFSET_Y, ColorUtils.UI_OUTLINE_COLOR);
        }
        for (int i = 0; i < gameMap.height; i++) {
            panel.write((char)179, GameMap.MAP_OFFSET_X - 1, i + GameMap.MAP_OFFSET_Y, ColorUtils.UI_OUTLINE_COLOR);
            panel.write((char)179, gameMap.width + GameMap.MAP_OFFSET_X, i + GameMap.MAP_OFFSET_Y, ColorUtils.UI_OUTLINE_COLOR);
        }
    }

    public static void renderBar(AsciiPanel panel, int currentValue, int maxValue, int totalWidth) {

        int barWidth = (int)((float)currentValue / maxValue * totalWidth);

        var str = ("HP:" + currentValue + "/" + maxValue).toCharArray();

        for (int i = 0; i < totalWidth; i++) {
            if (i >= 1 && (i - 1) < str.length) {
                panel.write(str[i -1], i + 1, 1, ColorUtils.BAR_TEXT, i < barWidth ? ColorUtils.BAR_FILLED : ColorUtils.BAR_EMPTY);
            } else {
                panel.write(' ', i + 1, 1, ColorUtils.BAR_TEXT, i < barWidth ? ColorUtils.BAR_FILLED : ColorUtils.BAR_EMPTY);
            }
        }
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
