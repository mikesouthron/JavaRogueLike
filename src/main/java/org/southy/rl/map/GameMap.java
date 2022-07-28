package org.southy.rl.map;

import org.southy.rl.Application;
import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.components.Equipable;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Entity;
import org.southy.rl.entity.EntityParent;
import org.southy.rl.entity.Item;
import org.southy.rl.gen.Procgen;
import org.southy.sdl.SDL;

import java.io.Serializable;
import java.util.*;

public class GameMap implements Serializable, EntityParent {

    public final static Integer MAP_OFFSET_X = Application.leftUIWidth;
    public final static Integer MAP_OFFSET_Y = Application.topUIHeight;

    public Engine engine;

    public int width;
    public int height;

    public List<Entity> entities;

    public Tile[] tiles;

    public Tile[] visible;

    public Tile[] explored;

    public List<Location> portalList = new ArrayList<>();

    public int upstairsX = 0;
    public int upstairsY = 0;

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
        FoV.computeFov(this, x, y, engine.player.fovRadius, true, FoV.FoVAlgorithm.FOV_RESTRICTIVE);
    }

    public Optional<Entity> getBlockingEntityAtLocation(int x, int y) {
        for (Entity entity : entities) {
            if (entity.blocksMovement && entity.x == x && entity.y == y) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    public Optional<Entity> getEntityAt(int x, int y) {
        for (Entity entity : entities) {
            if (entity.x == x && entity.y == y) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    public void render(SDL sdl) {
        int startX = Application.camera.x - (Application.camera.width / 2);
        int endX = Application.camera.x + (Application.camera.width / 2);
        int startY = Application.camera.y - (Application.camera.height / 2);
        int endY = Application.camera.y + (Application.camera.height / 2);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                var idx = x + y * width;

                int renderX = x - startX + MAP_OFFSET_X;
                int renderY = y - startY + MAP_OFFSET_Y;

                if (x < 0 || y < 0 || x >= width || y >= height) {
                    continue;
                }

                if (idx < 0 || idx >= tiles.length) {
                    sdl.write(Tile.SHROUD.ch, renderX, renderY, Tile.SHROUD.fg, Tile.SHROUD.bg);
                } else {
                    var tile = tiles[idx];
                    if (visible[idx] != null) {
                        sdl.write(tile.light.ch, renderX, renderY, tile.light.fg, tile.light.bg);
                    } else if (explored[idx] != null) {
                        sdl.write(tile.dark.ch, renderX, renderY, tile.dark.fg, tile.dark.bg);
                    } else {
                        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                            sdl.write(Tile.SHROUD.ch, renderX, renderY, ColorUtils.color(40, 0, 0), ColorUtils.color(40, 0, 0));
                        } else {
                            sdl.write(Tile.SHROUD.ch, renderX, renderY, Tile.SHROUD.fg, Tile.SHROUD.bg);
                        }
                    }
                }
            }
        }

        List<Integer> tilesWithNameIn = new ArrayList<>();

        entities
                .stream()
                .filter(e -> visible[Procgen.toIdx(e.x, e.y, width)] != null)
                .sorted(Comparator.comparing(a -> a.renderOrder.ordinal()))
                .forEach(e -> {
                    int renderX = e.x - startX + MAP_OFFSET_X;
                    int renderY = e.y - startY + MAP_OFFSET_Y;
                    if (renderX >= 0 && renderX < Application.camera.width && renderY >= 0 && renderY < Application.camera.height) {
                        sdl.write(e.str, renderX, renderY, e.fg, e.bg);
                        //This feels quite hacky, and maybe slow if there are lots of entities on the screen?
                        if (engine.showNames) {
                            boolean validPlus = false;
                            boolean validMinus = false;
                            Map<Integer, List<Integer>> tileListMap = new HashMap<>();
                            int offset = -1;
                            while (!validPlus && !validMinus) {
                                offset++;
                                int y1 = renderY + offset;
                                int y2 = renderY - offset;

                                List<Integer> tilesPlus = new ArrayList<>();
                                List<Integer> tilesMinus = new ArrayList<>();

                                tileListMap.put(y1, tilesPlus);
                                tileListMap.put(y2, tilesMinus);

                                validPlus = true;
                                validMinus = true;
                                for (int x = renderX + 1; x < renderX + 1 + e.name.length(); x++) {
                                    int i1 = x + y1 * width;
                                    int i2 = x + y2 * width;
                                    if (tilesWithNameIn.contains(i1) || getEntityAt(e.x + (x - renderX), e.y + offset).isPresent()) {
                                        validPlus = false;
                                    } else {
                                        tilesPlus.add(i1);
                                    }
                                    if (tilesWithNameIn.contains(i2) || getEntityAt(e.x + (x - renderX), e.y - offset).isPresent()) {
                                        validMinus = false;
                                    } else {
                                        tilesMinus.add(i2);
                                    }
                                }
                            }

                            List<Integer> tileList;
                            int y;
                            if (validPlus) {
                                tileList = tileListMap.get(renderY + offset);
                                y = renderY + offset;
                            } else {
                                tileList = tileListMap.get(renderY - offset);
                                y = renderY - offset;
                            }
                            if (tileList != null) {
                                tilesWithNameIn.addAll(tileList);
                                sdl.write(e.name, renderX + 1, y, ColorUtils.WHITE, ColorUtils.color(255, 0, 0));
                                if (y != renderY) {
                                    //TODO: Draw a line from renderX, renderY, to renderX + 1, y
                                }
                            }
                        }
                    }
                });
    }

    public Integer countAvailableMoves(int x, int y) {
        int count = 0;
        if (inBounds(x - 1, y) && getTileAt(x - 1, y).walkable) {
            count++;
        }
        if (inBounds(x + 1, y) && getTileAt(x + 1, y).walkable) {
            count++;
        }
        if (inBounds(x, y - 1) && getTileAt(x, y - 1).walkable) {
            count++;
        }
        if (inBounds(x, y + 1) && getTileAt(x, y + 1).walkable) {
            count++;
        }
        return count;
    }

    public List<Actor> getActors() {
        List<Actor> actors = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Actor) {
                if (((Actor) entity).isAlive()) {
                    actors.add((Actor) entity);
                }
            }
        }
        return actors;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Item) {
                items.add((Item) entity);
            }
        }
        return items;
    }

    public List<Equipable> getEquipment() {
        List<Equipable> items= new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Equipable) {
                items.add((Equipable) entity);
            }
        }
        return items;
    }

    public Actor getActorAtLocation(int x, int y) {
        return getActors()
                .stream()
                .filter(a -> a.x == x && a.y == y)
                .findFirst()
                .orElse(null);
    }

    public GameMap gamemap() {
        return this;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    public void setVisible(Tile[] visible) {
        this.visible = visible;
    }

    public void setExplored(Tile[] explored) {
        this.explored = explored;
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }
}
