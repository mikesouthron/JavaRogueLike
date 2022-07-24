package org.southy.rl.components;

import org.southy.rl.Action;
import org.southy.rl.BaseAction;
import org.southy.rl.ColorUtils;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Item;
import org.southy.rl.eventhandler.SingleRangedAttackHandler;
import org.southy.rl.exceptions.Impossible;

import java.io.Serializable;
import java.util.Optional;

public class ConfusionConsumable extends Consumable implements Serializable {
    public int numberOfTurns;

    public ConfusionConsumable(int numerOfTurns) {
        this.numberOfTurns = numerOfTurns;
    }

    @Override
    public Optional<Action> getAction(Actor consumer) {
        engine().eventHandler = new SingleRangedAttackHandler(engine(), (x, y) -> new BaseAction.ItemAction(consumer, this.parent, x, y));
        return Optional.empty();
    }

    @Override
    public void activate(BaseAction.ItemAction action) throws Impossible {
        var consumer = action.entity;
        var target = action.getTargetActor();

        if (gamemap().visible[action.targetX + action.targetY * gamemap().width] == null) {
            throw new Impossible("Cannot target an area you cannot see");
        }
        if (target.isEmpty()) {
            throw new Impossible("No target!");
        }
        if (target.get() == consumer) {
            throw new Impossible("You cannot confuse yourself"); //TODO Maybe we can?
        }

        engine().logger.addMessage("The eyes of " + target.get().name + " look vacant, as it starts to stumble around!", ColorUtils.STATUS_EFFECT_APPLIED);

        target.get().ai = new ConfusedEnemy(target.get(), target.get().ai, numberOfTurns);
        consume();
    }

    @Override
    public void setParent(Item parent) {
        super.setParent(parent);
    }

    public void setNumberOfTurns(int numberOfTurns) {
        this.numberOfTurns = numberOfTurns;
    }
}
