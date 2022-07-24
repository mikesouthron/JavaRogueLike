package org.southy.rl.eventhandler;

import org.southy.rl.*;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;
import org.southy.rl.ui.Render;
import org.southy.sdl.SDL;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LookModeEventHandler extends SelectIndexEventHandler implements EventHandler {

    public LookModeEventHandler(Engine engine) {
        super(engine);
    }

    @Override
    public void onRender(SDL sdl) {
        super.onRender(sdl);

        int startX = Application.camera.x - (Application.camera.width / 2);
        int startY = Application.camera.y - (Application.camera.height / 2);

        sdl.write("Look", 88, 1, ColorUtils.WHITE);

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
                    sdl.write(lines.get(i), 82, yOffset + 2, ColorUtils.WHITE);
                    yOffset--;
                }

                var entity = entities
                        .stream()
                        .sorted(Comparator.comparing(a -> a.renderOrder.ordinal()))
                        .collect(Collectors.toList()).get(entities.size() - 1);

                var x = entity.x - startX + GameMap.MAP_OFFSET_X;
                var y = entity.x - startY + GameMap.MAP_OFFSET_Y;

                sdl.write(entity.str, cursorRenderX, cursorRenderY, entity.fg, ColorUtils.CYAN);
            } else {
                sdl.write(tile.name, 82, 2, ColorUtils.WHITE);
                sdl.write(tile.light.ch, cursorRenderX, cursorRenderY, tile.light.fg, ColorUtils.CYAN);
            }
        } else {
            sdl.write(tile.dark.ch, cursorRenderX, cursorRenderY, tile.dark.fg, ColorUtils.CYAN);
            sdl.write("Not Visible", 82, 2, ColorUtils.WHITE);
        }
    }

    @Override
    public Action onIndexSelected() {
        engine.eventHandler = new MainGameEventHandler(engine);
        return null;
    }
}
