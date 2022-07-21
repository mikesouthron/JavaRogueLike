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

}
