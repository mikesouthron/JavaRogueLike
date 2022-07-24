package org.southy.rl.eventhandler;

import org.southy.rl.Action;
import org.southy.rl.Application;
import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;
import org.southy.rl.map.Tile;
import org.southy.sdl.SDL;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class AreaRangedAttackHandler extends SelectIndexEventHandler implements EventHandler{

    int radius;
    BiFunction<Integer, Integer, Action> callback;

    public AreaRangedAttackHandler(Engine engine, int radius, BiFunction<Integer, Integer, Action> callback) {
        super(engine);
        this.radius = radius;
        this.callback = callback;
    }

    private double distance(int x, int y, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
    }

    @Override
    public void onRender(SDL sdl) {
        super.onRender(sdl);

        int startCX = Application.camera.x - (Application.camera.width / 2);
        int startCY = Application.camera.y - (Application.camera.height / 2);

        var cursorRenderX = cursorX - startCX + GameMap.MAP_OFFSET_X;
        var cursorRenderY = cursorY - startCY + GameMap.MAP_OFFSET_Y;

        sdl.write("Target Area", 85, 1, ColorUtils.WHITE);

        int startX = cursorX - radius - 1;
        int startY = cursorY - radius - 1;
        int width = radius * radius;
        int height = radius * radius;

        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                if (distance(cursorX, cursorY, x, y) <= radius) {
                    int idx = x + y * engine.gameMap.width;
                    if (idx >= 0 && idx < engine.gameMap.tiles.length) {
                        var tile = engine.gameMap.tiles[idx];
                        if (tile.fov) {
                            sdl.write(tile.light.ch, x - startCX + GameMap.MAP_OFFSET_X, y - startCY + GameMap.MAP_OFFSET_Y, tile.light.fg,
                                    ColorUtils.color(200, 0, 0));

                            var entityList = new ArrayList<Entity>();
                            for (Entity entity : engine.gameMap.entities) {
                                if (entity.x == x && entity.y == y) {
                                    entityList.add(entity);
                                }
                            }
                            if (entityList.size() > 0) {
                                var entity = entityList
                                        .stream()
                                        .sorted(Comparator.comparing(a -> a.renderOrder.ordinal()))
                                        .collect(Collectors.toList()).get(entityList.size() - 1);

                                sdl.write(entity.str, entity.x - startCX + GameMap.MAP_OFFSET_X, entity.y - startCY + GameMap.MAP_OFFSET_Y, entity.fg,
                                        ColorUtils.color(200, 0, 0));
                            }
                        } else {
                            if (engine.gameMap.explored[idx] != null) {
                                sdl.write(tile.light.ch, x - startCX + GameMap.MAP_OFFSET_X, y - startCY + GameMap.MAP_OFFSET_Y, tile.dark.fg,
                                        ColorUtils.color(100, 0, 0));
                            } else {
                                sdl.write(Tile.SHROUD.ch, x - startCX + GameMap.MAP_OFFSET_X, y - startCY + GameMap.MAP_OFFSET_Y, Tile.SHROUD.fg,
                                        ColorUtils.color(100, 0, 0));
                            }
                        }
                    }
                }
            }
        }

        //TODO: List visible entities in blast area.

        sdl.write("Select area", 82, 24);
        sdl.write("Enter to use", 82, 25);
    }

    @Override
    public Action onIndexSelected() {
        return callback.apply(cursorX, cursorY);
    }




}
