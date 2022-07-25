package org.southy.rl.entity;

import org.southy.rl.ColorUtils;
import org.southy.rl.components.*;
import org.southy.rl.map.GameMap;

public class EntityFactory {

    public static Actor player() {
        return new Actor(null, '@', ColorUtils.WHITE, "Player", new Fighter(1, 1, 1, 1), HostileEnemy.class, new Inventory(20));
    }

    public static void orc(GameMap gameMap, int x, int y) {
        new Actor(gameMap, x, y, 'o', ColorUtils.color(127, 127, 127), "Orc", new Fighter(0, 1, 1, 1), HostileEnemy.class, new Inventory(0));
    }

    public static void troll(GameMap gameMap, int x, int y) {
        new Actor(gameMap, x, y, 'T', ColorUtils.color(0, 127, 0), "Troll", new Fighter(0, 1, 2, 1), HostileEnemy.class, new Inventory(0));
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

