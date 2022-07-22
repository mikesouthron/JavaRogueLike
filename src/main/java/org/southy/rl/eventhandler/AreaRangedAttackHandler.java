package org.southy.rl.eventhandler;

import org.southy.rl.Action;
import org.southy.rl.Application;
import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.Logger;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;
import org.southy.rl.map.Tile;

import java.awt.*;
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
    public void onRender(AsciiPanel panel) {
        super.onRender(panel);

        int startCX = Application.camera.x - (Application.camera.width / 2);
        int startCY = Application.camera.y - (Application.camera.height / 2);

        var cursorRenderX = cursorX - startCX + GameMap.MAP_OFFSET_X;
        var cursorRenderY = cursorY - startCY + GameMap.MAP_OFFSET_Y;

        panel.write("Target Area", 85, 1, Color.WHITE);

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
                            panel.write(tile.light.ch, x - startCX + GameMap.MAP_OFFSET_X, y - startCY + GameMap.MAP_OFFSET_Y, tile.light.fg,
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

                                panel.write(entity.str, entity.x - startCX + GameMap.MAP_OFFSET_X, entity.y - startCY + GameMap.MAP_OFFSET_Y, entity.fg,
                                        ColorUtils.color(200, 0, 0));
                            }
                        } else {
                            if (engine.gameMap.explored[idx] != null) {
                                panel.write(tile.light.ch, x - startCX + GameMap.MAP_OFFSET_X, y - startCY + GameMap.MAP_OFFSET_Y, tile.dark.fg,
                                        ColorUtils.color(100, 0, 0));
                            } else {
                                panel.write(Tile.SHROUD.ch, x - startCX + GameMap.MAP_OFFSET_X, y - startCY + GameMap.MAP_OFFSET_Y, Tile.SHROUD.fg,
                                        ColorUtils.color(100, 0, 0));
                            }
                        }
                    }
                }
            }
        }

        //TODO: List visible entities in blast area.

        panel.write("Select area", 82, 24);
        panel.write("Enter to use", 82, 25);
    }

    @Override
    public Action onIndexSelected() {
        return callback.apply(cursorX, cursorY);
    }




}
