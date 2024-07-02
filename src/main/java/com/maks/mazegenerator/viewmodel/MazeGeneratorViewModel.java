package com.maks.mazegenerator.viewmodel;

import com.maks.mazegenerator.entity.AnimationHero;
import com.maks.mazegenerator.entity.Pacman;
import com.maks.mazegenerator.exception.TransportException;
import com.maks.mazegenerator.handler.StackPaneEventHandler;
import com.maks.mazegenerator.lifecycle.Event;
import com.maks.mazegenerator.lifecycle.Lifecycle;
import com.maks.mazegenerator.property.EventHandlerDto;
import com.maks.mazegenerator.property.Maze;
import com.maks.mazegenerator.property.PathProperty;
import com.maks.mazegenerator.service.EulerMazeGenerator;
import com.maks.mazegenerator.service.MazeGenerator;
import com.maks.mazegenerator.service.SimpleBooleanGenerator;
import com.maks.mazegenerator.service.file.MazeTransporter;
import com.maks.mazegenerator.service.file.MazeTransporterImpl;
import com.maks.mazegenerator.service.graph.Graph;
import com.maks.mazegenerator.service.graph.builder.DefaultGraphBuilder;
import com.maks.mazegenerator.service.graph.builder.DefaultGraphDirector;
import com.maks.mazegenerator.service.graph.builder.GraphDirector;
import com.maks.mazegenerator.service.graph.id.DefaultMazeIdConverter;
import com.maks.mazegenerator.service.graph.id.MazeIdConverter;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.*;

public class MazeGeneratorViewModel extends AbstractViewModel {
    private final AnimationHero hero = new Pacman();

    private final Property<Maze> mazeProperty = new SimpleObjectProperty<>();
    private final Property<String> exportErrorProperty = new SimpleStringProperty();
    private final Property<String> generateErrorProperty = new SimpleStringProperty();
    private final Property<EventHandlerDto> eventHandlerDtoProperty = new SimpleObjectProperty<>();
    private final Property<Pair<Integer, Integer>> startPointProperty = new SimpleObjectProperty<>();
    private final Property<Pair<Integer, Integer>> endPointProperty = new SimpleObjectProperty<>();
    private final Property<String> pointErrorProperty = new SimpleStringProperty();
    private final Property<PathProperty> pathProperty = new SimpleObjectProperty<>();
    private final Property<AnimationHero> animationProperty = new SimpleObjectProperty<>(hero);

    private final MazeGenerator mazeGenerator = new EulerMazeGenerator(new SimpleBooleanGenerator());
    private final MazeIdConverter mazeIdConverter = new DefaultMazeIdConverter();
    private final GraphDirector graphDirector = new DefaultGraphDirector(new DefaultGraphBuilder(), mazeIdConverter);
    private final MazeTransporter mazeTransporter = new MazeTransporterImpl();

    private final ChangeListener<? super Maze> mazeListener = (observable, oldValue, newValue) -> mazeIdConverter.setWidth(newValue.size());

    public MazeGeneratorViewModel(Lifecycle lifecycle) {
        super(lifecycle);
    }

    @Override
    protected void handleStart(Event event) {
        super.handleStart(event);
        mazeProperty.addListener(mazeListener);
    }

    @Override
    protected void handleStop(Event event) {
        super.handleStop(event);
        mazeProperty.removeListener(mazeListener);
    }

    public void handleGenerate(String sizeStr) {
        if (lifecycle.getCurrentEvent() == Event.START) {
            try {
                int size = Integer.parseInt(sizeStr);
                if (size > 0) {
                    generateErrorProperty.setValue("");
                    animationProperty.setValue(null);
                    startPointProperty.setValue(null);
                    endPointProperty.setValue(null);
                    mazeProperty.setValue(mazeGenerator.generate(size));
                    eventHandlerDtoProperty.setValue(new EventHandlerDto(new StackPaneEventHandler(startPointProperty, endPointProperty)));
                } else {
                    generateErrorProperty.setValue("Incorrect data");
                }
            } catch (NumberFormatException e) {
                generateErrorProperty.setValue("Incorrect data");
            }
        }
    }

    public void handleExport(Stage primaryStage) {
        if (mazeProperty.getValue() == null) {
            exportErrorProperty.setValue("Initialize maze at first");
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Maze");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                try {
                    mazeTransporter.exportMaze(mazeProperty.getValue(), file);
                    exportErrorProperty.setValue("");
                } catch (TransportException e) {
                    exportErrorProperty.setValue("Filed to export maze");
                }
            }
        }
    }

    public void handleImport(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Maze");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                Maze maze = mazeTransporter.importMaze(file);
                exportErrorProperty.setValue("");
                animationProperty.setValue(null);
                startPointProperty.setValue(null);
                endPointProperty.setValue(null);
                mazeProperty.setValue(maze);
                eventHandlerDtoProperty.setValue(new EventHandlerDto(new StackPaneEventHandler(startPointProperty, endPointProperty)));
            } catch (TransportException e) {
                exportErrorProperty.setValue("Filed to import maze");
            }
        }
    }

    public void handlePass() {
        if (startPointProperty.getValue() == null || endPointProperty.getValue() == null) {
            pointErrorProperty.setValue("Input start and end points");
        } else {
            pointErrorProperty.setValue("");
            Graph graph = graphDirector.makeMazeGraph(mazeProperty.getValue(), startPointProperty.getValue());
            Deque<Integer> bfs = graph.bfs(mazeIdConverter.convert(endPointProperty.getValue()));
            pathProperty.setValue(new PathProperty(bfs.stream().map(mazeIdConverter::retrieve).toList()));
            eventHandlerDtoProperty.setValue(new EventHandlerDto(null));
            animationProperty.setValue(hero);
        }
    }

    public void clearData() {
        animationProperty.setValue(null);
        startPointProperty.setValue(null);
        endPointProperty.setValue(null);
        eventHandlerDtoProperty.setValue(new EventHandlerDto(new StackPaneEventHandler(startPointProperty, endPointProperty)));
    }

    public Property<Maze> getMazeProperty() {
        return mazeProperty;
    }

    public Property<String> getGenerateErrorProperty() {
        return generateErrorProperty;
    }

    public Property<EventHandlerDto> getEventHandlerDtoProperty() {
        return eventHandlerDtoProperty;
    }

    public Property<Pair<Integer, Integer>> getStartPointProperty() {
        return startPointProperty;
    }

    public Property<Pair<Integer, Integer>> getEndPointProperty() {
        return endPointProperty;
    }

    public Property<String> getPointErrorProperty() {
        return pointErrorProperty;
    }

    public Property<PathProperty> getPathProperty() {
        return pathProperty;
    }

    public Property<AnimationHero> getAnimationProperty() {
        return animationProperty;
    }

    public Property<String> getExportErrorProperty() {
        return exportErrorProperty;
    }
}
