package org.southy.rl;

import org.southy.rl.entity.Entity;

public interface Action {
    void perform(Engine engine, Entity entity);

    class EscapeAction implements Action {
        @Override
        public void perform(Engine engine, Entity entity) {
            System.exit(0);
        }
    }

    class ActionWithDirection implements Action {
        int dx;
        int dy;

        public ActionWithDirection(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public void perform(Engine engine, Entity entity) {
            //TODO:
        }
    }

    class MeleeAction extends ActionWithDirection {
        public MeleeAction(int dx, int dy) {
            super(dx, dy);
        }

        @Override
        public void perform(Engine engine, Entity entity) {
            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;
            var target = engine.gameMap.getBlockingEntityAtLocation(dest_x, dest_y);
            if (target.isEmpty()) {
                return;
            }
            System.out.println("You kick " + target.get().name + ", much to its annoyance!");
        }
    }

    class MovementAction extends ActionWithDirection {

        public MovementAction(int dx, int dy) {
            super(dx, dy);
        }

        @Override
        public void perform(Engine engine, Entity entity) {
            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;

            if (!engine.gameMap.inBounds(dest_x, dest_y)) {
                return;
            }
            if (!engine.gameMap.getTileAt(entity.x + dx, entity.y +dy).walkable) {
                return;
            }
            if (engine.gameMap.getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                return;
            }

            entity.move(dx, dy);
        }
    }

    class BumpAction extends ActionWithDirection {

        public BumpAction(int dx, int dy) {
            super(dx, dy);
        }

        @Override
        public void perform(Engine engine, Entity entity) {
            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;

            if (engine.gameMap.getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                new MeleeAction(dx, dy).perform(engine, entity);
            } else {
                new MovementAction(dx, dy).perform(engine, entity);
            }
        }
    }
}
