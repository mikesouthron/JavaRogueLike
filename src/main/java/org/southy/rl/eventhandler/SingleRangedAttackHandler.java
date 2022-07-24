package org.southy.rl.eventhandler;

import org.southy.rl.*;
import org.southy.rl.entity.Actor;
import org.southy.rl.map.GameMap;
import org.southy.sdl.SDL;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class SingleRangedAttackHandler extends SelectIndexEventHandler implements EventHandler{

    BiFunction<Integer, Integer, Action> callback;

    public SingleRangedAttackHandler(Engine engine, BiFunction<Integer, Integer, Action> callback) {
        super(engine);
        this.callback = callback;
    }

    @Override
    public void onRender(SDL sdl) {
        super.onRender(sdl);

        int startX = Application.camera.x - (Application.camera.width / 2);
        int startY = Application.camera.y - (Application.camera.height / 2);

        sdl.write("Target", 88, 1, ColorUtils.WHITE);

        var cursorRenderX = cursorX - startX + GameMap.MAP_OFFSET_X;
        var cursorRenderY = cursorY - startY + GameMap.MAP_OFFSET_Y;

        var tile = engine.gameMap.tiles[cursorX + cursorY * engine.gameMap.width];
        if (tile.fov) {
            Actor target = engine.gameMap.getActorAtLocation(cursorX, cursorY);
            var lines = new ArrayList<String>();
            if (target != null) {
                var text = Logger.wrap(target.name, 16);
                lines.addAll(text);
                sdl.write(target.str, cursorRenderX, cursorRenderY, target.fg, ColorUtils.CYAN);
            } else {
                lines.add("No Target");
                sdl.write(tile.light.ch, cursorRenderX, cursorRenderY, tile.light.fg, ColorUtils.CYAN);
            }

            var yOffset = lines.size() - 1;
            for (int i = lines.size() - 1; i >= 0; i--) {
                sdl.write(lines.get(i), 82, yOffset + 2, ColorUtils.WHITE);
                yOffset--;
            }
        } else {
            sdl.write("No Target", 82, 2);
            sdl.write(tile.dark.ch, cursorRenderX, cursorRenderY, tile.dark.fg, ColorUtils.CYAN);
        }

        sdl.write("Select a target", 82, 24);
        sdl.write("Enter to use", 82, 25);
    }

    @Override
    public Action onIndexSelected() {
        return callback.apply(cursorX, cursorY);
    }
}
