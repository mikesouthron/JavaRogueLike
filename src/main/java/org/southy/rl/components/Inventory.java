package org.southy.rl.components;

import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Item;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.GameMap;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Serializable {

    Actor parent;

    public int capacity;

    public List<Item> items;

    public Inventory(int capacity) {
        this.capacity = capacity;
        items = new ArrayList<>();
    }

    public void drop(Item item) throws Impossible {
        items.remove(item);
        item.setParent(null);
        item.place(parent.x, parent.y, gamemap());
        engine().logger.addMessage("You dropped the " + item.name, Color.WHITE);
    }

    public void setParent(Actor parent) {
        this.parent = parent;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Engine engine() {
        return gamemap().engine;
    }

    public GameMap gamemap() {
        return parent.gamemap();
    }
}
