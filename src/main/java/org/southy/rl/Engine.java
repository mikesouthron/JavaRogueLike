package org.southy.rl;

import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.components.BaseAI;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.FastMoveState;
import org.southy.rl.map.GameMap;

import java.util.List;

public class Engine {
    public Entity player;

    Logger logger;

    public GameMap gameMap;

    public EventHandler eventHandler;

    FastMoveState fastMove = null;

    public Engine(Entity player, Logger logger) {
        this.player = player;
        this.logger = logger;
        eventHandler = new EventHandler(this, logger);
    }

    public void handleEnemyTurns() {
        System.out.println(gameMap.entities.size());
        for (Entity entity : gameMap.entities) {
            if (entity != player) {
                BaseAI baseAI = new BaseAI();
                baseAI.setEntity(entity);

                List<Integer> path = baseAI.getPathTo(player.x, player.y);
                if (path.size() > 1) {
                    int i = path.get(1);
                    int x = i % gameMap.width;
                    int y = i / gameMap.width;

                    new BaseAction.MovementAction(entity, x - entity.x, y - entity.y, false).perform();
                }
            }
        }
    }

    public void updateFov() {
        gameMap.computeFov(player.x, player.y);
        for (int i = 0; i < gameMap.visible.length; i++) {
            if (gameMap.visible[i] != null) {
                gameMap.explored[i] = gameMap.visible[i];
            }
        }
    }

    public void render(AsciiPanel panel) {
        panel.clear();
        gameMap.render(panel);
        panel.repaint();
    }
}
