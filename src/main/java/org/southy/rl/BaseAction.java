package org.southy.rl;

import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Entity;
import org.southy.rl.eventhandler.MainGameEventHandler;
import org.southy.rl.map.FastMoveState;

import java.util.Optional;

public abstract class BaseAction implements Action {
    Actor entity;

    public boolean shiftHeld = false;

    public BaseAction(Actor entity) {
        this.entity = entity;
    }

    Engine engine() {
        return entity.gameMap.engine;
    }

    public static class EscapeAction extends BaseAction {

        public EscapeAction(Actor entity) {
            super(entity);
        }

        @Override
        public boolean perform() {
            System.exit(0);
            return false;
        }
    }

    public static class WaitAction extends BaseAction {

        public WaitAction(Actor entity) {
            super(entity);
        }

        @Override
        public boolean perform() {
            return true;
        }
    }

    static class ActionWithDirection extends BaseAction {
        int dx;
        int dy;

        public ActionWithDirection(Actor entity, int dx, int dy, boolean shiftHeld) {
            super(entity);
            this.dx = dx;
            this.dy = dy;
            this.shiftHeld = shiftHeld;
        }

        private MainGameEventHandler.Direction dest() {
            return new MainGameEventHandler.Direction(entity.x + dx, entity.y + dy);
        }

        public Optional<Actor> targetActor() {
            var d = dest();
            return Optional.ofNullable(engine().gameMap.getActorAtLocation(d.x, d.y));
        }

        @Override
        public boolean perform() {
            return false;
        }
    }

    public static class MeleeAction extends ActionWithDirection {
        public MeleeAction(Actor entity, int dx, int dy, boolean shiftHeld) {
            super(entity, dx, dy, shiftHeld);
        }

        @Override
        public boolean perform() {
            engine().fastMove = null;
            var target = targetActor();
            if (target.isEmpty()) {
                return false;
            }

            var damage = entity.fighter.power - target.get().fighter.defence;

            var attackDesc = entity.name.toUpperCase() + " attacks " + target.get().name;

            if (damage > 0) {
                target.get().fighter.setHp(target.get().fighter.getHp() - damage);
                System.out.println(attackDesc + " for " + damage + " hit points");
            } else {
                System.out.println(attackDesc + " but does no damage");
            }

            return true;
        }
    }

    public static class MovementAction extends ActionWithDirection {

        public MovementAction(Actor entity, int dx, int dy, boolean shiftHeld) {
            super(entity, dx, dy, shiftHeld);
        }

        @Override
        public boolean perform() {
            if (shiftHeld && engine().fastMove == null) {
                engine().fastMove = new FastMoveState();
            }

            if (engine().fastMove != null && engine().fastMove.dx != null) {
                dx = engine().fastMove.dx;
            }
            if (engine().fastMove != null && engine().fastMove.dy != null) {
                dy = engine().fastMove.dy;
            }

            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;

            if (!entity.gameMap.inBounds(dest_x, dest_y)) {
                engine().fastMove = null;
                return false;
            }

            if (!entity.gameMap.getTileAt(dest_x, dest_y).walkable) {
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
                    engine().fastMove.dx = dx;
                    engine().fastMove.dy = dy;
                } else {
                    //Special case for fast move, if not null and right angle is available, take right angle
                    engine().fastMove = null;
                    return false;
                }
            }

            dest_x = entity.x + dx;
            dest_y = entity.y + dy;

            if (entity.gameMap.getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                engine().fastMove = null;
                return false;
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
            return true;
        }
    }

    public static class BumpAction extends ActionWithDirection {

        public BumpAction(Actor entity, int dx, int dy, boolean shiftHeld) {
            super(entity, dx, dy, shiftHeld);
        }

        @Override
        public boolean perform() {
            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;

            if (entity.gameMap.getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                return new MeleeAction(entity, dx, dy, shiftHeld).perform();
            } else {
                return new MovementAction(entity, dx, dy, shiftHeld).perform();
            }
        }
    }
}
