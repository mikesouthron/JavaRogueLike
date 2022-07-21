package org.southy.rexpaint;

import org.southy.rl.Engine;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.GameMap;
import org.southy.rl.map.Tile;

import java.io.File;
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

    public static void parseUILayout(File file) {

    }

    public static void main(String[] args) {

    }

}
