package org.southy.rl.entity;

import org.southy.rl.Color;
import org.southy.rl.components.BaseAI;
import org.southy.rl.components.Equipment;
import org.southy.rl.components.Fighter;
import org.southy.rl.components.Inventory;
import org.southy.rl.map.GameMap;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class Actor extends Entity implements Serializable, EntityParent {

    public BaseAI ai;
    public Fighter fighter;

    public Inventory inventory;

    public Equipment equipment;

    public int fovRadius = 8;

    public int inCombat = 0;

    public int baseHpRestore;

    public Actor(GameMap parent, char str, Color fg, String name, Fighter fighter, Class<? extends BaseAI> aiClass, Inventory inventory, int baseHpRestore) {
        this(parent, 0, 0, str, fg, name, fighter, aiClass, inventory, baseHpRestore);
    }


    public Actor(GameMap parent, int x, int y, char str, Color fg, String name, Fighter fighter, Class<? extends BaseAI> aiClass, Inventory inventory, int baseHpRestore) {
        super(parent, x, y, str, fg, name, true, RenderOrder.ACTOR);
        try {
            ai = aiClass.getDeclaredConstructor(Actor.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.fighter = fighter;
        this.fighter.setEntity(this);
        this.inventory = inventory;
        this.inventory.setParent(this);
        this.equipment = new Equipment(this);
        this.baseHpRestore = baseHpRestore;
    }

    public void endOfTurn() {
        //TODO: Tweak these values with real world testing.
        if (inCombat == 0) {
            this.fighter.heal(baseHpRestore + equipment.calculateHpRestore());
        } else {
            this.fighter.heal(equipment.calculateInCombatHpRestore());
        }
        if (inCombat > 0) {
            inCombat--;
        }
    }

    public BaseAI getAi() {
        return ai;
    }

    public boolean isAlive() {
        return ai != null;
    }

    public void setAi(BaseAI ai) {
        this.ai = ai;
    }

    public void setFighter(Fighter fighter) {
        this.fighter = fighter;
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

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setFovRadius(int fovRadius) {
        this.fovRadius = fovRadius;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public void setInCombat(int inCombat) {
        this.inCombat = inCombat;
    }

    public void setBaseHpRestore(int baseHpRestore) {
        this.baseHpRestore = baseHpRestore;
    }
}
