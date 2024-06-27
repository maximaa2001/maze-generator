package com.maks.mazegenerator.service.graph.builder;

import com.maks.mazegenerator.service.graph.Graph;

public class DefaultGraphBuilder implements GraphBuilder {
    private Graph graph;

    @Override
    public void buildNode(int id) {
        graph.addNode(id);
    }

    @Override
    public void buildEdge(int from, int to) {
        graph.addEdge(from, to);
    }

    @Override
    public void buildRoot(int root) {
        graph = new Graph(root);
    }

    @Override
    public Graph build() {
        return graph;
    }
}
