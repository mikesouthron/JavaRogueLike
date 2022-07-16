package org.southy.rl.components;

import org.southy.rl.BaseAction;
import org.southy.rl.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class HostileEnemy extends BaseAI {

    List<Integer> path = new ArrayList<>();

    public HostileEnemy(Entity entity) {
        super(entity);
    }

    @Override
    public void perform() {
        Entity target = engine().player;

        int dx = target.x - entity.x;
        int dy = target.y - entity.y;

        int distance = Math.max(Math.abs(dx), Math.abs(dy));

        if (engine().gameMap.visible[entity.x + entity.y * engine().gameMap.width] != null) {
            if (distance <= 1) {
                new BaseAction.MeleeAction(entity, dx, dy, false).perform();
                return;
            }

            path = getPathTo(target.x, target.y);
        }

        if (path != null && path.size() > 0) {
            int dest_idx = path.remove(0);
            int x = dest_idx % engine().gameMap.width;
            int y = dest_idx / engine().gameMap.width;
            new BaseAction.MovementAction(entity, x - entity.x, y - entity.y, false).perform();
            return;
        }

        new BaseAction.WaitAction(entity).perform();
    }
}
