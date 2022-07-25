package org.southy.rl.entity;

import org.southy.rl.ColorUtils;
import org.southy.rl.components.*;
import org.southy.rl.map.GameMap;

public class EntityFactory {

    public static Actor player() {
        var player = new Actor(null, '@', ColorUtils.WHITE, "Player", new Fighter(2, 2, 2, 2), HostileEnemy.class, new Inventory(20), 1);
        var dagger = new Equipable(null, 0, 0, '|', ColorUtils.WHITE, "Dagger", BodyPart.HAND, 1, 4, 0, 0, 0, 0, 0, 0);
        player.equipment.items[EquipSlot.RIGHT_HAND.idx] = dagger;
        return player;
    }

    public static void orc(GameMap gameMap, int x, int y) {
        new Actor(gameMap, x, y, 'o', ColorUtils.color(127, 127, 127), "Orc", new Fighter(2, 1, 1, 1), HostileEnemy.class, new Inventory(0), 0);
    }

    public static void troll(GameMap gameMap, int x, int y) {
        new Actor(gameMap, x, y, 'T', ColorUtils.color(0, 127, 0), "Troll", new Fighter(3, 1, 2, 1), HostileEnemy.class, new Inventory(0), 0);
    }

//    public static void potion(GameMap gameMap, int x, int y) {
//        new Item(gameMap, x, y, '!', ColorUtils.color(127, 0, 255), "Health Potion", new HealingConsumable(4), null);
//    }
//
//    public static void litScroll(GameMap gameMap, int x, int y) {
//        new Item(gameMap, x, y, '~', ColorUtils.color(255, 255, 0), "Lit Scroll", new RandomTargetDamageConsumable(20, 5), null);
//    }
//
//    public static void confusionScroll(GameMap gameMap, int x, int y) {
//        new Item(gameMap, x, y, '~', ColorUtils.color(255, 255, 0), "Confusion Scroll", new ConfusionConsumable(10), null);
//    }
//
//    public static void fireballScroll(GameMap gameMap, int x, int y) {
//        new Item(gameMap, x, y, '~', ColorUtils.color(255, 255, 0), "Fireball Scroll", new AreaDamageConsumable(12, 3), null);
//    }

    public static void sword(GameMap gameMap, int x, int y) {
        new Equipable(gameMap, x, y, '|', ColorUtils.WHITE, "Sword", BodyPart.HAND, 2, 8, 1.2, 0, 0, 0, 0, 0);
    }
}

