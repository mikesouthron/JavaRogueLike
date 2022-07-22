package org.southy.rl.entity;

import org.southy.rl.ColorUtils;
import org.southy.rl.components.AreaDamageConsumable;
import org.southy.rl.components.ConfusionConsumable;
import org.southy.rl.components.Fighter;
import org.southy.rl.components.HealingConsumable;
import org.southy.rl.components.HostileEnemy;
import org.southy.rl.components.Inventory;
import org.southy.rl.components.RandomTargetDamageConsumable;
import org.southy.rl.map.GameMap;

import java.awt.*;

public class EntityFactory {

    public static Actor player() {
        return new Actor(null, '@', Color.WHITE, "Player", new Fighter(30, 2, 5), HostileEnemy.class, new Inventory(20));
    }

    public static void orc(GameMap gameMap, int x, int y) {
        new Actor(gameMap, x, y, 'o', ColorUtils.color(127, 127, 127), "Orc", new Fighter(10, 0, 3), HostileEnemy.class, new Inventory(0));
    }

    public static void troll(GameMap gameMap, int x, int y) {
        new Actor(gameMap, x, y, 'T', ColorUtils.color(0, 127, 0), "Troll", new Fighter(16, 1, 4), HostileEnemy.class, new Inventory(0));
    }

    public static void potion(GameMap gameMap, int x, int y) {
        new Item(gameMap, x, y, '!', ColorUtils.color(127, 0, 255), "Health Potion", new HealingConsumable(4));
    }

    public static void litScroll(GameMap gameMap, int x, int y) {
        new Item(gameMap, x, y, '~', ColorUtils.color(255, 255, 0), "Lit Scroll", new RandomTargetDamageConsumable(20, 5));
    }

    public static void confusionScroll(GameMap gameMap, int x, int y) {
        new Item(gameMap, x, y, '~', ColorUtils.color(255, 255, 0), "Confusion Scroll", new ConfusionConsumable(10));
    }

    public static void fireballScroll(GameMap gameMap, int x, int y) {
        new Item(gameMap, x, y, '~', ColorUtils.color(255, 255, 0), "Fireball Scroll", new AreaDamageConsumable(12, 3));
    }
}

