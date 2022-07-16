package org.southy.rl.entity;

import org.southy.rl.components.BaseAI;
import org.southy.rl.components.Fighter;
import org.southy.rl.map.GameMap;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Actor extends Entity {

    BaseAI ai;
    Fighter fighter;

    public Actor(GameMap gameMap, char str, Color fg, String name, Fighter fighter, Class<? extends BaseAI> aiClass) {
        this(gameMap, 0, 0, str, fg, name, fighter, aiClass);
    }


    public Actor(GameMap gameMap, int x, int y, char str, Color fg, String name, Fighter fighter, Class<? extends BaseAI> aiClass) {
        super(gameMap, x, y, str, fg, name, true);
        try {
            ai = aiClass.getDeclaredConstructor(Entity.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.fighter = fighter;
        this.fighter.setEntity(this);
    }

    public BaseAI getAi() {
        return ai;
    }

    public boolean isAlive() {
        return ai != null;
    }

    public Entity spawn(GameMap map, int x, int y) {
        return new Actor(map, x, y, str, fg, name, fighter, ai.getClass());
    }

    public Entity copy() {
        return new Actor(null, x, y, str, fg, name, fighter, ai.getClass());
    }

}
