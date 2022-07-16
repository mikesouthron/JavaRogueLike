package org.southy.rl;

import org.southy.rl.gen.Procgen;

import java.awt.event.KeyEvent;
import java.util.Optional;

public class EventHandler {

    Engine engine;
    Logger logger;

    public EventHandler(Engine engine, Logger logger) {
        this.engine = engine;
        this.logger = logger;
    }

    public void handleEvents(KeyEvent event) {
        var option = keyDown(event);
        option.ifPresent(BaseAction::perform);
        engine.handleEnemyTurns();
        engine.updateFov();
    }

    public Optional<BaseAction> keyDown(KeyEvent event) {
        if (event == null) {
            return Optional.empty();
        }

        var player = engine.player;

        switch (event.getKeyCode()) {
            case 37:
                return Optional.of(new BaseAction.BumpAction(player, -1, 0, event.isShiftDown()));
            case 39:
                return Optional.of(new BaseAction.BumpAction(player, 1, 0, event.isShiftDown()));
            case 38:
                return Optional.of(new BaseAction.BumpAction(player, 0, -1, event.isShiftDown()));
            case 40:
                return Optional.of(new BaseAction.BumpAction(player, 0, 1, event.isShiftDown()));
                //TODO: 8-WAY
//            case 99:
//                return Optional.of(new Action.BumpAction(player, 1, 1, event.isShiftDown()));
//            case 97:
//                return Optional.of(new Action.BumpAction(player, -1, 1, event.isShiftDown()));
//            case 103:
//                return Optional.of(new Action.BumpAction(player, -1, -1, event.isShiftDown()));
//            case 105:
//                return Optional.of(new Action.BumpAction(player, 1, -1, event.isShiftDown()));
            case 27:
                return Optional.of(new BaseAction.EscapeAction(player));
            case 82:
                if (event.isControlDown()) {
                    player.gameMap = engine.gameMap = Procgen.generateDungeon(engine, Application.maxRooms, Application.roomMinSize, Application.roomMaxSize, Application.mapWidth, Application.mapHeight, Application.maxMonstersPerRoom);
                }
                return Optional.empty();
            default: {
                logger.log("Key Pressed: " + event.getKeyCode());
                return Optional.empty();
            }
        }
    }

}
