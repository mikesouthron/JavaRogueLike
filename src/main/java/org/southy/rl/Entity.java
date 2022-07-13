package org.southy.rl;

import java.awt.*;

public class Entity {

    public int x;
    public int y;
    final char str;
    final Color fg;
    final Color bg;

    public Entity(int x, int y, char str, Color fg) {
        this(x, y, str, fg, ColorUtils.FLOOR_COLOR_LIGHT);
    }

    public Entity(int x, int y, char str, Color fg, Color bg) {
        this.x = x;
        this.y = y;
        this.str = str;
        this.fg = fg;
        this.bg = bg;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

}
