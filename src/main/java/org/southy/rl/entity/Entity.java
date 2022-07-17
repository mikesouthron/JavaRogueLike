package org.southy.rl.entity;

import org.southy.rl.ColorUtils;
import org.southy.rl.map.GameMap;

import java.awt.*;

public class Entity {

    public GameMap gameMap;

    public int x;
    public int y;
    public char str;
    public Color fg;
    public Color bg;

    public String name;

    public boolean blocksMovement;

    public RenderOrder renderOrder;

    public Entity(GameMap gameMap, int x, int y, char str, Color fg, String name, boolean blocksMovement,
                  RenderOrder renderOrder) {
        this(gameMap, x, y, str, fg, ColorUtils.FLOOR_COLOR_LIGHT, name, blocksMovement, renderOrder);
    }

    public Entity(GameMap gameMap, int x, int y, char str, Color fg, Color bg, String name, boolean blocksMovement,
                  RenderOrder renderOrder) {
        this.x = x;
        this.y = y;
        this.str = str;
        this.fg = fg;
        this.bg = bg;
        this.name = name;
        this.blocksMovement = blocksMovement;
        this.renderOrder = renderOrder;
        if (gameMap != null) {
            this.gameMap = gameMap;
            gameMap.entities.add(this);
        }
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void place(int x, int y, GameMap map) {
        this.x = x;
        this.y = y;
        if (map != null) {
            if (this.gameMap != null) {
                this.gameMap.entities.remove(this);
            }
            this.gameMap = map;
            map.entities.add(this);
        }
    }

}
