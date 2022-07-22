package org.southy.rl.components;

import org.southy.rl.BaseAction;
import org.southy.rl.RandomUtils;
import org.southy.rl.entity.Actor;
import org.southy.rl.exceptions.Impossible;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConfusedEnemy extends BaseAI {
    List<Integer> path = new ArrayList<>();

    BaseAI previousAi;

    int turnsRemaining;

    public ConfusedEnemy(Actor parent, BaseAI previousAi, int numberOfTurns) {
        super(parent);
        this.previousAi = previousAi;
        turnsRemaining = numberOfTurns;
    }

    @Override
    public boolean perform() throws Impossible {
        if (turnsRemaining <= 0) {
            parent.gamemap().engine.logger.addMessage(parent.name + " is no longer confused", Color.WHITE);
            parent.ai = previousAi;
            return false;
        } else {
            int dx = RandomUtils.randomInt(-1, 1);
            int dy = RandomUtils.randomInt(-1, 1);
            turnsRemaining--;
            return new BaseAction.BumpAction(parent, dx, dy, false).perform();
        }
    }

    public void setPath(List<Integer> path) {
        this.path = path;
    }

    public void setParent(Actor parent) {
        this.parent = parent;
    }
}
