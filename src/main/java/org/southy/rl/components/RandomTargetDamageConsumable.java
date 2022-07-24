package org.southy.rl.components;

import org.southy.rl.BaseAction;
import org.southy.rl.ColorUtils;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Item;
import org.southy.rl.exceptions.Impossible;

import java.io.Serializable;

public class RandomTargetDamageConsumable extends Consumable implements Serializable {
    public int damage;
    public int maxRange;

    public RandomTargetDamageConsumable(int damage, int maxRange) {
        this.damage = damage;
        this.maxRange = maxRange;
    }

    @Override
    public void activate(BaseAction.ItemAction action) throws Impossible {
        var consumer = action.entity;
        Actor target = null;
        var closest = maxRange + 1.0;

        for (Actor actor : gamemap().getActors()) {
            if (actor != consumer && gamemap().visible[actor.x + actor.y * gamemap().width] != null) {
                var distance = consumer.distance(actor.x, actor.y);
                if (distance < closest) {
                    target = actor;
                    closest = distance;
                }
            }
        }

        if (target != null) {
            target.fighter.takeDamage(damage);
            engine().logger.addMessage("A lighting bolt strikes the " + target.name + ", for " + damage + " damage!", ColorUtils.WHITE);
            consume();
        } else {
            throw new Impossible("No target in range");
        }
    }

    @Override
    public void setParent(Item parent) {
        super.setParent(parent);
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }
}
