package com.maks.mazegenerator.service.file;

import com.maks.mazegenerator.exception.TransportException;
import com.maks.mazegenerator.property.Maze;

import java.io.File;

public interface MazeTransporter {
    void exportMaze(Maze maze, File file) throws TransportException;
    Maze importMaze(File file) throws TransportException;
}
