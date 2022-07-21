package org.southy.rl.components;

import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.RenderOrder;
import org.southy.rl.eventhandler.GameOverEventHandler;
import org.southy.rl.map.GameMap;

import java.awt.*;
import java.io.Serializable;

public class Fighter implements Serializable {
    public int maxHp;
    int hp;
    public int defence;
    public int power;

    Actor parent;

    public Engine engine() {
        return gamemap().engine;
    }

    public GameMap gamemap() {
        return parent.gamemap();
    }


    public Fighter(int maxHp, int defence, int power) {
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.defence = defence;
        this.power = power;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
        if (this.hp == 0 && parent.isAlive()) {
            die();
        }
    }

    private void die() {
        String deathMessage;
        Color deathMessageColor;
        if (engine().player == this.parent) {
            deathMessage = "You died!";
            deathMessageColor = ColorUtils.PLAYER_DIE;
            engine().eventHandler = new GameOverEventHandler(engine());
        } else {
            deathMessage = parent.name + " is dead";
            deathMessageColor = ColorUtils.ENEMY_DIE;
        }

        parent.str = '%';
        parent.fg = ColorUtils.color(191, 0, 0);
        parent.blocksMovement = false;
        parent.ai = null;
        parent.name = "Remains of " + parent.name;
        parent.renderOrder = RenderOrder.CORPSE;

        engine().logger.addMessage(deathMessage, deathMessageColor);
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setEntity(Actor entity) {
        this.parent = entity;
    }

    public void setParent(Actor entity) {
        setEntity(entity);
    }

    public Actor getParent() {
        return parent;
    }

}
