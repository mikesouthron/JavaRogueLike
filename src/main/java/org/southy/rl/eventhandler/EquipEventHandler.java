package org.southy.rl.eventhandler;

import org.southy.rl.Application;
import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.components.EquipSlot;
import org.southy.rl.components.Equipable;
import org.southy.rl.entity.Actor;
import org.southy.rl.exceptions.Impossible;
import org.southy.sdl.SDL;

import java.util.ArrayList;
import java.util.List;

public class EquipEventHandler implements EventHandler {

    Engine engine;
    Actor actor;
    Equipable equipable;

    List<Equipable> existing = new ArrayList<>();

    EquipSlot equipSlot;

    public EquipEventHandler(Engine engine, Actor actor, Equipable equipable) {
        this.engine = engine;
        this.actor = actor;
        this.equipable = equipable;

        switch (equipable.bodyPart) {
            case HEAD:
                existing.add(actor.equipment.items[EquipSlot.HEAD.idx]);
                equipSlot = EquipSlot.HEAD;
                break;
            case NECK:
                existing.add(actor.equipment.items[EquipSlot.NECK.idx]);
                equipSlot = EquipSlot.NECK;
                break;
            case BODY:
                existing.add(actor.equipment.items[EquipSlot.BODY.idx]);
                equipSlot = EquipSlot.BODY;
                break;
            case BACK:
                existing.add(actor.equipment.items[EquipSlot.BACK.idx]);
                equipSlot = EquipSlot.BACK;
                break;
            case HAND:
                existing.add(actor.equipment.items[EquipSlot.RIGHT_HAND.idx]);
                equipSlot = EquipSlot.RIGHT_HAND;
                break;
            case OFF_HAND:
                existing.add(actor.equipment.items[EquipSlot.LEFT_HAND.idx]);
                equipSlot = EquipSlot.LEFT_HAND;
                break;
            case FINGER:
                existing.add(actor.equipment.items[EquipSlot.RIGHT_FINGER.idx]);
                existing.add(actor.equipment.items[EquipSlot.LEFT_FINGER.idx]);
//                equipSlot = EquipSlot.LEFT_FINGER | EquipSlot.RIGHT_FINGER;
                break;
            case FEET:
                existing.add(actor.equipment.items[EquipSlot.FEET.idx]);
                equipSlot = EquipSlot.FEET;
                break;
        }
    }

    private boolean valid(int x, int y) {
        return engine.gameMap.getTileAt(x, y).walkable && engine.gameMap.getEntityAt(x, y).isEmpty();
    }

    private MainGameEventHandler.Direction nearestAvaiable(int x, int y) {
        int distance = 1;
        while (true) {
            if (valid(x + distance, y)) {
                return new MainGameEventHandler.Direction(distance, 0);
            }
            if (valid(x - distance, y)) {
                return new MainGameEventHandler.Direction(-distance, 0);
            }
            if (valid(x, y + distance)) {
                return new MainGameEventHandler.Direction(0, distance);
            }
            if (valid(x, y - distance)) {
                return new MainGameEventHandler.Direction(0, -distance);
            }
            if (valid(x + distance, y + distance)) {
                return new MainGameEventHandler.Direction(distance, distance);
            }
            if (valid(x - distance, y + distance)) {
                return new MainGameEventHandler.Direction(-distance, distance);
            }
            if (valid(x - distance, y - distance)) {
                return new MainGameEventHandler.Direction(-distance, -distance);
            }
            if (valid(x + distance, y - distance)) {
                return new MainGameEventHandler.Direction(distance, -distance);
            }
            distance++;
        }
    }

    @Override
    public void handleEvents(SDL sdl) throws Impossible {
        var keyEvent = sdl.SDLGetEvent();
        if (keyEvent == null) {
            return;
        }

        boolean swap = false;

        if (existing.size() <= 1) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_Y) {
                swap = true;
            }
        } else {
             if (keyEvent.getKeyCode() == KeyEvent.VK_1 || keyEvent.getKeyCode() == KeyEvent.VK_2) {
                 if (keyEvent.getKeyCode() == KeyEvent.VK_1) {
                     equipSlot = EquipSlot.RIGHT_FINGER;
                 } else {
                     equipSlot = EquipSlot.LEFT_FINGER;
                 }
                 swap = true;
             }
        }

        if (swap) {
            var current = actor.equipment.items[equipSlot.idx];
            if (current != null) {
                var dir = nearestAvaiable(actor.x, actor.y);
                current.place(actor.x + dir.x, actor.y + dir.y, engine.gameMap);
            }

            actor.equipment.items[equipSlot.idx] = equipable;
            engine.gameMap.entities.remove(equipable);
            equipable.setParent(null);

            engine.eventHandler = new MainGameEventHandler(engine);
            engine.handleEnemyTurns();
            engine.updateFov();
            engine.endOfTurn();
        }


        engine.eventHandler = new MainGameEventHandler(engine);
    }

    @Override
    public void onRender(SDL sdl) {
        EventHandler.super.onRender(sdl);

        var bg = ColorUtils.color(100, 100, 100);

        int x = Application.screenWidth - Application.rightUIWidth;

        for (int _x = x; _x < Application.screenWidth; _x++) {
            for (int y = 0; y < Application.screenHeight; y++) {
                sdl.write(" ", _x, y, sdl.getDefaultForegroundColor(), bg);
            }
        }

        sdl.write("Equip " + equipable.name + "?", x, 1, sdl.getDefaultForegroundColor(), bg);

        int yOffset = 2;

        if (equipable.atk != null) {
            sdl.write("Atk: " + equipable.atk.low + "-" + equipable.atk.high, x, yOffset++, ColorUtils.color(200, 100, 0), bg);
        }
        if (equipable.def != null) {
            sdl.write("Def: " + equipable.def.low + "-" + equipable.def.high, x, yOffset++, ColorUtils.color(100, 200, 0), bg);
        }
        if (equipable.strMod > 0) {
            sdl.write("StrMod: " + equipable.strMod, x, yOffset++, ColorUtils.color(200, 100, 0), bg);
        }

        var currentAtk = actor.equipment.getMeleeAtkRange();
        var currentDef = actor.equipment.getMeleeDefenseRange();

        yOffset++;

        sdl.write("Change", x, yOffset++, sdl.getDefaultForegroundColor(), bg);

        int idx = 0;
        for (Equipable eq : existing) {
            if (existing.size() > 1) {
                sdl.write((idx + 1) + ") " + (eq != null ? eq.name : "Empty"), x, yOffset++, sdl.getDefaultForegroundColor(), bg);
            }
            var atk = actor.equipment.getMeleeAtkRange(eq, equipable);
            sdl.write("Atk: " + currentAtk.low + "-" + currentAtk.high + " -> " + atk.low + "-" + atk.high, x, yOffset++, ColorUtils.color(200, 100, 0), bg);

            var def = actor.equipment.getMeleeDefenseRange(eq, equipable);
            sdl.write("Def: " + currentDef.low + "-" + currentDef.high + " -> " + def.low + "-" + def.high, x, yOffset++, ColorUtils.color(200, 100, 0), bg);
            idx++;
        }

        if (existing.size() <= 1) {
            sdl.write("Press Y to equip", x, yOffset + 2);
        } else {
            sdl.write("Press number to swap", x, yOffset + 2);
        }
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
