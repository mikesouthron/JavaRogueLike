public interface Action {
    void perform(Engine engine, Entity entity);

    record EscapeAction() implements Action {
        @Override
        public void perform(Engine engine, Entity entity) {
            engine.logger.log("Escape Action");
            System.exit(0);
        }
    }
    record MovementAction(int dx, int dy) implements Action {
        @Override
        public void perform(Engine engine, Entity entity) {
            engine.logger.log("Movement Action");
            if (engine.gameMap.getTileAt(entity.x + dx, entity.y +dy).walkable) {
                entity.move(dx, dy);
            }
        }
    }
}
