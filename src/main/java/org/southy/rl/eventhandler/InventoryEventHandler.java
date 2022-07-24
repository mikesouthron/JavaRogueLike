package org.southy.rl.eventhandler;

import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.entity.Item;
import org.southy.rl.exceptions.Impossible;
import org.southy.sdl.SDL;

public class InventoryEventHandler implements EventHandler {

    enum Mode {
        VIEW, EXAMINE, USE, DROP
    }

    private Engine engine;
    private Mode mode;

    int idxToExamine = -1;

    public InventoryEventHandler(Engine engine) {
        this.engine = engine;
        mode = Mode.VIEW;
    }

    @Override
    public void handleEvents(SDL sdl) throws Impossible {
        var keyEvent = sdl.SDLGetEvent();
        if (keyEvent == null) {
            return;
        }
        if (mode == Mode.VIEW) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_X) {
                mode = Mode.EXAMINE;
                return;
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_U) {
                mode = Mode.USE;
                return;
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
                mode = Mode.DROP;
                return;
            }
            engine.eventHandler = new MainGameEventHandler(engine);
        } else if (mode == Mode.EXAMINE) {
            int idx = keyEvent.getKeyChar() - 97;
            if (idx >= 0 && idx < engine.player.inventory.items.size()) {
                idxToExamine = idx;
            } else {
                mode = Mode.VIEW;
                idxToExamine = -1;
            }
        } else if (mode == Mode.USE) {
            int idx = keyEvent.getKeyChar() - 97;
            if (idx >= 0 && idx < engine.player.inventory.items.size()) {
                var item = engine.player.inventory.items.get(idx);
                var action = item.consumable.getAction(engine.player);
                if (action.isPresent()) {
                    if (action.get().perform()) {
                        engine.handleEnemyTurns();
                        engine.updateFov();
                    }
                }
                mode = Mode.VIEW;
            } else {
                mode = Mode.VIEW;
            }
        } else if (mode == Mode.DROP) {
            int idx = keyEvent.getKeyChar() - 97;
            if (idx >= 0 && idx < engine.player.inventory.items.size()) {
                var item = engine.player.inventory.items.get(idx);
                engine.player.inventory.drop(item);
                engine.handleEnemyTurns();
                engine.updateFov();
                mode = Mode.VIEW;
            } else {
                mode = Mode.VIEW;
            }
        }
    }

    @Override
    public void onRender(SDL sdl) {
        EventHandler.super.onRender(sdl);
        sdl.write("Inventory", 85, 1, ColorUtils.WHITE);

        int yOffset = 2;
        java.util.List<Item> items = engine.player.inventory.items;
        for (int i = 0; i < items.size(); i++) {
            var item = items.get(i);
            var str = charForIdx(i) + ")" + item.name;
            if (str.length() > 16) {
                str = str.substring(0, 16);
            }
            sdl.write(str, 82, i + yOffset, ColorUtils.WHITE);
        }

        switch (mode) {
            case VIEW:
                sdl.write("x to examine", 82, 24);
                sdl.write("u to use", 82, 25);
                sdl.write("d to drop", 82, 26);
                break;
            case EXAMINE:
                if (idxToExamine == -1) {
                    sdl.write("examine", 82, 24);
                    sdl.write("which item?", 82, 25);
                } else {
                    var item = items.get(idxToExamine);
                    var str = Logger.wrap(item.name, 16);
                    for (int i = 0; i < str.size(); i++) {
                        sdl.write(str.get(i), 82, 24 + i);
                    }
                }
                break;
            case USE:
                sdl.write("use", 82, 24);
                sdl.write("which item?", 82, 25);
                break;
            case DROP:
                sdl.write("drop", 82, 24);
                sdl.write("which item?", 82, 25);
                break;
        }
    }

    private char charForIdx(int i) {
        return (char) (i + 97);
    }

    @Override
    public Engine engine() {
        return engine;
    }
}
