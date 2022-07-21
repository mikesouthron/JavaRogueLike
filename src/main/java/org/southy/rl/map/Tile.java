package org.southy.rl.map;

import org.southy.rl.ColorUtils;

import java.awt.*;

public class Tile {

    public static class Graphic {
        public char ch;
        public Color fg;
        public Color bg;

        public Graphic(char ch, Color fg, Color bg) {
            this.ch = ch;
            this.fg = fg;
            this.bg = bg;
        }
    }

    public boolean walkable;
    public boolean transparent;
    public Graphic dark;
    public Graphic light;

    public boolean fov = false;

    public String name;

    public static Tile newTile(boolean walkable, boolean transparent, Graphic dark, Graphic light, String name) {
        var t = new Tile();
        t.walkable = walkable;
        t.transparent = transparent;
        t.dark = dark;
        t.light = light;
        t.name = name;
        return t;
    }

    public static Graphic SHROUD = new Graphic(' ', ColorUtils.color(255, 255, 255), ColorUtils.color(0,0,0));

    public static Tile floorTile() {
        return newTile(true, true,
                new Graphic('.', ColorUtils.color(100, 100, 100), Color.BLACK),
                new Graphic('.', ColorUtils.color(200, 200, 200), Color.BLACK), "Floor");
    }

    public static Tile wallTile() {
        return newTile(false, false,
                new Graphic(' ', Color.BLACK, ColorUtils.color(100, 100, 100)),
                new Graphic(' ', Color.BLACK, ColorUtils.color(200, 200, 200)), "Wall");
    }

}
