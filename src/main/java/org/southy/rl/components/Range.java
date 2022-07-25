package org.southy.rl.components;

import org.southy.rl.RandomUtils;

import java.io.Serializable;

public class Range implements Serializable {

    public int low;
    public int high;

    public Range(int low, int high) {
        this.low = low;
        this.high = high;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int random() {
        return RandomUtils.randomInt(low, high);
    }

}
