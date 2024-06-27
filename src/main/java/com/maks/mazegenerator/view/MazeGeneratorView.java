package com.maks.mazegenerator.view;

import com.maks.mazegenerator.property.Maze;
import com.maks.mazegenerator.util.AppUtils;
import com.maks.mazegenerator.viewmodel.MazeGeneratorViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.*;

public class MazeGeneratorView extends AbstractView {

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

    @FXML
    private Button passBtn;

    @FXML
    private Label pointErrorLbl;

    @FXML
    private TextField widthInput;

    @FXML
    private TextField heightInput;

    @FXML
    private Label generateErrorLbl;

    @FXML
    private CheckBox startCheckBox;

    @FXML
    private CheckBox endCheckBox;

    @FXML
    private Button clearPointsBtn;

    private StackPane[][] stackPanes;

    private MazeGeneratorViewModel mazeGeneratorViewModel;

    @FXML
    void initialize() {
        initLifecycle();
        initViewModel();
        addObservers();
    }

    @FXML
    void onExport(ActionEvent event) {

    }

    @FXML
    void onGenerate(ActionEvent event) {
        mazeGeneratorViewModel.handleGenerate(widthInput.getText(), heightInput.getText());
    }

    @FXML
    void onImport(ActionEvent event) {

    }

    @FXML
    void onPass(ActionEvent event) {
        mazeGeneratorViewModel.handlePass();
    }

    @FXML
    void onClearData(ActionEvent event) {
        mazeGeneratorViewModel.clearData();
    }

    private void addObservers() {
        mazeGeneratorViewModel.getMazeProperty().addListener((observable, oldValue, newValue) -> showMaze(newValue));
        mazeGeneratorViewModel.getGenerateErrorProperty().addListener((observable, oldValue, newValue) -> generateErrorLbl.setText(newValue));
        mazeGeneratorViewModel.getEventHandlerDtoProperty().addListener((observable, oldValue, newValue) -> {
            for (StackPane[] stackPanes : stackPanes) {
                for (StackPane stackPane : stackPanes) {
                    if (newValue.clickOnPaneHandler() != null) {
                        stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, newValue.clickOnPaneHandler());
                    } else {
                        stackPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, oldValue.clickOnPaneHandler());
                    }
                }
            }
        });
        mazeGeneratorViewModel.getStartPointProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                stackPanes[oldValue.getKey()][oldValue.getValue()].setStyle("");
            }
            if (newValue != null) {
                stackPanes[newValue.getKey()][newValue.getValue()].setStyle("-fx-background-color: green");
                startCheckBox.setSelected(true);
            } else {
                startCheckBox.setSelected(false);
            }
        });
        mazeGeneratorViewModel.getEndPointProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                stackPanes[oldValue.getKey()][oldValue.getValue()].setStyle("");
            }
            if (newValue != null) {
                stackPanes[newValue.getKey()][newValue.getValue()].setStyle("-fx-background-color: red");
                endCheckBox.setSelected(true);
            } else {
                endCheckBox.setSelected(false);
            }
        });
        mazeGeneratorViewModel.getPointErrorProperty().addListener((observable, oldValue, newValue) -> pointErrorLbl.setText(newValue));
        mazeGeneratorViewModel.getPathProperty().addListener((observable, oldValue, newValue) -> {
            for (int i = 0; i < newValue.size(); i++) {
                Label label = new Label(String.valueOf(i));
                stackPanes[newValue.get(i).getKey()][newValue.get(i).getValue()].getChildren().add(label);
            }
//            newValue.stream()
//                    .skip(1)
//                    .forEach(e -> {
//                        stackPanes[e.getKey()][e.getValue()].setStyle("-fx-border-color: #DEFAFF");
//                    });
        });
    }

    private void showMaze(Maze maze) {
        initDefaultMazeState(maze);
        boolean[][] verticalWalls = maze.verticalWalls();
        boolean[][] horizontalWalls = maze.horizontalWalls();
        for (int i = 0; i < maze.height(); i++) {
            for (int j = 0; j < maze.width() - 1; j++) {
                if (verticalWalls[i][j]) {
                    StackPane pane = stackPanes[i][j];
                    Line line = new Line();
                    pane.getChildren().add(line);
                    StackPane.setAlignment(line, Pos.TOP_RIGHT);
                    if (i == maze.height() - 1) {
                        line.setEndY(gridPane.getHeight() / maze.height() - 2);
                    } else {
                        line.setEndY(gridPane.getHeight() / maze.height() - 1);
                    }
                }
            }
        }
        for (int i = 0; i < maze.height() - 1; i++) {
            for (int j = 0; j < maze.width(); j++) {
                if (horizontalWalls[i][j]) {
                    StackPane pane = stackPanes[i][j];
                    Line line = new Line();
                    pane.getChildren().add(line);
                    StackPane.setAlignment(line, Pos.BOTTOM_LEFT);
                    if (j == maze.width() - 1) {
                        line.setEndX(gridPane.getWidth() / maze.width() - 2);
                    } else {
                        line.setEndX(gridPane.getWidth() / maze.width() - 1);
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
        stackPanes = new StackPane[maze.height()][maze.width()];
        for (int i = 0; i < maze.width(); i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setMaxWidth(gridPane.getWidth() / maze.width());
            gridPane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < maze.height(); i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            row.setMaxHeight(gridPane.getHeight() / maze.height());
            gridPane.getRowConstraints().add(row);
        }
        for (int i = 0; i < maze.height(); i++) {
            for (int j = 0; j < maze.width(); j++) {
                StackPane pane = new StackPane();
                pane.setUserData(AppUtils.generatePaneData(i, j));
                stackPanes[i][j] = pane;
                gridPane.add(pane, j, i);
            }
        }
    }

    @Override
    void initViewModel() {
        mazeGeneratorViewModel = new MazeGeneratorViewModel(lifecycle);
    }
}


