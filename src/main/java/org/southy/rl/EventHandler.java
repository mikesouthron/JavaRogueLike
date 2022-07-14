package org.southy.rl;

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
        option.ifPresent(Action::perform);
        engine.handleEnemyTurns();
        engine.updateFov();
    }

    public Optional<Action> keyDown(KeyEvent event) {
        if (event == null) {
            return Optional.empty();
        }

        var player = engine.player;

        switch (event.getKeyCode()) {
            case 37:
                return Optional.of(new Action.BumpAction(player, -1, 0, event.isShiftDown()));
            case 39:
                return Optional.of(new Action.BumpAction(player, 1, 0, event.isShiftDown()));
            case 38:
                return Optional.of(new Action.BumpAction(player, 0, -1, event.isShiftDown()));
            case 40:
                return Optional.of(new Action.BumpAction(player, 0, 1, event.isShiftDown()));
//            case 99:
//                return Optional.of(new Action.BumpAction(player, 1, 1, event.isShiftDown()));
//            case 97:
//                return Optional.of(new Action.BumpAction(player, -1, 1, event.isShiftDown()));
//            case 103:
//                return Optional.of(new Action.BumpAction(player, -1, -1, event.isShiftDown()));
//            case 105:
//                return Optional.of(new Action.BumpAction(player, 1, -1, event.isShiftDown()));
            case 27:
                return Optional.of(new Action.EscapeAction(player));
            default: {
                logger.log("Key Pressed: " + event.getKeyCode());
                return Optional.empty();
            }
        }
    }

}
