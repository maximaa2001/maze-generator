package com.maks.mazegenerator.service;

import com.maks.mazegenerator.entity.Maze;

public class EilerMazeGenerator implements MazeGenerator {
    private final int width;
    private final int height;
    private final boolean[][] verticalWalls;
    private final boolean[][] horizontalWalls;
    private final BooleanGenerator booleanGenerator;
    private int nextGroupNumber = 1;

    public EilerMazeGenerator(int width, int height, BooleanGenerator booleanGenerator) {
        this.width = width;
        this.height = height;
        verticalWalls = new boolean[height][width];
        horizontalWalls = new boolean[height][width];
        this.booleanGenerator = booleanGenerator;
    }

    public EilerMazeGenerator(int size, BooleanGenerator booleanGenerator) {
        this(size, size, booleanGenerator);
    }

    @Override
    public Maze generate() {
        int[][] groupNumberMatrix = new int[height][width];
        resetNextGroupNumber();
        generateGroupNumbersForFirstLine(groupNumberMatrix);
        for (int i = 0; i < height; i++) {
            installRightWalls(groupNumberMatrix, i);
            installBottomWalls(groupNumberMatrix, i);
            if (needNextLine(i)) {
                addNewLine(groupNumberMatrix, i, i + 1);
            } else {
                finishLastLine(groupNumberMatrix, i);
            }
        }
        return new Maze(width, height, verticalWalls, horizontalWalls);
    }

    private void resetNextGroupNumber() {
        nextGroupNumber = 1;
    }

    private int generateNextGroupNumber() {
        return nextGroupNumber++;
    }

    private void generateGroupNumbersForFirstLine(int[][] groupNumberMatrix) {
        for (int i = 0; i < groupNumberMatrix[0].length; i++) {
            groupNumberMatrix[0][i] = generateNextGroupNumber();
        }
    }

    private void installRightWalls(int[][] groupNumberMatrix, int row) {
        for (int i = 0; i < groupNumberMatrix[row].length - 1; i++) {
            boolean needToInstallRightWall = booleanGenerator.random();
            if (needToInstallRightWall) {
                verticalWalls[row][i] = true;
            } else if (compareWithGroupNumberOnRight(groupNumberMatrix, row, i)) {
                verticalWalls[row][i] = true;
            } else {
                combineGroups(groupNumberMatrix, row, i);
            }
        }
        verticalWalls[row][width - 1] = true;
    }

    private boolean compareWithGroupNumberOnRight(int[][] groupNumberMatrix, int row, int column) {
        return groupNumberMatrix[row][column] == groupNumberMatrix[row][column + 1];
    }

    private void combineGroups(int[][] groupNumberMatrix, int row, int column) {
        int mainGroup = groupNumberMatrix[row][column];
        int groupToCombine = groupNumberMatrix[row][column + 1];
        for (int i = 0; i < groupNumberMatrix[row].length; i++) {
            if (groupNumberMatrix[row][i] == groupToCombine) {
                groupNumberMatrix[row][i] = mainGroup;
            }
        }
    }

    private void installBottomWalls(int[][] groupNumberMatrix, int row) {
        for (int i = 0; i < groupNumberMatrix[row].length; i++) {
            boolean needToInstallBottomWall = booleanGenerator.random();
            if (needToInstallBottomWall) {
                if (resolveCellsWithoutBottomWallCountInGroup(groupNumberMatrix, row, groupNumberMatrix[row][i]) > 1) {
                    horizontalWalls[row][i] = true;
                }
            }
        }
    }

    private int resolveCellsWithoutBottomWallCountInGroup(int[][] groupNumberMatrix, int row, int groupNumber) {
        int cellsWithoutBottomWallCount = 0;
        for (int i = 0; i < groupNumberMatrix[row].length; i++) {
            if (groupNumberMatrix[row][i] == groupNumber && !horizontalWalls[row][i]) {
                cellsWithoutBottomWallCount++;
            }
        }
        return cellsWithoutBottomWallCount;
    }

    private void addNewLine(int[][] groupNumberMatrix, int prevRow, int newRow) {
        for (int i = 0; i < groupNumberMatrix[newRow].length; i++) {
            groupNumberMatrix[newRow][i] = groupNumberMatrix[prevRow][i];
        }
        for (int i = 0; i < horizontalWalls[prevRow].length; i++) {
            if (horizontalWalls[prevRow][i]) {
                groupNumberMatrix[newRow][i] = generateNextGroupNumber();
            }
        }
    }

    private void finishLastLine(int[][] groupNumberMatrix, int row) {
        for (int i = 0; i < groupNumberMatrix[row].length - 1; i++) {
            horizontalWalls[row][i] = true;
            if (!compareWithGroupNumberOnRight(groupNumberMatrix, row, i)) {
                verticalWalls[row][i] = false;
                combineGroups(groupNumberMatrix, row, i);
            }
        }
        horizontalWalls[row][width - 1] = true;
    }

    private boolean needNextLine(int currentLine) {
        return currentLine + 1 < height;
    }
}
