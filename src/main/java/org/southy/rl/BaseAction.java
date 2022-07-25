package org.southy.rl;

import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Entity;
import org.southy.rl.entity.Item;
import org.southy.rl.eventhandler.LevelUpEventHandler;
import org.southy.rl.eventhandler.MainGameEventHandler;
import org.southy.rl.eventhandler.MainMenuHandler;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.FastMoveState;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public abstract class BaseAction implements Action {
    Actor entity;

    public boolean shiftHeld = false;

    public BaseAction(Actor entity) {
        this.entity = entity;
    }

    Engine engine() {
        return entity.gamemap().engine;
    }

    public static class EscapeAction extends BaseAction {

        public EscapeAction(Actor entity) {
            super(entity);
        }

        @Override
        public boolean perform() {
            try {
                if (engine().player.isAlive()) {
                    long start = System.currentTimeMillis();
                    try (var os = new ObjectOutputStream(new FileOutputStream("game.save"))) {
                        os.writeObject(engine());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Saved in " + (System.currentTimeMillis() - start) + "ms");
                } else {
                    Files.deleteIfExists(Paths.get("game.save"));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            engine().eventHandler = new MainMenuHandler(engine());
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
        public boolean perform() throws Impossible {
            return false;
        }
    }

    public static class MeleeAction extends ActionWithDirection {
        public MeleeAction(Actor entity, int dx, int dy, boolean shiftHeld) {
            super(entity, dx, dy, shiftHeld);
        }

        @Override
        public boolean perform() throws Impossible {
            engine().fastMove = null;
            var target = targetActor();
            if (target.isEmpty()) {
                throw new Impossible("Nothing to attack");
            }

            /*
            weapon damage range + slide up based on strength * weapon.strmod
            defense range - based on armour - base = 1-2
             */

            var damage = entity.equipment.calculateMeleeDamage() - target.get().equipment.calculateMeleeDefense();

            var attackDesc = entity.name.toUpperCase() + " attacks " + target.get().name;

            var attackColor = entity == engine().player ? ColorUtils.PLAYER_ATK : ColorUtils.ENEMY_ATK;

            if (damage > 0) {
                engine().logger.addMessage(attackDesc + " for " + damage + " hit points", attackColor);
                target.get().fighter.setHp(target.get().fighter.getHp() - damage);
            } else {
                engine().logger.addMessage(attackDesc + " but does no damage", attackColor);
            }

            entity.inCombat = 3;
            target.get().inCombat = 3;

            return true;
        }
    }

    public static class MovementAction extends ActionWithDirection {

        public MovementAction(Actor entity, int dx, int dy, boolean shiftHeld) {
            super(entity, dx, dy, shiftHeld);
        }

        @Override
        public boolean perform() throws Impossible {
            var gameMap = entity.gamemap();
            
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

            if (!gameMap.inBounds(dest_x, dest_y)) {
                engine().fastMove = null;
                throw new Impossible("The way is blocked");
            }

            if (!gameMap.getTileAt(dest_x, dest_y).walkable) {
                if (engine().fastMove != null && engine().fastMove.currentAvailableMoves > 0 && entity.gamemap().countAvailableMoves(entity.x, entity.y) == 2) {
                    if (dy == 0) {
                        dx = 0;
                        if (gameMap.getTileAt(entity.x, entity.y + 1).walkable) {
                            dy = 1;
                        } else {
                            dy = -1;
                        }
                    } else if (dx == 0) {
                        dy = 0;
                        if (gameMap.getTileAt(entity.x + 1, entity.y).walkable) {
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
                    throw new Impossible("The way is blocked");
                }
            }

            dest_x = entity.x + dx;
            dest_y = entity.y + dy;

            if (gameMap.getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                engine().fastMove = null;
                throw new Impossible("The way is blocked");
            }

            if (engine().fastMove != null) {
                int count = gameMap.countAvailableMoves(dest_x, dest_y);
                var fastMove = engine().fastMove;
                if (fastMove.currentAvailableMoves > 0 && count != fastMove.currentAvailableMoves) {
                    engine().fastMove = null;
                } else {
                    fastMove.currentAvailableMoves = count;
                }
                //Enemy in fov
                for (Entity e : gameMap.entities) {
                    if (e != entity && e.blocksMovement && e.gamemap().getTileAt(e.x, e.y).fov) {
                        engine().fastMove = null;
                    }
                }
            }

            if (entity == engine().player) {
                Application.camera.x += dx;
                Application.camera.y += dy;
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
        public boolean perform() throws Impossible {
            var dest_x = entity.x + dx;
            var dest_y = entity.y + dy;

            if (entity.gamemap().getBlockingEntityAtLocation(dest_x, dest_y).isPresent()) {
                return new MeleeAction(entity, dx, dy, shiftHeld).perform();
            } else {
                return new MovementAction(entity, dx, dy, shiftHeld).perform();
            }
        }
    }

    public static class ItemAction implements Action {

        public Actor entity;
        public Item item;
        public int targetX;
        public int targetY;

        public ItemAction(Actor entity, Item item, int targetX, int targetY) {
            this.entity = entity;
            this.item = item;
            this.targetX = targetX;
            this.targetY = targetY;
        }

        public ItemAction(Actor entity, Item item) {
            this(entity, item, entity.x, entity.y);
        }

        public Optional<Actor> getTargetActor() {
            return Optional.ofNullable(entity.gamemap().getActorAtLocation(targetX, targetY));
        }

        @Override
        public boolean perform() throws Impossible {
            item.consumable.activate(this);
            return true;
        }
    }

    public static class PickupAction extends BaseAction {

        public PickupAction(Actor entity) {
            super(entity);
        }

        @Override
        public boolean perform() throws Impossible {
            for (Item item : engine().gameMap.getItems()) {
                if (entity.x == item.x && entity.y == item.y) {
                    if (entity.inventory.items.size() >= entity.inventory.capacity) {
                        throw new Impossible("Your inventory is full");
                    }

                    engine().gameMap.entities.remove(item);
                    item.setParent(entity);
                    entity.inventory.items.add(item);

                    engine().logger.addMessage("You picked up the " + item.name, ColorUtils.WHITE);

                    return true;
                }

            }

            throw new Impossible("There is nothing to pickup");
        }
    }

    public static class TakeStairsAction extends BaseAction {
        public TakeStairsAction(Actor entity) {
            super(entity);
        }

        @Override
        public boolean perform() throws Impossible {
            if (entity.x == engine().gameMap.upstairsX && entity.y == engine().gameMap.upstairsY) {
                engine().logger.addMessage("You ascend the stairs.", ColorUtils.DESCEND);
                engine().eventHandler = new LevelUpEventHandler(engine(), 2);
                return true;
            }
            throw new Impossible("No stairs here");
        }
    }

}
