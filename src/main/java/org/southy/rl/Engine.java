package org.southy.rl;

import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;

public class Engine {
    public Entity player;

    Logger logger;

    public GameMap gameMap;

    public EventHandler eventHandler;

    public Engine(Entity player, Logger logger) {
        this.player = player;
        this.logger = logger;
        eventHandler = new EventHandler(this, logger);
    }

    public void handleEnemyTurns() {
        for (Entity entity : gameMap.entities) {
            if (entity != player) {
                logger.log("The " + entity.name + " wonders when it will get to take a real turn");
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
