package org.southy.rl.components;

import org.southy.rl.ColorUtils;
import org.southy.rl.entity.RenderOrder;
import org.southy.rl.eventhandler.GameOverEventHandler;

import java.awt.*;

public class Fighter extends BaseComponent {
    public int maxHp;
    int hp;
    public int defence;
    public int power;

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
        if (this.hp == 0 && entity.isAlive()) {
            die();
        }
    }

    private void die() {
        String deathMessage;
        Color deathMessageColor;
        if (engine().player == this.entity) {
            deathMessage = "You died!";
            deathMessageColor = ColorUtils.PLAYER_DIE;
            engine().eventHandler = new GameOverEventHandler(engine());
        } else {
            deathMessage = entity.name + " is dead";
            deathMessageColor = ColorUtils.ENEMY_DIE;
        }

        entity.str = '%';
        entity.fg = ColorUtils.color(191, 0, 0);
        entity.blocksMovement = false;
        entity.ai = null;
        entity.name = "remains of " + entity.name;
        entity.renderOrder = RenderOrder.CORPSE;

        engine().logger.addMessage(deathMessage, deathMessageColor);
    }

}
