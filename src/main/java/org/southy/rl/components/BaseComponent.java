package org.southy.rl.components;

import org.southy.rl.Engine;
import org.southy.rl.entity.Actor;

public abstract class BaseComponent {
    Actor entity;

    public Engine engine() {
        return entity.gameMap.engine;
    }

    public void setEntity(Actor entity) {
        this.entity = entity;
    }

}
