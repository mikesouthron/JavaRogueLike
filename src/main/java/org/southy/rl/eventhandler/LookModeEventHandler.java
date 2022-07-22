package org.southy.rl.eventhandler;

import org.southy.rl.Action;
import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;
import org.southy.rl.ui.Render;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LookModeEventHandler extends SelectIndexEventHandler implements EventHandler {

    public LookModeEventHandler(Engine engine) {
        super(engine);
    }

    @Override
    public void onRender(AsciiPanel panel) {
        super.onRender(panel);

        panel.write("Look", 88, 1, Color.WHITE);

        var tile = engine.gameMap.tiles[cursorX + cursorY * engine.gameMap.width];
        if (tile.fov) {
            List<Entity> entities = Render.getNamesAtLocation(cursorX, cursorY, engine.gameMap);

            if (entities.size() > 0) {
                var lines = new ArrayList<String>();
                for (Entity entity : entities) {
                    var text = Logger.wrap(entity.name, 16);
                    lines.addAll(text);
                }
                var yOffset = lines.size() - 1;
                for (int i = lines.size() - 1; i >= 0; i--) {
                    panel.write(lines.get(i), 82, yOffset + 2, Color.WHITE);
                    yOffset--;
                }

                var entity = entities
                        .stream()
                        .sorted(Comparator.comparing(a -> a.renderOrder.ordinal()))
                        .collect(Collectors.toList()).get(entities.size() - 1);

                panel.write(entity.str, entity.x + GameMap.MAP_OFFSET_X, entity.y + GameMap.MAP_OFFSET_Y, entity.fg, Color.CYAN);
            } else {
                panel.write(tile.name, 82, 2, Color.WHITE);
                panel.write(tile.light.ch, cursorX + GameMap.MAP_OFFSET_X, cursorY + GameMap.MAP_OFFSET_Y, tile.light.fg, Color.CYAN);
            }
        } else {
            panel.write(tile.dark.ch, cursorX + GameMap.MAP_OFFSET_X, cursorY + GameMap.MAP_OFFSET_Y, tile.dark.fg, Color.CYAN);
            panel.write("Not Visible", 82, 2, Color.WHITE);
        }
    }

    @Override
    public Action onIndexSelected() {
        engine.eventHandler = new MainGameEventHandler(engine);
        return null;
    }
}
