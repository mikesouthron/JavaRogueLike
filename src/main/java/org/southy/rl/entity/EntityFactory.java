package org.southy.rl.entity;

import org.southy.rl.ColorUtils;
import org.southy.rl.components.Fighter;
import org.southy.rl.components.HostileEnemy;
import org.southy.rl.map.GameMap;

import java.awt.*;

public class EntityFactory {

    public static Actor player() {
        return new Actor(null, '@', Color.WHITE, "Player", new Fighter(30, 2, 5), HostileEnemy.class);
    }

    public static void orc(GameMap gameMap, int x, int y) {
        new Actor(gameMap, x, y, 'o', ColorUtils.color(127, 127, 127), "Orc", new Fighter(10, 0, 3), HostileEnemy.class );
    }

    public static void troll(GameMap gameMap, int x, int y) {
        new Actor(gameMap, x, y, 'T', ColorUtils.color(0, 127, 0), "Troll", new Fighter(16, 1, 4), HostileEnemy.class);
    }

}
