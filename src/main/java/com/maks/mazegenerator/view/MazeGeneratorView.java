package com.maks.mazegenerator.view;

import com.maks.mazegenerator.property.Maze;
import com.maks.mazegenerator.service.animation.AnimationService;
import com.maks.mazegenerator.service.animation.AnimationServiceImpl;
import com.maks.mazegenerator.util.AppUtils;
import com.maks.mazegenerator.viewmodel.MazeGeneratorViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.util.Pair;

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
    private Label exportErrorLbl;

    @FXML
    private TextField sizeInput;

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
    private final AnimationService animationService = new AnimationServiceImpl();

    private Double cellSideLength;

    @FXML
    void initialize() {
        initLifecycle();
        initViewModel();
        addObservers();
    }

    @FXML
    void onExport(ActionEvent event) {
        mazeGeneratorViewModel.handleExport(stage);
    }

    @FXML
    void onGenerate(ActionEvent event) {
        mazeGeneratorViewModel.handleGenerate(sizeInput.getText());
    }

    @FXML
    void onImport(ActionEvent event) {
        mazeGeneratorViewModel.handleImport(stage);
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
        mazeGeneratorViewModel.getMazeProperty().addListener((observable, oldValue, newValue) -> {
            cellSideLength = gridPane.getWidth() / newValue.size();
            showMaze(newValue);
        });
        mazeGeneratorViewModel.getGenerateErrorProperty().addListener((observable, oldValue, newValue) -> generateErrorLbl.setText(newValue));
        mazeGeneratorViewModel.getExportErrorProperty().addListener((observable, oldValue, newValue) -> exportErrorLbl.setText(newValue));
        mazeGeneratorViewModel.getEventHandlerDtoProperty().addListener((observable, oldValue, newValue) -> {
            if (stackPanes != null) {
                for (StackPane[] stackPanes : stackPanes) {
                    for (StackPane stackPane : stackPanes) {
                        if (newValue.clickOnPaneHandler() != null) {
                            stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, newValue.clickOnPaneHandler());
                        } else {
                            stackPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, oldValue.clickOnPaneHandler());
                        }
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
        mazeGeneratorViewModel.getAnimationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                if (oldValue.getTimeline() != null) {
                    oldValue.getTimeline().stop();
                }
                if (oldValue.getPathTransition() != null) {
                    oldValue.getPathTransition().stop();
                }
                Pair<Integer, Integer> startPoint = mazeGeneratorViewModel.getStartPointProperty().getValue();
                if (startPoint != null) {
                    stackPanes[startPoint.getKey()][startPoint.getValue()].getChildren().removeIf(e -> e instanceof ImageView);
                }
            } else {
                ImageView heroImageView = newValue.getImageView();
                heroImageView.setFitWidth(cellSideLength);
                heroImageView.setFitHeight(cellSideLength);
                Pair<Integer, Integer> startPoint = mazeGeneratorViewModel.getStartPointProperty().getValue();
                stackPanes[startPoint.getKey()][startPoint.getValue()].getChildren().add(heroImageView);
                animationService.runFrameAnimation(newValue);
                animationService.runTransitionAnimation(newValue, mazeGeneratorViewModel.getPathProperty().getValue().path(), cellSideLength, () -> {
                    mazeGeneratorViewModel.clearData();
                    stackPanes[startPoint.getKey()][startPoint.getValue()].getChildren().remove(heroImageView);
                });
            }
        });
    }

    private void showMaze(Maze maze) {
        initDefaultMazeState(maze);
        boolean[][] verticalWalls = maze.verticalWalls();
        boolean[][] horizontalWalls = maze.horizontalWalls();
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.size() - 1; j++) {
                if (verticalWalls[i][j]) {
                    StackPane pane = stackPanes[i][j];
                    Line line = new Line();
                    pane.getChildren().add(line);
                    StackPane.setAlignment(line, Pos.TOP_RIGHT);
                    if (i == maze.size() - 1) {
                        line.setEndY(cellSideLength - 2);
                    } else {
                        line.setEndY(cellSideLength - 1);
                    }
                }
            }
        }
        for (int i = 0; i < maze.size() - 1; i++) {
            for (int j = 0; j < maze.size(); j++) {
                if (horizontalWalls[i][j]) {
                    StackPane pane = stackPanes[i][j];
                    Line line = new Line();
                    pane.getChildren().add(line);
                    StackPane.setAlignment(line, Pos.BOTTOM_LEFT);
                    if (j == maze.size() - 1) {
                        line.setEndX(cellSideLength - 2);
                    } else {
                        line.setEndX(cellSideLength - 1);
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
        stackPanes = new StackPane[maze.size()][maze.size()];
        for (int i = 0; i < maze.size(); i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setMaxWidth(cellSideLength);
            gridPane.getColumnConstraints().add(column);
        }
        for (int i = 0; i < maze.size(); i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            row.setMaxHeight(cellSideLength);
            gridPane.getRowConstraints().add(row);
        }
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.size(); j++) {
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
