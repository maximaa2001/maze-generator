package com.maks.mazegenerator.service.graph.builder;

import com.maks.mazegenerator.property.Maze;
import com.maks.mazegenerator.service.graph.Graph;
import javafx.util.Pair;

public interface GraphDirector {
    Graph makeMazeGraph(Maze maze, Pair<Integer, Integer> root);
    void setBuilder(GraphBuilder graphBuilder);
}
