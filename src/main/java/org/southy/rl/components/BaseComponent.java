package org.southy.rl.components;

import org.southy.rl.Engine;
import org.southy.rl.entity.Actor;
import org.southy.rl.map.GameMap;

public abstract class BaseComponent {
    Actor parent;

    public Engine engine() {
        return gamemap().engine;
    }

    public void setEntity(Actor entity) {
        this.parent = entity;
    }

    public GameMap gamemap() {
        return parent.gamemap();
    }

}
