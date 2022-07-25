package org.southy.rl.components;

public enum EquipSlot {

    HEAD(0), NECK(1), BODY(2), BACK(3), RIGHT_HAND(4), LEFT_HAND(5), RIGHT_FINGER(6), LEFT_FINGER(7), FEET(8);

    public int idx;

    EquipSlot(int idx) {
        this.idx = idx;
    }
}
