package org.southy.rl.eventhandler;

import org.southy.rl.Application;
import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Entity;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.GameMap;
import org.southy.rl.ui.Render;
import org.southy.sdl.SDL;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LookModeEventHandler extends SelectIndexEventHandler implements EventHandler {

    boolean renderInfo = false;

    public LookModeEventHandler(Engine engine) {
        super(engine);
    }

    private void renderActorInfo(SDL sdl, Actor actor) {
        var bg = ColorUtils.color(100, 100, 100);

        int x = Application.screenWidth - Application.rightUIWidth;

        for (int _x = x; _x < Application.screenWidth; _x++) {
            for (int y = 0; y < Application.screenHeight; y++) {
                sdl.write(" ", _x, y, sdl.getDefaultForegroundColor(), bg);
            }
        }
        sdl.write(actor.name, x, 1, sdl.getDefaultForegroundColor(), bg);

        sdl.write("Str " + actor.fighter.strength, x,  2, sdl.getDefaultForegroundColor(), bg);
        sdl.write("Agi " + actor.fighter.agility, x, 3, sdl.getDefaultForegroundColor(), bg);
        sdl.write("Con " + actor.fighter.constitution, x, 4, sdl.getDefaultForegroundColor(), bg);
        sdl.write("Int " + actor.fighter.intelligence, x, 5, sdl.getDefaultForegroundColor(), bg);
        sdl.write("HP " + actor.fighter.getHp() + " / " + actor.fighter.getMaxHp(), x, 7, sdl.getDefaultForegroundColor(), bg);

        var atk = actor.equipment.getMeleeAtkRange();
        sdl.write("Atk: " + atk.low + "-" + atk.high, x, 9, ColorUtils.color(200, 100, 0), bg);

        var def = actor.equipment.getMeleeDefenseRange();
        sdl.write("Def: " + def.low + "-" + def.high, x, 10, ColorUtils.color(100, 200, 0), bg);

        /*
        x = sdl.getWidthInCharacters() - Application.rightUIWidth;
        sdl.write("Equipment", x, 1);
        int y = 3;
        sdl.write("Head", x, y);
        if (player.equipment.items[EquipSlot.HEAD.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.HEAD.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Neck", x, y);
        if (player.equipment.items[EquipSlot.NECK.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.NECK.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Body", x, y);
        if (player.equipment.items[EquipSlot.BODY.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.BODY.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Back", x, y);
        if (player.equipment.items[EquipSlot.BACK.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.BACK.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Right Hand", x, y);
        if (player.equipment.items[EquipSlot.RIGHT_HAND.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.RIGHT_HAND.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Left Hand", x, y);
        if (player.equipment.items[EquipSlot.LEFT_HAND.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.LEFT_HAND.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Right Finger", x, y);
        if (player.equipment.items[EquipSlot.RIGHT_FINGER.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.RIGHT_FINGER.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Left Finger", x, y);
        if (player.equipment.items[EquipSlot.LEFT_FINGER.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.LEFT_FINGER.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
        sdl.write("Feet", x, y);
        if (player.equipment.items[EquipSlot.FEET.idx] != null) {
            sdl.write(player.equipment.items[EquipSlot.FEET.idx].name, x + 15, y++, ColorUtils.INVENTORY);
        } else {
            sdl.write("-", x + 15, y++, ColorUtils.color(100, 0, 0));
        }
         */
    }

    @Override
    public void onRender(SDL sdl) {
        super.onRender(sdl);

        int startX = Application.camera.x - (Application.camera.width / 2);
        int startY = Application.camera.y - (Application.camera.height / 2);

        var cursorRenderX = cursorX - startX + GameMap.MAP_OFFSET_X;
        var cursorRenderY = cursorY - startY + GameMap.MAP_OFFSET_Y;

        var tile = engine.gameMap.tiles[cursorX + cursorY * engine.gameMap.width];
        if (tile.fov) {
            List<Entity> entities = Render.getNamesAtLocation(cursorX, cursorY, engine.gameMap);

            if (entities.size() > 0) {
                var entity = entities
                        .stream()
                        .sorted(Comparator.comparing(a -> a.renderOrder.ordinal()))
                        .collect(Collectors.toList()).get(entities.size() - 1);

                sdl.write(entity.str, cursorRenderX, cursorRenderY, entity.fg, ColorUtils.CYAN);

                if (renderInfo) {
                    if (entity instanceof Actor) {
                        renderActorInfo(sdl, (Actor) entity);
                    }
                }

            } else {
                sdl.write(tile.light.ch, cursorRenderX, cursorRenderY, tile.light.fg, ColorUtils.CYAN);
            }
        } else {
            sdl.write(tile.dark.ch, cursorRenderX, cursorRenderY, tile.dark.fg, ColorUtils.CYAN);
        }
    }

    @Override
    public void handleEvents(SDL sdl) throws Impossible {
        var keyEvent = sdl.SDLGetEvent();
        if (keyEvent == null) {
            return;
        }
        if (MOVE_KEYS.containsKey(keyEvent.getKeyCode())) {
            var dir = MOVE_KEYS.get(keyEvent.getKeyCode());
            cursorX = Math.max(0, Math.min(cursorX + (keyEvent.isShiftDown() ? dir.x * 5 : dir.x), engine.gameMap.width - 1));
            cursorY = Math.max(0, Math.min(cursorY + (keyEvent.isShiftDown() ? dir.y * 5 : dir.y), engine.gameMap.height - 1));
            return;
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
            renderInfo = !renderInfo;
            return;
        }
        engine.eventHandler = new MainGameEventHandler(engine);
    }
}
