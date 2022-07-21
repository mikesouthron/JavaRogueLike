package org.southy.rl;

import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Actor;
import org.southy.rl.eventhandler.EventHandler;
import org.southy.rl.eventhandler.MainGameEventHandler;
import org.southy.rl.map.FastMoveState;
import org.southy.rl.map.GameMap;
import org.southy.rl.ui.Render;

import java.io.Serializable;

public class Engine implements Serializable {
    public Actor player;

    public Logger logger;

    public GameMap gameMap;

    public EventHandler eventHandler;

    FastMoveState fastMove = null;

    public Engine(Actor player) {
        this.player = player;
        this.logger = new Logger();
        eventHandler = new MainGameEventHandler(this);
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
        Render.renderUIBorders(panel, gameMap);
        gameMap.render(panel);
        logger.render(panel, 20, 46, 60, 12);
        Render.renderBar(panel, player.fighter.getHp(), player.fighter.maxHp, 16);
        panel.repaint();
    }

    public void setPlayer(Actor player) {
        this.player = player;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void setFastMove(FastMoveState fastMove) {
        this.fastMove = fastMove;
    }
}
