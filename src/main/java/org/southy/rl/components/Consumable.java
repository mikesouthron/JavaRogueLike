package org.southy.rl.components;

import org.southy.rl.Action;
import org.southy.rl.BaseAction;
import org.southy.rl.Engine;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Item;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.GameMap;

import java.io.Serializable;
import java.util.Optional;

public class Consumable implements Serializable {
    public Item parent;

    public Consumable() {
    }

    public Optional<Action> getAction(Actor consumer) {
        return Optional.of(new BaseAction.ItemAction(consumer, parent));
    }

    public void activate(BaseAction.ItemAction action) throws Impossible  {
        throw new Impossible("Not implemented");
    }

    public void consume() throws Impossible {
        parent.parent.getInventory().items.remove(parent);
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    public Engine engine() {
        return gamemap().engine;
    }

    public GameMap gamemap() {
        return parent.gamemap();
    }


}
