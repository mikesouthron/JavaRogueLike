package org.southy.rl.components;

import org.southy.rl.BaseAction;
import org.southy.rl.entity.Actor;
import org.southy.rl.exceptions.Impossible;

import java.util.ArrayList;
import java.util.List;

public class HostileEnemy extends BaseAI {
    List<Integer> path = new ArrayList<>();

    public HostileEnemy(Actor parent) {
        super(parent);
    }

    @Override
    public boolean perform() throws Impossible {
        var engine = parent.gamemap().engine;
        var target = engine.player;

        int dx = target.x - parent.x;
        int dy = target.y - parent.y;

        int distance = Math.max(Math.abs(dx), Math.abs(dy));

        if (engine.gameMap.visible[parent.x + parent.y * engine.gameMap.width] != null) {
            if (distance <= 1) {
                return new BaseAction.MeleeAction(parent, dx, dy, false).perform();
            }

            path = getPathTo(target.x, target.y);
        }

        if (path != null && path.size() > 0) {
            int dest_idx = path.remove(0);
            int x = dest_idx % engine.gameMap.width;
            int y = dest_idx / engine.gameMap.width;
            return new BaseAction.MovementAction(parent, x - parent.x, y - parent.y, false).perform();
        }

        return new BaseAction.WaitAction(parent).perform();
    }

    public void setPath(List<Integer> path) {
        this.path = path;
    }

    public void setParent(Actor parent) {
        this.parent = parent;
    }
}
