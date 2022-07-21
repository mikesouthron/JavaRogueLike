package org.southy.rexpaint;

import org.southy.rl.ColorUtils;
import org.southy.rl.Engine;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;
import org.southy.rl.map.Tile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class RexPaintParser {

    static class EnemyPool {
        //Set of enemies with % chance of appearing based on rareness * depth modifier.
    }

    static class ItemPool {
        //Set of items with % chance of appearing based on rareness * depth modifier.
    }

    //TODO: Think about fixed map layout pool, enemy pool is different depending on level (enemies have depth range + % of appearing at that depth)
    public static GameMap parseMap(File file, Engine engine, int width, int height, EnemyPool enemyPool, ItemPool itemPool) {
        List<Entity> entities = new ArrayList<>();
        GameMap gameMap = new GameMap(engine, width, height, entities);

        return gameMap;
    }

    public static void parseUILayout(File file) throws IOException {
        boolean skip = false;
        for (String line : Files.readAllLines(file.toPath())) {
            if (!skip) {
                skip = true;
                continue;
            }
            String[] data = line.split(",");
            System.out.printf("panel.write((char)%d, %d, %d, Color.decode(\"%s\"), Color.decode(\"%s\"));%n", Integer.parseInt(data[2]), Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[3], data[4]);
        }
    }

    public static void main(String[] args) throws IOException {
        parseUILayout(new File("/Users/mike.southron/rex-test.csv"));
    }

}
