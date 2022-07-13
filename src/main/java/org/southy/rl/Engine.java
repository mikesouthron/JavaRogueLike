package org.southy.rl;

import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;

import java.awt.event.KeyEvent;

public class Engine {
    Entity player;

    Logger logger;

    GameMap gameMap;

    public Engine(Entity player, GameMap gameMap, Logger logger) {
        this.player = player;
        this.logger = logger;
        this.gameMap = gameMap;
        updateFov();
    }

    private void handleEnemyTurns() {
        for (Entity entity : gameMap.entities) {
            if (entity != player) {
                logger.log("The " + entity.name + " wonders when it will get to take a real turn");
            }
        }
    }

    public void handleEvent(KeyEvent event) {
        var option = EventHandler.keyDown(event, logger);
        option.ifPresent(action -> action.perform(this, player));
        handleEnemyTurns();
        updateFov();
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
