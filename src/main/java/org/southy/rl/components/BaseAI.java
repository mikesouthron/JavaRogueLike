package org.southy.rl.components;

import org.southy.rl.Action;
import org.southy.rl.entity.Entity;
import org.southy.rl.map.Tile;
import org.southy.rl.pathing.Algorithm;
import org.southy.rl.pathing.Node;

import java.util.ArrayList;
import java.util.List;

public class BaseAI extends BaseComponent implements Action {

    public BaseAI(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void perform() {
        //Pass
    }

    public List<Integer> getPathTo(int destX, int destY) {
        //TODO: Move cost and graph calculations out to once per turn
        Integer[] cost = new Integer[entity.gameMap.tiles.length];

        boolean[][] block = new boolean[entity.gameMap.width][entity.gameMap.height];

        for (Entity e : entity.gameMap.entities) {
            if (e.blocksMovement) {
                block[e.x][e.y] = true;
            }
        }

        for (int i = 0; i < entity.gameMap.tiles.length; i++) {
            Tile t = entity.gameMap.tiles[i];

            int x = i % entity.gameMap.width;
            int y = i / entity.gameMap.width;

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

        Node[] graph = Algorithm.buildGraph(entity.gameMap.width, entity.gameMap.height, cost);

        Node start = graph[entity.x + entity.y * entity.gameMap.width];

        Algorithm.calculateShortestPathFromSource(start);


        //TODO: Just return the index of the next location?

        List<Integer> path = new ArrayList<>();

        Node dest = graph[destX + destY * entity.gameMap.width];
        boolean skip = true;
        for (Node node : dest.getShortestPath()) {
            if (skip) {
                skip = false;
                continue;
            }
            path.add(node.getX() + node.getY() * entity.gameMap.width);
        }
        return path;
    }
}
