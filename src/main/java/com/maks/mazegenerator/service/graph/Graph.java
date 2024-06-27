package com.maks.mazegenerator.service.graph;

import java.util.*;

public class Graph {
    private final Integer rootId;
    private final Map<Integer, List<Integer>> node2Edges = new HashMap<>();

    public Graph(Integer rootId) {
        this.rootId = rootId;
    }

    public Integer getRootId() {
        return rootId;
    }

    public void addNode(int id) {
        node2Edges.put(id, new ArrayList<>());
    }

    public void addEdge(int from, int to) {
        List<Integer> edges = node2Edges.get(from);
        if (edges == null) {
            throw new IllegalStateException();
        }
        edges.add(to);
    }

    public Deque<Integer> bfs(int sourceId) {
        if (rootId == sourceId) {
            return new ArrayDeque<>(List.of(rootId));
        }
        Map<Integer, Boolean> visitedNodes = new HashMap<>();
        Map<Integer, Integer> graphPath = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(rootId);
        while (!queue.isEmpty()) {
            Integer nodeId = queue.remove();
            if (nodeId == sourceId) {
                return resolvePath(graphPath, sourceId);
            }
            visitedNodes.put(nodeId, true);
            for (Integer childId : node2Edges.get(nodeId)) {
                if (!visitedNodes.containsKey(childId)) {
                    queue.add(childId);
                    graphPath.put(childId, nodeId);
                }
            }
        }
        return null;
    }

    private Deque<Integer> resolvePath(Map<Integer, Integer> graphPath, int sourceId) {
        Deque<Integer> path = new ArrayDeque<Integer>();
        do {
            path.addFirst(sourceId);
        } while ((sourceId = graphPath.get(sourceId)) != rootId);
        path.addFirst(rootId);
        return path;
    }
}
