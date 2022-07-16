package org.southy.rl.pathing;

import java.util.*;

public class Algorithm {

    public static Node[] buildGraph(int width, int height, Integer[] cost) {
        Node[] nodes = new Node[width * height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = x + y * width;
                Integer v = cost[i];
                if (v == null) {
                    continue;
                }

                Node node;
                if (nodes[i] == null) {
                    node = new Node(x, y);
                    nodes[i] = node;
                } else {
                    node = nodes[i];
                }

                //Left
                handleAdjacentNode(x - 1, y, width, node, cost, nodes, v);
                //Right
                handleAdjacentNode(x + 1, y, width, node, cost, nodes, v);
                //Up
                handleAdjacentNode(x, y - 1, width, node, cost, nodes, v);
                //Down
                handleAdjacentNode(x, y + 1, width, node, cost, nodes, v);
            }
        }

        return nodes;
    }

    private static void handleAdjacentNode(int x, int y, int width, Node node, Integer[] cost, Node[] nodes, int currentCost) {
        int i = x + y * width;
        Integer v = cost[i];
        if (v == null) {
            return;
        }

        Node adjacent;
        if (nodes[i] == null) {
            adjacent = new Node(x, y);
            nodes[i] = adjacent;
        } else {
            adjacent = nodes[i];
        }

        adjacent.addDestination(node, currentCost);
        node.addDestination(adjacent, v);
    }

    public static void calculateShortestPathFromSource(Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            for (Map.Entry<Node, Integer> entry : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = entry.getKey();
                Integer edgeWeight = entry.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
    }

    private static Node getLowestDistanceNode(Set < Node > unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

}
