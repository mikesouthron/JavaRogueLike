package org.southy.rl.eventhandler;

import org.southy.rl.Action;
import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Actor;
import org.southy.rl.map.GameMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.BiFunction;

public class SingleRangedAttackHandler extends SelectIndexEventHandler implements EventHandler{

    BiFunction<Integer, Integer, Action> callback;

    public SingleRangedAttackHandler(Engine engine, BiFunction<Integer, Integer, Action> callback) {
        super(engine);
        this.callback = callback;
    }

    @Override
    public void onRender(AsciiPanel panel) {
        super.onRender(panel);

        panel.write("Target", 88, 1, Color.WHITE);

        var tile = engine.gameMap.tiles[cursorX + cursorY * engine.gameMap.width];
        if (tile.fov) {
            Actor target = engine.gameMap.getActorAtLocation(cursorX, cursorY);
            var lines = new ArrayList<String>();
            if (target != null) {
                var text = Logger.wrap(target.name, 16);
                lines.addAll(text);
                panel.write(target.str, target.x + GameMap.MAP_OFFSET_X, target.y + GameMap.MAP_OFFSET_Y, target.fg, Color.CYAN);
            } else {
                lines.add("No Target");
                panel.write(tile.light.ch, cursorX + GameMap.MAP_OFFSET_X, cursorY + GameMap.MAP_OFFSET_Y, tile.light.fg, Color.CYAN);
            }

            var yOffset = lines.size() - 1;
            for (int i = lines.size() - 1; i >= 0; i--) {
                panel.write(lines.get(i), 82, yOffset + 2, Color.WHITE);
                yOffset--;
            }
        } else {
            panel.write("No Target", 82, 2);
            panel.write(tile.dark.ch, cursorX + GameMap.MAP_OFFSET_X, cursorY + GameMap.MAP_OFFSET_Y, tile.dark.fg, Color.CYAN);
        }

        panel.write("Select a target", 82, 24);
        panel.write("Enter to use", 82, 25);
    }

    @Override
    public Action onIndexSelected() {
        return callback.apply(cursorX, cursorY);
    }
}
