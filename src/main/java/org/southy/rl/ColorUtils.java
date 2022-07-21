package org.southy.rl;

import java.awt.*;

public class ColorUtils {

    public final static Color FLOOR_COLOR_DARK = color(100, 100,100);
    public final static Color FLOOR_COLOR_LIGHT = color(200, 200,200);
    public final static Color PLAYER_ATK = color(0xE0, 0xE0, 0xE0);
    public final static Color ENEMY_ATK = color(0xFF, 0xC0, 0xC0);
    public final static Color PLAYER_DIE = color(0xFF, 0x30, 0x30);
    public final static Color ENEMY_DIE = color(0xFF, 0xA0, 0x30);
    public final static Color WELCOME_TEXT = color(0x20, 0xA0, 0xFF);
    public final static Color BAR_TEXT = Color.WHITE;
    public final static Color BAR_FILLED = color(0x0, 0x60, 0x0);
    public final static Color BAR_EMPTY = color(0x40, 0x10, 0x10);

    public final static Color UI_OUTLINE_COLOR = color(128, 128, 128);

    public final static Color INVALID = color(0xFF, 0xFF, 0x00);
    public final static Color IMPOSSIBLE = color(0x80, 0x80, 0x80);
    public final static Color ERROR = color(0xFF, 0x40, 0x40);
    public final static Color HEALTH_RECOVERED = color(0x0, 0xFF, 0x0);

    public static Color color(int red, int green, int blue) {
        var hsb = Color.RGBtoHSB(red, green, blue, null);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

}
