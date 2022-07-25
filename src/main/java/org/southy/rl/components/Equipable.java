package org.southy.rl.components;

import org.southy.rl.Color;
import org.southy.rl.entity.Entity;
import org.southy.rl.entity.EntityParent;
import org.southy.rl.entity.RenderOrder;
import org.southy.rl.map.GameMap;

import java.io.Serializable;

public class Equipable extends Entity implements Serializable {
    public BodyPart bodyPart;

    public Range atk;

    public double strMod;

    public Range def;

    public double armourMod;

    public int hpRestore;
    public int inCombatHpRestore;

    public Equipable(GameMap gameMap, int x, int y, char str, Color color, String name, BodyPart bodyPart, int atkLow, int atkHigh, double strMod, int defLow, int defHigh, double armourMod, int hpRestore, int inCombatHpRestore) {
        super(gameMap, x, y, str, color, name, false, RenderOrder.ITEM);
        this.bodyPart = bodyPart;
        if (atkLow > 0 && atkHigh > 0) {
            this.atk = new Range(atkLow, atkHigh);
        }
        this.strMod = strMod;

        if (defLow > 0 && defHigh > 0) {
            this.def = new Range(defLow, defHigh);
        }
        this.armourMod = armourMod;
        this.hpRestore = hpRestore;
        this.inCombatHpRestore = inCombatHpRestore;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBodyPart(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    public void setAtk(Range atk) {
        this.atk = atk;
    }

    public void setStrMod(double strMod) {
        this.strMod = strMod;
    }

    public void setDef(Range def) {
        this.def = def;
    }

    public void setArmourMod(double armourMod) {
        this.armourMod = armourMod;
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
    public void setBlocksMovement(boolean blocksMovement) {
        super.setBlocksMovement(blocksMovement);
    }

    @Override
    public void setRenderOrder(RenderOrder renderOrder) {
        super.setRenderOrder(renderOrder);
    }

    public void setHpRestore(int hpRestore) {
        this.hpRestore = hpRestore;
    }

    public void setInCombatHpRestore(int inCombatHpRestore) {
        this.inCombatHpRestore = inCombatHpRestore;
    }
}
