package com.maks.mazegenerator.service;

import com.maks.mazegenerator.property.Maze;

public interface MazeGenerator {
    Maze generate(int width, int height);
}
