package com.maks.mazegenerator.service.file;

import com.maks.mazegenerator.exception.TransportException;
import com.maks.mazegenerator.property.Maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class MazeTransporterImpl implements MazeTransporter {
    @Override
    public void exportMaze(Maze maze, File file) throws TransportException {
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file))) {
            osw.write(createData(maze));
        } catch (IOException e) {
            throw new TransportException("Failed to export maze");
        }
    }

    @Override
    public Maze importMaze(File file) throws TransportException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int size = Integer.parseInt(br.readLine());
            br.readLine();
            String line;
            boolean[][] verticalWalls = new boolean[size][size];
            for (int i = 0; i < size; i++) {
                line = br.readLine();
                List<String> list = Arrays.stream(line.split(" +")).toList();
                for (int j = 0; j < list.size(); j++) {
                    verticalWalls[i][j] = Boolean.parseBoolean(list.get(j));
                }
            }
            br.readLine();
            boolean[][] horizontalWalls = new boolean[size][size];
            for (int i = 0; i < size; i++) {
                line = br.readLine();
                List<String> list = Arrays.stream(line.split(" +")).toList();
                for (int j = 0; j < list.size(); j++) {
                    horizontalWalls[i][j] = Boolean.parseBoolean(list.get(j));
                }
            }
            return new Maze(size, verticalWalls, horizontalWalls);
        } catch (Exception e) {
            throw new TransportException("Failed to export maze");
        }
    }

    private String createData(Maze maze) {
        StringBuilder builder = new StringBuilder();
        builder.append(maze.size());
        builder.append("\n");
        builder.append("\n");
        boolean[][] verticalWalls = maze.verticalWalls();
        for (boolean[] verticalWall : verticalWalls) {
            for (boolean b : verticalWall) {
                builder.append(b);
                System.out.println();
                builder.append(" ");
            }
            builder.append("\n");
        }
        builder.append("\n");
        boolean[][] horizontalWalls = maze.horizontalWalls();
        for (boolean[] horizontalWall : horizontalWalls) {
            for (boolean b : horizontalWall) {
                builder.append(b);
                builder.append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
