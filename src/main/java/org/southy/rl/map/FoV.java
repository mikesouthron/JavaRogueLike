package org.southy.rl.map;

import java.util.Arrays;

public class FoV {

    enum FoVAlgorithm {
        FOV_RESTRICTIVE;
    }

    private static void clearFov(GameMap map) {
        Arrays.fill(map.visible, null);
        for (Tile tile : map.tiles) {
            tile.fov = false;
        }
    }

    public static boolean computeFov(GameMap map, int povx, int povy, int maxRadius, boolean lightWalls, FoVAlgorithm algo) {
        clearFov(map);
        switch (algo) {
            case FOV_RESTRICTIVE:
                return FoVRestrictiveShadowCasting.compute(map, povx, povy, maxRadius, lightWalls);
            default:
                return false;
        }
    }

}
