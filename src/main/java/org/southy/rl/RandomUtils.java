package org.southy.rl;

import java.util.Random;

public class RandomUtils {

    private final static Random random = new Random();

    public static int randomInt(int lower, int upper) {
        return random.nextInt((upper + 1) - lower) + lower;
    }

}
