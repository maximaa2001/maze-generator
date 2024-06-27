package com.maks.mazegenerator.service.graph.builder;

import com.maks.mazegenerator.service.graph.Graph;

public interface GraphBuilder {
    void buildNode(int id);
    void buildEdge(int from, int to);
    void buildRoot(int root);
    Graph build();
}
