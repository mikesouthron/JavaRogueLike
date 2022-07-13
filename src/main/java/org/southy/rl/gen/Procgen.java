package org.southy.rl.gen;

import java.util.ArrayList;
import java.util.List;

public class Procgen {

    public static List<Integer> tunnel(int start, int end, int mapWidth) {
        int x1 = start % mapWidth;
        int y1 = start / mapWidth;

        int x2 = end % mapWidth;
        int y2 = end / mapWidth;

        int cornerX = x2;
        int cornerY = y1;

        /*
        if (new Random().nextDouble() < 0.5) {
            cornerX = x2;
            cornerY = y1;
        } else {
            cornerX = x1;
            cornerY = y2;
        }*/

        var line = new ArrayList<Integer>();

        var b = new Bresenham(x1, cornerX, y1, cornerY);
        var p = new Position();
        while (!b.step(p)) {
            line.add(toIdx(p.x, p.y, mapWidth));
        }
        b = new Bresenham(cornerX, x2, cornerY, y2);
        while (!b.step(p)) {
            line.add(toIdx(p.x, p.y, mapWidth));
        }

        return line;
    }

    public static List<Integer> plotPixel(int x1, int y1, int x2, int y2, int dx, int dy, int decide, int mapWidth)
    {
        List<Integer> plot = new ArrayList<>();
        //pk is initial decision making parameter
        //Note:x1&y1,x2&y2, dx&dy values are interchanged
        //and passed in plotPixel function so
        //it can handle both cases when m>1 & m<1
        int pk = 2 * dy - dx;
        for (int i = 0; i <= dx; i++)
        {
            plot.add(toIdx(x1, y1, mapWidth));
            //checking either to decrement or increment the value
            //if we have to plot from (0,100) to (100,0)
            if(x1 < x2)
                x1++;
            else
                x1--;
            if (pk < 0)
            {
                //decision value will decide to plot
                //either  x1 or y1 in x's position
                if (decide == 0)
                {
                    pk = pk + 2 * dy;
                }
                else
                    pk = pk + 2 * dy;
            }
            else
            {
                if(y1 < y2)
                    y1++;
                else
                    y1--;
                pk = pk + 2 * dy - 2 * dx;
            }
        }

        return plot;
    }

    public static List<Integer> drawLine(int x1, int y1, int x2, int y2, int mapWidth) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        //If slope is less than one
        if (dx > dy)
        {
            //passing argument as 0 to plot(x,y)
            return plotPixel(x1, y1, x2, y2, dx, dy, 0, mapWidth);
        }
        //if slope is greater than or equal to 1
        else
        {
            //passing argument as 1 to plot (y,x)
            return plotPixel(y1, x1, y2, x2, dy, dx, 1, mapWidth);
        }
    }

    public static int toIdx(int x, int y, int mapWidth) {
        return (x + y * mapWidth);
    }

}