package com.maks.mazegenerator.viewmodel;

import com.maks.mazegenerator.handler.StackPaneEventHandler;
import com.maks.mazegenerator.lifecycle.Event;
import com.maks.mazegenerator.lifecycle.Lifecycle;
import com.maks.mazegenerator.property.EventHandlerDto;
import com.maks.mazegenerator.property.Maze;
import com.maks.mazegenerator.service.EilerMazeGenerator;
import com.maks.mazegenerator.service.MazeGenerator;
import com.maks.mazegenerator.service.SimpleBooleanGenerator;
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
import javafx.util.Pair;

import java.util.*;

public class MazeGeneratorViewModel extends AbstractViewModel {
    private final Property<Maze> mazeProperty = new SimpleObjectProperty<>();
    private final Property<String> generateErrorProperty = new SimpleStringProperty();
    private final Property<EventHandlerDto> eventHandlerDtoProperty = new SimpleObjectProperty<>();
    private final Property<Pair<Integer, Integer>> startPointProperty = new SimpleObjectProperty<>();
    private final Property<Pair<Integer, Integer>> endPointProperty = new SimpleObjectProperty<>();
    private final Property<String> pointErrorProperty = new SimpleStringProperty();
    private final Property<List<Pair<Integer, Integer>>> path = new SimpleObjectProperty<>();

    private final StackPaneEventHandler stackPaneEventHandler = new StackPaneEventHandler(startPointProperty, endPointProperty);
    private final MazeGenerator mazeGenerator = new EilerMazeGenerator(new SimpleBooleanGenerator());
    private final MazeIdConverter mazeIdConverter = new DefaultMazeIdConverter();
    private final GraphDirector graphDirector = new DefaultGraphDirector(new DefaultGraphBuilder(), mazeIdConverter);

    private final ChangeListener<? super Maze> mazeListener = (observable, oldValue, newValue) -> mazeIdConverter.setWidth(newValue.width());

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

    public void handleGenerate(String widthStr, String heightStr) {
        if (lifecycle.getCurrentEvent() == Event.START) {
            try {
                int width = Integer.parseInt(widthStr);
                int height = Integer.parseInt(heightStr);
                generateErrorProperty.setValue("");
                mazeProperty.setValue(mazeGenerator.generate(width, height));
                eventHandlerDtoProperty.setValue(new EventHandlerDto(stackPaneEventHandler));
            } catch (NumberFormatException e) {
                generateErrorProperty.setValue("Incorrect data");
            }
        }
    }

    public void handleImport() {

    }

    public void handlePass() {
        if (startPointProperty.getValue() == null || endPointProperty.getValue() == null) {
            pointErrorProperty.setValue("Input start and end points");
        } else {
            pointErrorProperty.setValue("");
            Graph graph = graphDirector.makeMazeGraph(mazeProperty.getValue(), startPointProperty.getValue());
            Deque<Integer> bfs = graph.bfs(mazeIdConverter.convert(endPointProperty.getValue()));
            path.setValue(bfs.stream().map(mazeIdConverter::retrieve).toList());
        }
    }

    public void clearData() {
        startPointProperty.setValue(null);
        endPointProperty.setValue(null);
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

    public Property<List<Pair<Integer, Integer>>> getPathProperty() {
        return path;
    }
}
