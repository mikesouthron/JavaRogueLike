package org.southy.rl.gen;

public class Bresenham {

    int origx;
    int origy;
    int destx;
    int desty;

    int deltax;
    int deltay;

    int stepx;
    int stepy;

    int e;

    public Bresenham(int xFrom, int xTo, int yFrom, int yTo) {
        origx = xFrom;
        origy = yFrom;
        destx = xTo;
        desty = yTo;
        deltax = xTo - xFrom;
        deltay = yTo - yFrom;
        if (deltax > 0) {
            stepx = 1;
        } else if (deltax < 0) {
            stepx = -1;
        } else
            stepx = 0;
        if (deltay > 0) {
            stepy = 1;
        } else if (deltay < 0) {
            stepy = -1;
        } else
            stepy = 0;
        if (stepx * deltax > stepy * deltay) {
            e = stepx * deltax;
            deltax *= 2;
            deltay *= 2;
        } else {
            e = stepy * deltay;
            deltax *= 2;
            deltay *= 2;
        }
    }

    public boolean step(Position position) {
        if (stepx * deltax > stepy * deltay) {
            if (origx == destx)
                return true;
            origx += stepx;
            e -= stepy * deltay;
            if (e < 0) {
                origy += stepy;
                e += stepx * deltax;
            }
        } else {
            if (origy == desty)
                return true;
            origy += stepy;
            e -= stepx * deltax;
            if (e < 0) {
                origx += stepx;
                e += stepy * deltay;
            }
        }

        position.x = origx;
        position.y = origy;

        return false;
    }
}
