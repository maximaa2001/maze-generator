package com.maks.mazegenerator.service.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GraphNode {
    private final int id;
    private final List<GraphNode> graphNodes = new ArrayList<>();

    public GraphNode(int id) {
        this.id = id;
    }

    public void addNode(GraphNode node) {
        graphNodes.add(node);
    }

    public int getId() {
        return id;
    }

    public List<GraphNode> getGraphNodes() {
        return graphNodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraphNode graphNode)) return false;
        return id == graphNode.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
