package org.southy.rl.entity;

import org.southy.rl.ColorUtils;
import org.southy.rl.map.GameMap;

import java.awt.*;

public class Entity {

    public int x;
    public int y;
    public char str;
    public Color fg;
    public Color bg;

    public String name;

    public boolean blocksMovement;

    public Entity(int x, int y, char str, Color fg, String name, boolean blocksMovement) {
        this(x, y, str, fg, ColorUtils.FLOOR_COLOR_LIGHT, name, blocksMovement);
    }

    public Entity(char str, Color fg, String name, boolean blocksMovement) {
        this(str, fg, ColorUtils.FLOOR_COLOR_LIGHT, name, blocksMovement);
    }

    public Entity(char str, Color fg, Color bg, String name, boolean blocksMovement) {
        this(0, 0, str, fg, bg, name, blocksMovement);
    }

    public Entity(int x, int y, char str, Color fg, Color bg, String name, boolean blocksMovement) {
        this.x = x;
        this.y = y;
        this.str = str;
        this.fg = fg;
        this.bg = bg;
        this.name = name;
        this.blocksMovement = blocksMovement;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public Entity spawn(GameMap map, int x, int y) {
        var copy = new Entity(x, y, str, fg, bg, name, blocksMovement);
        map.entities.add(copy);
        return copy;
    }

    public Entity copy() {
        return new Entity(x, y, str, fg, bg, name, blocksMovement);
    }

}
