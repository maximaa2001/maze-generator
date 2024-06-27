package com.maks.mazegenerator.service.graph.id;

import javafx.util.Pair;

public interface MazeIdConverter extends IdConverter<Pair<Integer, Integer>> {
    void setWidth(int width);
}
