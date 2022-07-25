package org.southy.rl.components;

import org.southy.rl.Color;
import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.entity.Actor;
import org.southy.rl.eventhandler.GameOverEventHandler;
import org.southy.rl.map.GameMap;

import java.io.Serializable;

public class Fighter implements Serializable {


    public int strength;
    public int agility;
    public int constitution;
    public int intelligence;

    public int hpMod = 10;

    int hp;

    Actor parent;

    public Engine engine() {
        return gamemap().engine;
    }

    public GameMap gamemap() {
        return parent.gamemap();
    }


    public Fighter(int strength, int agility, int constitution, int intelligence) {
        this.hp = constitution * hpMod;
        this.strength = strength;
        this.agility = agility;
        this.constitution = constitution;
        this.intelligence = intelligence;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, getMaxHp()));
        if (this.hp == 0 && parent.isAlive()) {
            die();
        }
    }

    public int getMaxHp() {
        return constitution * hpMod;
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

        engine().gameMap.entities.remove(parent);

        engine().logger.addMessage(deathMessage, deathMessageColor);
    }

    public int heal(int amount) {
        if (hp == getMaxHp()) {
            return 0;
        }

        var newHpValue = hp + amount;
        if (newHpValue > getMaxHp()) {
            newHpValue = getMaxHp();
        }

        var amountRecovered = newHpValue - hp;

        hp = newHpValue;

        return amountRecovered;
    }

    public void takeDamage(int amount) {
        setHp(hp - amount);
    }

    public void levelUp(int strength, int agility, int constitution, int intelligence) {
        this.strength += strength;
        this.agility += agility;
        this.constitution += constitution;
        this.intelligence += intelligence;
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

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setHpMod(int hpMod) {
        this.hpMod = hpMod;
    }
}
