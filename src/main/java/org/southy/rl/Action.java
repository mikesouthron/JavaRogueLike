package org.southy.rl;

import org.southy.rl.entity.Entity;

public abstract class Action {

    Entity entity;

    public Action(Entity entity) {
        this.entity = entity;
    }

    abstract void perform();

    Engine engine() {
        return entity.gameMap.engine;
    }

    static class EscapeAction extends Action {

        public EscapeAction(Entity entity) {
            super(entity);
        }

        @Override
        public void perform() {
            System.exit(0);
        }
    }

    static class ActionWithDirection extends Action {
        int dx;
        int dy;

        public ActionWithDirection(Entity entity, int dx, int dy) {
            super(entity);
            this.dx = dx;
            this.dy = dy;
        }

        @Override
        public void perform() {
            //TODO:
        }
    }

    static class MeleeAction extends ActionWithDirection {
        public MeleeAction(Entity entity, int dx, int dy) {
            super(entity, dx, dy);
        }

        @Override
        public void perform() {
            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;
            var target = entity.gameMap.getBlockingEntityAtLocation(dest_x, dest_y);
            if (target.isEmpty()) {
                return;
            }
            System.out.println("You kick " + target.get().name + ", much to its annoyance!");
        }
    }

    static class MovementAction extends ActionWithDirection {

        public MovementAction(Entity entity, int dx, int dy) {
            super(entity, dx, dy);
        }

        @Override
        public void perform() {
            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;

            if (!entity.gameMap.inBounds(dest_x, dest_y)) {
                return;
            }
            if (!entity.gameMap.getTileAt(entity.x + dx, entity.y +dy).walkable) {
                return;
            }
            if (entity.gameMap.getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                return;
            }

            entity.move(dx, dy);
        }
    }

    static class BumpAction extends ActionWithDirection {

        public BumpAction(Entity entity, int dx, int dy) {
            super(entity, dx, dy);
        }

        @Override
        public void perform() {
            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;

            if (entity.gameMap.getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                new MeleeAction(entity, dx, dy).perform();
            } else {
                new MovementAction(entity, dx, dy).perform();
            }
        }
    }
}
