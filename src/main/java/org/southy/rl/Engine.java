package org.southy.rl;

import org.southy.rl.components.EquipSlot;
import org.southy.rl.entity.Actor;
import org.southy.rl.eventhandler.EventHandler;
import org.southy.rl.eventhandler.MainMenuHandler;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.FastMoveState;
import org.southy.rl.map.GameMap;
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

    public void endOfTurn() {
        gameMap.getActors().forEach(Actor::endOfTurn);
    }

    private void renderUI(SDL sdl) {
        int x = 0;
        sdl.write("Str " + player.fighter.strength, x,  1);
        sdl.write("Agi " + player.fighter.agility, x, 2);
        sdl.write("Con " + player.fighter.constitution, x, 3);
        sdl.write("Int " + player.fighter.intelligence, x, 4);
        sdl.write("HP " + player.fighter.getHp() + " / " + player.fighter.getMaxHp(), x, 6);

        if (player.inCombat > 0) {
            sdl.write("In Combat [" + "*".repeat(player.inCombat) + "]", x, 7, sdl.getDefaultForegroundColor(), ColorUtils.color(200, 0, 0));
        }

        var atk = player.equipment.getMeleeAtkRange();
        sdl.write("Atk: " + atk.low + "-" + atk.high, x, 8, ColorUtils.color(200, 100, 0));

        var def = player.equipment.getMeleeDefenseRange();
        sdl.write("Def: " + def.low + "-" + def.high, x, 9, ColorUtils.color(100, 200, 0));

        x = sdl.getWidthInCharacters() - Application.rightUIWidth;
        sdl.write("Equipment", x, 1);
        int y = 3;
        sdl.write("Head", x, y);
        if (player.equipment.items[EquipSlot.HEAD.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.HEAD.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Neck", x, y);
        if (player.equipment.items[EquipSlot.NECK.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.NECK.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Body", x, y);
        if (player.equipment.items[EquipSlot.BODY.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.BODY.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Back", x, y);
        if (player.equipment.items[EquipSlot.BACK.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.BACK.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Right Hand", x, y);
        if (player.equipment.items[EquipSlot.RIGHT_HAND.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.RIGHT_HAND.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Left Hand", x, y);
        if (player.equipment.items[EquipSlot.LEFT_HAND.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.LEFT_HAND.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Right Finger", x, y);
        if (player.equipment.items[EquipSlot.RIGHT_FINGER.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.RIGHT_FINGER.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Left Finger", x, y);
        if (player.equipment.items[EquipSlot.LEFT_FINGER.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.LEFT_FINGER.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Feet", x, y);
        if (player.equipment.items[EquipSlot.FEET.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.FEET.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
    }

    public void render(SDL sdl) {
        gameMap.render(sdl);
        renderUI(sdl);
        logger.render(sdl, Application.screenWidth - Application.rightUIWidth, Application.screenHeight - 12, Application.rightUIWidth, 12);
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
