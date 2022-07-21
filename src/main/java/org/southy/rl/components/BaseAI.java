package org.southy.rl.components;

import org.southy.rl.Action;
import org.southy.rl.entity.Actor;
import org.southy.rl.entity.Entity;
import org.southy.rl.exceptions.Impossible;
import org.southy.rl.map.Tile;
import org.southy.rl.pathing.Algorithm;
import org.southy.rl.pathing.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseAI implements Action, Serializable {

    Actor parent;

    public BaseAI(Actor parent) {
        this.parent = parent;
    }

    @Override
    public boolean perform() throws Impossible {
        return false;
    }

    public List<Integer> getPathTo(int destX, int destY) {
        var gameMap = parent.gamemap();
        
        //TODO: Move cost and graph calculations out to once per turn
        Integer[] cost = new Integer[gameMap.tiles.length];

        boolean[][] block = new boolean[gameMap.width][gameMap.height];

        for (Entity e : gameMap.entities) {
            if (e.blocksMovement) {
                block[e.x][e.y] = true;
            }
        }

        for (int i = 0; i < gameMap.tiles.length; i++) {
            Tile t = gameMap.tiles[i];

            int x = i % gameMap.width;
            int y = i / gameMap.width;

            if (t.walkable) {
                if (block[x][y]) {
                    cost[i] = 10;
                } else {
                    cost[i] = 1;
                }
            } else {
                cost[i] = null;
            }
        }

        Node[] graph = Algorithm.buildGraph(gameMap.width, gameMap.height, cost);

        Node start = graph[parent.x + parent.y * gameMap.width];

        Algorithm.calculateShortestPathFromSource(start);


        //TODO: Just return the index of the next location?

        List<Integer> path = new ArrayList<>();

        Node dest = graph[destX + destY * gameMap.width];
        boolean skip = true;
        for (Node node : dest.getShortestPath()) {
            if (skip) {
                skip = false;
                continue;
            }
            path.add(node.getX() + node.getY() * gameMap.width);
        }
        return path;
    }
}
