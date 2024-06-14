package com.maks.mazegenerator.entity;

public class Maze {
    private final int width;
    private final int height;
    private final boolean[][] verticalWalls;
    private final boolean[][] horizontalWalls;

    public Maze(int width, int height, boolean[][] verticalWalls, boolean[][] horizontalWalls) {
        this.width = width;
        this.height = height;
        this.verticalWalls = verticalWalls;
        this.horizontalWalls = horizontalWalls;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean[][] getVerticalWalls() {
        return verticalWalls;
    }

    public boolean[][] getHorizontalWalls() {
        return horizontalWalls;
    }
}
