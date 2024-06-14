package com.maks.mazegenerator;

import com.maks.mazegenerator.entity.Maze;
import com.maks.mazegenerator.service.EilerMazeGenerator;
import com.maks.mazegenerator.service.MazeGenerator;
import com.maks.mazegenerator.service.SimpleBooleanGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

public class MazeGeneratorController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exportBtn;

    @FXML
    private Button generateBtn;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button importBtn;

    private final int GRID_WIDTH = 790;
    private final int GRID_HEIGHT = 790;

    private StackPane[][] stackPanes;

    @FXML
    void initialize() {

    }

    @FXML
    void onExport(ActionEvent event) {

    }

    @FXML
    void onGenerate(ActionEvent event) {
        MazeGenerator mazeGenerator = new EilerMazeGenerator(6, new SimpleBooleanGenerator());
        Maze maze = mazeGenerator.generate();
        showMaze(maze);
    }

    @FXML
    void onImport(ActionEvent event) {

    }

    private void showMaze(Maze maze) {
        initDefaultMazeState(maze);
        boolean[][] verticalWalls = maze.getVerticalWalls();
        boolean[][] horizontalWalls = maze.getHorizontalWalls();
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth() - 1; j++) {
                if (verticalWalls[i][j]) {
                    StackPane pane = stackPanes[i][j];
                    Line line = new Line();
                    pane.getChildren().add(line);
                    StackPane.setAlignment(line, Pos.TOP_RIGHT);
                    if (i == maze.getHeight() - 1) {
                        line.setEndY((double) GRID_HEIGHT / maze.getHeight() - 2);
                    } else {
                        line.setEndY((double) GRID_HEIGHT / maze.getHeight() - 1);
                    }
                }
            }
        }
        for (int i = 0; i < maze.getHeight() - 1; i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                if (horizontalWalls[i][j]) {
                    StackPane pane = stackPanes[i][j];
                    Line line = new Line();
                    pane.getChildren().add(line);
                    StackPane.setAlignment(line, Pos.BOTTOM_LEFT);
                    if (j == maze.getWidth() - 1) {
                        line.setEndX((double) GRID_WIDTH / maze.getWidth() - 2);
                    } else {
                        line.setEndX((double) GRID_WIDTH / maze.getWidth() - 1);
                    }
                }
            }
        }
    }

    private void initDefaultMazeState(Maze maze) {
        if (stackPanes != null) {
            gridPane.getColumnConstraints().clear();
            gridPane.getRowConstraints().clear();
            for (StackPane[] stackPanes : stackPanes) {
                for (StackPane stackPane : stackPanes) {
                    stackPane.getChildren().clear();
                }
            }
        }
        stackPanes = new StackPane[maze.getHeight()][maze.getWidth()];
        for (int i = 0; i < maze.getWidth(); i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setMaxWidth((double) GRID_WIDTH / maze.getWidth());
            gridPane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < maze.getHeight(); i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            row.setMaxHeight((double) GRID_HEIGHT / maze.getHeight());
            gridPane.getRowConstraints().add(row);
        }
        int t = 0;
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                StackPane pane = new StackPane();
//                if(t == 0) {
//                    pane.setStyle("-fx-background-color: yellow");
//                    t++;
//                } else if(t == 1) {
//                    pane.setStyle("-fx-background-color: green");
//                    t++;
//                } else {
//                    pane.setStyle("-fx-background-color: blue");
//                    t = 0;
//                }
                stackPanes[i][j] = pane;
                gridPane.add(pane, j, i);
            }
        }
    }

}
