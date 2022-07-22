package org.southy.rl.components;

import org.southy.rl.BaseAction;
import org.southy.rl.ColorUtils;
import org.southy.rl.entity.Item;
import org.southy.rl.exceptions.Impossible;

import java.io.Serializable;

public class HealingConsumable extends Consumable implements Serializable {
    public int amount;

    public HealingConsumable(int amount) {
        this.amount = amount;
    }

    @Override
    public void activate(BaseAction.ItemAction action) throws Impossible {
        var consumer = action.entity;
        var amountRecovered = consumer.fighter.heal(amount);
        if (amountRecovered > 0) {
            consume();
            engine().logger.addMessage("You consume the " + parent.name + " and recover " + amountRecovered + " HP", ColorUtils.HEALTH_RECOVERED);
        } else {
            throw new Impossible("Your health is already full");
        }
    }

    @Override
    public void setParent(Item parent) {
        super.setParent(parent);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
