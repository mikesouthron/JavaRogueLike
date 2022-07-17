package org.southy.rl;

import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Actor;
import org.southy.rl.eventhandler.EventHandler;
import org.southy.rl.eventhandler.MainGameEventHandler;
import org.southy.rl.map.FastMoveState;
import org.southy.rl.map.GameMap;

public class Engine {
    public Actor player;

    Logger logger;

    public GameMap gameMap;

    public EventHandler eventHandler;

    FastMoveState fastMove = null;

    public Engine(Actor player, Logger logger) {
        this.player = player;
        this.logger = logger;
        eventHandler = new MainGameEventHandler(this, logger);
    }

    public void handleEnemyTurns() {
        for (Actor actor : gameMap.getActors()) {
            if (actor != player) {
                actor.getAi().perform();
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
        panel.write("HP: " + player.fighter.getHp() + "/" + player.fighter.maxHp, 1, 47);
        panel.repaint();
    }
}
