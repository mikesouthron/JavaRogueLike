package org.southy.rl;

import org.southy.rl.entity.Entity;
import org.southy.rl.map.FastMoveState;

public abstract class Action {

    Entity entity;

    public boolean shiftHeld = false;

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

        public ActionWithDirection(Entity entity, int dx, int dy, boolean shiftHeld) {
            super(entity);
            this.dx = dx;
            this.dy = dy;
            this.shiftHeld = shiftHeld;
        }

        @Override
        public void perform() {
            //TODO:
        }
    }

    static class MeleeAction extends ActionWithDirection {
        public MeleeAction(Entity entity, int dx, int dy, boolean shiftHeld) {
            super(entity, dx, dy, shiftHeld);
        }

        @Override
        public void perform() {
            engine().fastMove = null;
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

        public MovementAction(Entity entity, int dx, int dy, boolean shiftHeld) {
            super(entity, dx, dy, shiftHeld);
        }

        @Override
        public void perform() {
            if (shiftHeld && engine().fastMove == null) {
                engine().fastMove = new FastMoveState();
            }

            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;

            if (!entity.gameMap.inBounds(dest_x, dest_y)) {
                engine().fastMove = null;
                return;
            }

            if (!entity.gameMap.getTileAt(entity.x + dx, entity.y + dy).walkable) {
                if (engine().fastMove != null && engine().fastMove.currentAvailableMoves > 0 && entity.gameMap.countAvailableMoves(entity.x, entity.y) == 2) {
                    if (dy == 0) {
                        dx = 0;
                        if (entity.gameMap.getTileAt(entity.x, entity.y + 1).walkable) {
                            dy = 1;
                        } else {
                            dy = -1;
                        }
                    } else if (dx == 0) {
                        dy = 0;
                        if (entity.gameMap.getTileAt(entity.x + 1, entity.y).walkable) {
                            dx = 1;
                        } else {
                            dx = -1;
                        }
                    }
                } else {
                    //Special case for fast move, if not null and right angle is available, take right angle
                    engine().fastMove = null;
                    return;
                }
            }

            dest_x = entity.x + dx;
            dest_y = entity.y + dy;

            if (entity.gameMap.getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                engine().fastMove = null;
                return;
            }

            if (engine().fastMove != null) {
                int count = entity.gameMap.countAvailableMoves(dest_x, dest_y);
                var fastMove = engine().fastMove;
                if (fastMove.currentAvailableMoves > 0 && count != fastMove.currentAvailableMoves) {
                    engine().fastMove = null;
                } else {
                    fastMove.currentAvailableMoves = count;
                }
                //Enemy in fov
                for (Entity e : entity.gameMap.entities) {
                    if (e != entity && e.blocksMovement && e.gameMap.getTileAt(e.x, e.y).fov) {
                        engine().fastMove = null;
                    }
                }
            }

            entity.move(dx, dy);
        }
    }

    static class BumpAction extends ActionWithDirection {

        public BumpAction(Entity entity, int dx, int dy, boolean shiftHeld) {
            super(entity, dx, dy, shiftHeld);
        }

        @Override
        public void perform() {
            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;

            if (entity.gameMap.getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                new MeleeAction(entity, dx, dy, shiftHeld).perform();
            } else {
                new MovementAction(entity, dx, dy, shiftHeld).perform();
            }
        }
    }
}
