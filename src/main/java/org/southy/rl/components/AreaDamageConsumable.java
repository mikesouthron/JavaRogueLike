package org.southy.rl.components;

import org.southy.rl.Action;
import org.southy.rl.BaseAction;
import org.southy.rl.ColorUtils;
import org.southy.rl.entity.Actor;
import org.southy.rl.eventhandler.AreaRangedAttackHandler;
import org.southy.rl.eventhandler.SingleRangedAttackHandler;
import org.southy.rl.exceptions.Impossible;

import java.awt.*;
import java.io.Serializable;
import java.util.Optional;

public class AreaDamageConsumable extends Consumable implements Serializable {

    int damage;
    int radius;

    public AreaDamageConsumable(int damage, int radius) {
        this.damage = damage;
        this.radius = radius;
    }

    @Override
    public Optional<Action> getAction(Actor consumer) {
        engine().eventHandler = new AreaRangedAttackHandler(engine(), radius, (x, y) -> new BaseAction.ItemAction(consumer, this.parent, x, y));
        return Optional.empty();
    }

    @Override
    public void activate(BaseAction.ItemAction action) throws Impossible {
        int targetIdx = action.targetX + action.targetY * gamemap().width;

        if (engine().player.distance(action.targetX, action.targetY) > engine().player.fovRadius) {
            throw new Impossible("Cannot target an area you cannot see");
        }

        var targetHits = false;

        for (Actor actor : gamemap().getActors()) {
            if (actor.distance(action.targetX, action.targetY) <= radius) {
                engine().logger.addMessage("The " + actor.name + " is engulfed in the attack, taking " + damage + " damage!", ColorUtils.PLAYER_ATK);
                actor.fighter.takeDamage(damage);
                targetHits = true;
            }
        }

        if (!targetHits) {
            engine().logger.addMessage("No targets in the radius", ColorUtils.PLAYER_ATK);
        }

        consume();
    }
}
