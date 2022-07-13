package org.southy.rl;

import java.util.ArrayList;
import java.util.List;

public class RectangularRoom {

    int x1;
    int x2;
    int y1;
    int y2;

    public RectangularRoom(int x, int y, int width, int height) {
        x1 = x;
        y1 = y;
        x2 = x + width;
        y2 = y + height;
    }

    public int centre(int mapWidth) {
        int x = (x1 + x2) / 2;
        int y = (y1 + y2) / 2;
        return (x + y * mapWidth);
    }

    public List<Integer> inner(int mapWidth) {
        List<Integer> inner = new ArrayList<>();
        for (int x = x1 + 1; x < x2; x++) {
            for (int y = y1 + 1; y < y2; y++) {
                inner.add(Procgen.toIdx(x, y, mapWidth));
            }
        }
        return inner;
    }
}
