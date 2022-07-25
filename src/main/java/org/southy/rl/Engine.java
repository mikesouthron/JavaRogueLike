package org.southy.rl;

import org.southy.rl.entity.Actor;
import org.southy.rl.eventhandler.EventHandler;
import org.southy.rl.eventhandler.MainMenuHandler;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.FastMoveState;
import org.southy.rl.map.GameMap;
import org.southy.rl.ui.Render;
import org.southy.sdl.SDL;

import java.io.Serializable;

public class Engine implements Serializable {
    public Actor player;

    public Logger logger;

    public GameMap gameMap;
    public GameWorld gameWorld;

    public EventHandler eventHandler;

    public FastMoveState fastMove = null;

    public boolean showNames = false;

    public Engine() {
        this.logger = new Logger();
        eventHandler = new MainMenuHandler(this);
    }

    public void handleEnemyTurns() {
        for (Actor actor : gameMap.getActors()) {
            if (actor != player) {
                try {
                    actor.getAi().perform();
                } catch (Impossible ignored) {
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

    public void render(SDL sdl) {
        if (!gameMap.fullMap) {
            Render.renderUIBorders(sdl, gameMap);
        }
        gameMap.render(sdl);
        if (!gameMap.fullMap) {
            logger.render(sdl, 20, 46, 60, 12);
            Render.renderBar(sdl, player.fighter.getHp(), player.fighter.maxHp, 16, gameWorld.currentFloor);
        }
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

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }
}
