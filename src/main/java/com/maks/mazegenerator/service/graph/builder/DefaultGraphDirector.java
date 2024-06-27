package com.maks.mazegenerator.service.graph.builder;

import com.maks.mazegenerator.property.Maze;
import com.maks.mazegenerator.service.graph.Graph;
import com.maks.mazegenerator.service.graph.id.MazeIdConverter;
import javafx.util.Pair;

public class DefaultGraphDirector implements GraphDirector {
    private final MazeIdConverter mazeIdConverter;
    private GraphBuilder graphBuilder;

    public DefaultGraphDirector(GraphBuilder graphBuilder, MazeIdConverter mazeIdConverter) {
        this.graphBuilder = graphBuilder;
        this.mazeIdConverter = mazeIdConverter;
    }

    @Override
    public Graph makeMazeGraph(Maze maze, Pair<Integer, Integer> root) {
        int height = maze.height();
        int width = maze.width();
        boolean[][] verticalWalls = maze.verticalWalls();
        boolean[][] horizontalWalls = maze.horizontalWalls();
        graphBuilder.buildRoot(mazeIdConverter.convert(root));
        createNodes(height, width);
        createEdges(height, width, verticalWalls, horizontalWalls);
        return graphBuilder.build();
    }

    @Override
    public void setBuilder(GraphBuilder graphBuilder) {
        this.graphBuilder = graphBuilder;
    }

    private void createNodes(int height, int width) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                graphBuilder.buildNode(mazeIdConverter.convert(new Pair<>(i, j)));
            }
        }
    }

    private void createEdges(int height, int width, boolean[][] verticalWalls, boolean[][] horizontalWalls) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!existsRightBorder(i, j, width, verticalWalls)) {
                    graphBuilder.buildEdge(mazeIdConverter.convert(new Pair<>(i, j)), mazeIdConverter.convert(new Pair<>(i, j + 1)));
                }
                if (!existsBottomBorder(i, j, width, horizontalWalls)) {
                    graphBuilder.buildEdge(mazeIdConverter.convert(new Pair<>(i, j)), mazeIdConverter.convert(new Pair<>(i + 1, j)));
                }
                if (!existsLeftBorder(i, j, verticalWalls)) {
                    graphBuilder.buildEdge(mazeIdConverter.convert(new Pair<>(i, j)), mazeIdConverter.convert(new Pair<>(i, j - 1)));
                }
                if (!existsTopBorder(i, j, horizontalWalls)) {
                    graphBuilder.buildEdge(mazeIdConverter.convert(new Pair<>(i, j)), mazeIdConverter.convert(new Pair<>(i - 1, j)));
                }
            }
        }
    }

    private boolean existsRightBorder(int row, int column, int width, boolean[][] verticalWalls) {
        if (column == width - 1) {
            return true;
        }
        return verticalWalls[row][column];
    }

    private boolean existsBottomBorder(int row, int column, int width, boolean[][] horizontalWalls) {
        if (row == width - 1) {
            return true;
        }
        return horizontalWalls[row][column];
    }

    private boolean existsLeftBorder(int row, int column, boolean[][] verticalWalls) {
        if (column == 0) {
            return true;
        }
        return verticalWalls[row][column - 1];
    }

    private boolean existsTopBorder(int row, int column, boolean[][] horizontalWalls) {
        if (row == 0) {
            return true;
        }
        return horizontalWalls[row - 1][column];
    }
}
