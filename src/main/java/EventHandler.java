import java.awt.event.KeyEvent;
import java.util.Optional;

public class EventHandler {

    public static Optional<Action> keyDown(KeyEvent event, Logger logger) {
        if (event == null) {
            return Optional.empty();
        }
        logger.log("Key Pressed: " + event.getKeyCode());
        return Optional.ofNullable(
                switch (event.getKeyCode()) {
                    case 100 -> new Action.MovementAction(-1, 0);
                    case 102 -> new Action.MovementAction(1, 0);
                    case 104 -> new Action.MovementAction(0, -1);
                    case 98 -> new Action.MovementAction(0, 1);
                    case 99 -> new Action.MovementAction(1, 1);
                    case 97 -> new Action.MovementAction(-1, 1);
                    case 103 -> new Action.MovementAction(-1, -1);
                    case 105 -> new Action.MovementAction(1, -1);
                    case 27 -> new Action.EscapeAction();
                    default -> null;
                }
        );
    }

}
