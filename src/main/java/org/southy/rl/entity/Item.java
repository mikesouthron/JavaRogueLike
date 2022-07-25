package org.southy.rl.entity;

import org.southy.rl.Color;
import org.southy.rl.components.Consumable;
import org.southy.rl.components.Equipable;
import org.southy.rl.map.GameMap;

import java.io.Serializable;

public class Item extends Entity implements Serializable {

    public Consumable consumable;

    public Equipable equipable;

    public Item(GameMap parent, int x, int y, char str, Color color, String name, Consumable consumable, Equipable equipable) {
        super(parent, x, y, str, color, name, false, RenderOrder.ITEM);
        this.consumable = consumable;
        if (this.consumable != null) {
            consumable.setParent(this);
        }
        this.equipable = equipable;
    }

    @Override
    public void setParent(EntityParent parent) {
        super.setParent(parent);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
    }

    @Override
    public void setStr(char str) {
        super.setStr(str);
    }

    @Override
    public void setFg(Color fg) {
        super.setFg(fg);
    }

    @Override
    public void setBg(Color bg) {
        super.setBg(bg);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void setBlocksMovement(boolean blocksMovement) {
        super.setBlocksMovement(blocksMovement);
    }

    @Override
    public void setRenderOrder(RenderOrder renderOrder) {
        super.setRenderOrder(renderOrder);
    }

    public void setConsumable(Consumable consumable) {
        this.consumable = consumable;
    }

    public void setEquipable(Equipable equipable) {
        this.equipable = equipable;
    }
}
