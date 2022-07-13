package org.southy.rl;

public interface Action {
    void perform(Engine engine, Entity entity);

    class EscapeAction implements Action {
        @Override
        public void perform(Engine engine, Entity entity) {
            System.exit(0);
        }
    }
    class MovementAction implements Action {
        int dx;
        int dy;

        public MovementAction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public void perform(Engine engine, Entity entity) {
            if (engine.gameMap.getTileAt(entity.x + dx, entity.y +dy).walkable) {
                entity.move(dx, dy);
            }
        }
    }
}
