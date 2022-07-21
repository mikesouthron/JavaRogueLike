package org.southy.rl.map;

import java.io.Serializable;

public class FastMoveState implements Serializable {

    public Integer currentAvailableMoves = 0;

    public Integer dx = null;
    public Integer dy = null;

    public Integer getCurrentAvailableMoves() {
        return currentAvailableMoves;
    }

    public Integer getDx() {
        return dx;
    }

    public Integer getDy() {
        return dy;
    }
}
