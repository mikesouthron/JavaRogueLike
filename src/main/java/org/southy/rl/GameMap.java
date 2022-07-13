package org.southy.rl;

import org.southy.rl.asciipanel.AsciiPanel;
import org.southy.rl.gen.Procgen;

public class GameMap {

    int width;
    int height;

    Tile[] tiles;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width * height];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = Tile.wallTile();
        }
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

    public void render(AsciiPanel panel) {
        for (int i = 0; i < tiles.length; i++) {
            var tile = tiles[i];
            int x = i % width;
            int y = i / width;
            panel.write(tile.dark.ch, x, y, tile.dark.fg, tile.dark.bg);
        }
    }
}
