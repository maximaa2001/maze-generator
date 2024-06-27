package com.maks.mazegenerator.service.graph.id;

import javafx.util.Pair;

public class DefaultMazeIdConverter implements MazeIdConverter {
    private int width;

    @Override
    public int convert(Pair<Integer, Integer> oldId) {
        return oldId.getKey() * width + oldId.getValue();
    }

    @Override
    public Pair<Integer, Integer> retrieve(int newId) {
        if (newId < width) {
            return new Pair<>(0, newId);
        }
        int row = newId / width;
        int column = newId - row * width;
        return new Pair<>(row, column);
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }
}
