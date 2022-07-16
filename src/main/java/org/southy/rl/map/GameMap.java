package org.southy.rl.map;

import org.southy.rl.Engine;
import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Entity;
import org.southy.rl.gen.Procgen;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameMap {

    public Engine engine;

    public int width;
    public int height;

    public List<Entity> entities;

    public Tile[] tiles;

    public Tile[] visible;

    public Tile[] explored;

    public GameMap(Engine engine, int width, int height, List<Entity> entities) {
        this.engine = engine;
        this.width = width;
        this.height = height;
        this.entities = entities;
        tiles = new Tile[width * height];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = Tile.wallTile();
        }
        visible = new Tile[width * height];
        explored = new Tile[width * height];
    }

    private void setTileAt(int x, int y, Tile tile) {
        tiles[x + y * width] = tile;
    }

    public Tile getTileAt(int x, int y) {
        return tiles[x + y * width];
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void dig(RectangularRoom room) {
        for (Integer i : room.inner(width)) {
            tiles[i] = Tile.floorTile();
        }
    }

    public void digTunnel(RectangularRoom start, RectangularRoom end) {
        for (Integer i : Procgen.tunnel(start.centre(width), end.centre(width), width)) {
            tiles[i] = Tile.floorTile();
        }
    }

    public void computeFov(int x, int y) {
        FoV.computeFov(this, x, y, 8, true, FoV.FoVAlgorithm.FOV_RESTRICTIVE);
    }

    public Optional<Entity> getBlockingEntityAtLocation(int x, int y) {
        for (Entity entity : entities) {
            if (entity.blocksMovement && entity.x == x && entity.y == y) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    public void render(AsciiPanel panel) {
        for (int i = 0; i < tiles.length; i++) {
            var tile = tiles[i];
            int x = i % width;
            int y = i / width;

            if (visible[i] != null) {
                panel.write(tile.light.ch, x, y, tile.light.fg, tile.light.bg);
            } else if (explored[i] != null) {
                panel.write(tile.dark.ch, x, y, tile.dark.fg, tile.dark.bg);
            } else {
                panel.write(Tile.SHROUD.ch, x, y, Tile.SHROUD.fg, Tile.SHROUD.bg);
            }
        }

        entities.stream().filter(e -> visible[Procgen.toIdx(e.x, e.y, width)] != null).forEach(e -> panel.write(e.str, e.x, e.y, e.fg, e.bg));
    }

    public Integer countAvailableMoves(int x, int y) {
        int count = 0;
        if (inBounds(x - 1, y) && getTileAt(x - 1, y).walkable) {
            count++;
        }
        if (inBounds(x + 1, y) && getTileAt(x + 1, y).walkable) {
            count++;
        }
        if (inBounds(x, y - 1) &&  getTileAt(x, y - 1).walkable) {
            count++;
        }
        if (inBounds(x, y + 1) && getTileAt(x, y + 1).walkable) {
            count++;
        }
        return count;
    }

    public List<Actor> getActors() {
        return entities
                .stream()
                .filter(e -> e instanceof Actor)
                .map(e -> (Actor)e)
                .filter(Actor::isAlive).collect(Collectors.toList());
    }

    public Actor getActorAtLocation(int x, int y) {
        return getActors()
                .stream()
                .filter(a -> a.x == x && a.y == y)
                .findFirst()
                .orElse(null);
    }
}
