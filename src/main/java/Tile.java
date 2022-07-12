import java.awt.*;

public class Tile {

    static class Graphic {
        public char ch;
        public Color fg;
        public Color bg;
    }

    boolean walkable;
    boolean transparent;
    Graphic dark;

    public static Tile newTile(boolean walkable, boolean transparent, char ch, Color fg, Color bg) {
        var t = new Tile();
        t.walkable = walkable;
        t.transparent = transparent;
        var g = new Graphic();
        g.ch = ch;
        g.fg = fg;
        g.bg = bg;
        t.dark = g;
        return t;
    }

    public static Tile floorTile() {
        return newTile(true, true, ' ', ColorUtils.color(255, 255, 255),
                ColorUtils.FLOOR_COLOR);
    }

    public static Tile wallTile() {
        return newTile(false, false, ' ', ColorUtils.color(255, 255, 255),
                ColorUtils.color(0, 0,100));
    }


}
