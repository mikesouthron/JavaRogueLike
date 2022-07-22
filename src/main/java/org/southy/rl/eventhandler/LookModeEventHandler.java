package org.southy.rl.eventhandler;

import org.southy.rl.Action;
import org.southy.rl.Application;
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

        int startX = Application.camera.x - (Application.camera.width / 2);
        int startY = Application.camera.y - (Application.camera.height / 2);

        panel.write("Look", 88, 1, Color.WHITE);

        var cursorRenderX = cursorX - startX + GameMap.MAP_OFFSET_X;
        var cursorRenderY = cursorY - startY + GameMap.MAP_OFFSET_Y;

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

                var x = entity.x - startX + GameMap.MAP_OFFSET_X;
                var y = entity.x - startY + GameMap.MAP_OFFSET_Y;

                panel.write(entity.str, cursorRenderX, cursorRenderY, entity.fg, Color.CYAN);
            } else {
                panel.write(tile.name, 82, 2, Color.WHITE);
                panel.write(tile.light.ch, cursorRenderX, cursorRenderY, tile.light.fg, Color.CYAN);
            }
        } else {
            panel.write(tile.dark.ch, cursorRenderX, cursorRenderY, tile.dark.fg, Color.CYAN);
            panel.write("Not Visible", 82, 2, Color.WHITE);
        }
    }

    @Override
    public Action onIndexSelected() {
        engine.eventHandler = new MainGameEventHandler(engine);
        return null;
    }
}
