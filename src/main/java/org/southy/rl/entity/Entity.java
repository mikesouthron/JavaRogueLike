package org.southy.rl.entity;

import org.southy.rl.Application;
import org.southy.rl.ColorUtils;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.GameMap;

import java.awt.*;
import java.io.Serializable;

public class Entity implements Serializable {

    public EntityParent parent;

    public int x;
    public int y;
    public char str;
    public Color fg;
    public Color bg;

    public String name;

    public boolean blocksMovement;

    public RenderOrder renderOrder;

    public Entity() {
    }

    public Entity(GameMap parent, int x, int y, char str, Color fg, String name, boolean blocksMovement,
                  RenderOrder renderOrder) {
        this(parent, x, y, str, fg, Color.BLACK, name, blocksMovement, renderOrder);
    }

    public Entity(GameMap parent, int x, int y, char str, Color fg, Color bg, String name, boolean blocksMovement,
                  RenderOrder renderOrder) {
        this.x = x;
        this.y = y;
        this.str = str;
        this.fg = fg;
        this.bg = bg;
        this.name = name;
        this.blocksMovement = blocksMovement;
        this.renderOrder = renderOrder;
        if (parent != null) {
            this.parent = parent;
            parent.entities.add(this);
        }
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void place(int x, int y, GameMap map) throws Impossible {
        this.x = x;
        this.y = y;
        Application.camera.x = x;
        Application.camera.y = y;
        if (map != null) {
            if (this.parent != null) {
                this.parent.getEntities().remove(this);
            }
            this.parent = map;
            map.entities.add(this);
        }
    }

    public double distance(int x, int y) {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    public GameMap gamemap() {
        return parent.gamemap();
    }

    public void setParent(EntityParent parent) {
        this.parent = parent;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setStr(char str) {
        this.str = str;
    }

    public void setFg(Color fg) {
        this.fg = fg;
    }

    public void setBg(Color bg) {
        this.bg = bg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBlocksMovement(boolean blocksMovement) {
        this.blocksMovement = blocksMovement;
    }

    public void setRenderOrder(RenderOrder renderOrder) {
        this.renderOrder = renderOrder;
    }
}
