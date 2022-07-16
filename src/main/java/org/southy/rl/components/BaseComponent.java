package org.southy.rl.components;

import org.southy.rl.Engine;
import org.southy.rl.entity.Entity;

public abstract class BaseComponent {
    Entity entity;

    public Engine engine() {
        return entity.gameMap.engine;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}
