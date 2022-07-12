public interface Action {
    record EscapeAction() implements Action {}
    record MovementAction(int dx, int dy) implements Action {}
}
