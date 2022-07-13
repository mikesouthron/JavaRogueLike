package org.southy.rl;

import java.awt.*;

public class ColorUtils {

    public final static Color FLOOR_COLOR = color(50, 50,150);

    public static Color color(int red, int green, int blue) {
        var hsb = Color.RGBtoHSB(red, green, blue, null);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

}
