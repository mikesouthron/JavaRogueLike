package org.southy.rl;

import java.awt.event.KeyEvent;
import java.util.Optional;

public class EventHandler {

    public static Optional<Action> keyDown(KeyEvent event, Logger logger) {
        if (event == null) {
            return Optional.empty();
        }

        switch (event.getKeyCode()) {
            case 100:
                return Optional.of(new Action.MovementAction(-1, 0));
            case 102:
                return Optional.of(new Action.MovementAction(1, 0));
            case 104:
                return Optional.of(new Action.MovementAction(0, -1));
            case 98:
                return Optional.of(new Action.MovementAction(0, 1));
            case 99:
                return Optional.of(new Action.MovementAction(1, 1));
            case 97:
                return Optional.of(new Action.MovementAction(-1, 1));
            case 103:
                return Optional.of(new Action.MovementAction(-1, -1));
            case 105:
                return Optional.of(new Action.MovementAction(1, -1));
            case 27:
                return Optional.of(new Action.EscapeAction());
            default: {
                logger.log("Key Pressed: " + event.getKeyCode());
                return Optional.empty();
            }
        }
    }

}
