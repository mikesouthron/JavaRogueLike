package org.southy.rl.map;

import org.southy.rl.Color;
import org.southy.rl.ColorUtils;

import java.io.Serializable;

public class Tile implements Serializable {

    public static class Graphic implements Serializable {
        public char ch;
        public Color fg;
        public Color bg;

        public Graphic(char ch, Color fg, Color bg) {
            this.ch = ch;
            this.fg = fg;
            this.bg = bg;
        }

        public void setCh(char ch) {
            this.ch = ch;
        }

        public void setFg(Color fg) {
            this.fg = fg;
        }

        public void setBg(Color bg) {
            this.bg = bg;
        }
    }

    public boolean walkable;
    public boolean transparent;
    public Graphic dark;
    public Graphic light;

    public boolean fov = false;

    public String name;

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public void setDark(Graphic dark) {
        this.dark = dark;
    }

    public void setLight(Graphic light) {
        this.light = light;
    }

    public void setFov(boolean fov) {
        this.fov = fov;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void setSHROUD(Graphic SHROUD) {
        Tile.SHROUD = SHROUD;
    }

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
                new Graphic('.', ColorUtils.color(100, 100, 100), ColorUtils.BLACK),
                new Graphic('.', ColorUtils.color(200, 200, 200), ColorUtils.BLACK), "Floor");
    }

    public static Tile wallTile() {
        return newTile(false, false,
                new Graphic(' ', ColorUtils.BLACK, ColorUtils.color(100, 100, 100)),
                new Graphic(' ', ColorUtils.BLACK, ColorUtils.color(200, 200, 200)), "Wall");
    }

    public static Tile upStairs() {
        return newTile(true, true,
                new Graphic('<', ColorUtils.color(100, 100, 100), ColorUtils.BLACK),
                new Graphic('<', ColorUtils.color(200, 200, 200), ColorUtils.BLACK), "Up Stairs");
    }

}
