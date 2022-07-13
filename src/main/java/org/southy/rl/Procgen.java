package org.southy.rl;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Procgen {

    public static List<Integer> tunnel(int start, int end, int mapWidth) {
        int x1 = start % mapWidth;
        int y1 = start / mapWidth;

        int x2 = end % mapWidth;
        int y2 = end / mapWidth;

        int cornerX;
        int cornerY;

        if (new Random().nextDouble() < 0.5) {
            cornerX = x2;
            cornerY = y1;
        } else {
            cornerX = x1;
            cornerY = y2;
        }

        var line = drawLine(x1, y1, cornerX, cornerY, mapWidth);
        line.addAll(drawLine(cornerX, cornerY, x2, y2, mapWidth));

        return line;
    }

    public static List<Integer> drawLine(int x1, int y1, int x2, int y2, int mapWidth) {

        List<Integer> line = new ArrayList<>();

        int dx, dy, p, x, y;
        dx = x2 - x1;
        dy = y2 - y1;
        x = x1;
        y = y1;
        p = 2 * dy - dx;
        while (x < x2) {
            if (p >= 0) {
                line.add(toIdx(x, y, mapWidth));
                y = y + 1;
                p = p + 2 * dy - 2 * dx;
            } else {
                line.add(toIdx(x, y, mapWidth));
                p = p + 2 * dy;
            }
            x = x + 1;
        }
        return line;
    }

    public static int toIdx(int x, int y, int mapWidth) {
        return (x + y * mapWidth);
    }

}
