package org.southy.rl.map;

import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.gen.Procgen;

public class GameMap {

    public int width;
    public int height;

    public Tile[] tiles;

    public Tile[] visible;

    public Tile[] explored;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
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
    }
}
