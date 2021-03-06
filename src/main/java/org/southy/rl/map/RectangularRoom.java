package org.southy.rl.map;

import org.southy.rl.gen.Procgen;

import java.util.ArrayList;
import java.util.List;

public class RectangularRoom {

    public int x1;
    public int x2;
    public int y1;
    public int y2;

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

    public boolean intersects(RectangularRoom other) {
        return x1 <= other.x2 &&
                x2 >= other.x1 &&
                y1 <= other.y2 &&
                y2 >= other.y1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
}
