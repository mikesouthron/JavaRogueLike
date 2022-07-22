package org.southy.rl.eventhandler;

import org.southy.rl.BaseAction;
import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Item;
import org.southy.rl.exceptions.Impossible;

import java.awt.*;
import java.awt.event.KeyEvent;

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
    public void handleEvents(KeyEvent event) throws Impossible {
        if (mode == Mode.VIEW) {
            if (event.getKeyCode() == KeyEvent.VK_X) {
                mode = Mode.EXAMINE;
                return;
            }
            if (event.getKeyCode() == KeyEvent.VK_U) {
                mode = Mode.USE;
                return;
            }
            if (event.getKeyCode() == KeyEvent.VK_D) {
                mode = Mode.DROP;
                return;
            }
            engine.eventHandler = new MainGameEventHandler(engine);
        } else if (mode == Mode.EXAMINE) {
            int idx = event.getKeyChar() - 97;
            if (idx >= 0 && idx < engine.player.inventory.items.size()) {
                idxToExamine = idx;
            } else {
                mode = Mode.VIEW;
                idxToExamine = -1;
            }
        } else if (mode == Mode.USE) {
            int idx = event.getKeyChar() - 97;
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
            int idx = event.getKeyChar() - 97;
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
    public void onRender(AsciiPanel panel) {
        EventHandler.super.onRender(panel);
        panel.write("Inventory", 85, 1, Color.WHITE);

        int yOffset = 2;
        java.util.List<Item> items = engine.player.inventory.items;
        for (int i = 0; i < items.size(); i++) {
            var item = items.get(i);
            var str = charForIdx(i) + ")" + item.name;
            if (str.length() > 16) {
                str = str.substring(0, 16);
            }
            panel.write(str, 82, i + yOffset, Color.WHITE);
        }

        switch (mode) {
            case VIEW:
                panel.write("x to examine", 82, 24);
                panel.write("u to use", 82, 25);
                panel.write("d to drop", 82, 26);
                break;
            case EXAMINE:
                if (idxToExamine == -1) {
                    panel.write("examine", 82, 24);
                    panel.write("which item?", 82, 25);
                } else {
                    var item = items.get(idxToExamine);
                    var str = Logger.wrap(item.name, 16);
                    for (int i = 0; i < str.size(); i++) {
                        panel.write(str.get(i), 82, 24 + i);
                    }
                }
                break;
            case USE:
                panel.write("use", 82, 24);
                panel.write("which item?", 82, 25);
                break;
            case DROP:
                panel.write("drop", 82, 24);
                panel.write("which item?", 82, 25);
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
