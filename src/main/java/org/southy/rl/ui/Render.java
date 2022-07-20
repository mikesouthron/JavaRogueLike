package org.southy.rl.ui;

import org.southy.rl.ColorUtils;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;

import java.awt.*;
import java.util.ArrayList;

public class Render {

    public static void renderBar(AsciiPanel panel, int currentValue, int maxValue, int totalWidth) {

        int barWidth = (int)((float)currentValue / maxValue * totalWidth);

        var str = ("HP:" + currentValue + "/" + maxValue).toCharArray();

        for (int i = 0; i < totalWidth; i++) {
            if (i >= 1 && (i - 1) < str.length) {
                panel.write(str[i -1], i, 45, ColorUtils.BAR_TEXT, i < barWidth ? ColorUtils.BAR_FILLED : ColorUtils.BAR_EMPTY);
            } else {
                panel.write(' ', i, 45, ColorUtils.BAR_TEXT, i < barWidth ? ColorUtils.BAR_FILLED : ColorUtils.BAR_EMPTY);
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
