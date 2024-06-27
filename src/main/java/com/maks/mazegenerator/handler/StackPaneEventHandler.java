package com.maks.mazegenerator.handler;

import com.maks.mazegenerator.util.AppUtils;
import javafx.beans.property.Property;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;

public class StackPaneEventHandler implements EventHandler<MouseEvent> {

    private final Property<Pair<Integer, Integer>> startPointProperty;
    private final Property<Pair<Integer, Integer>> endPointProperty;

    public StackPaneEventHandler(Property<Pair<Integer, Integer>> startPointProperty,
                                 Property<Pair<Integer, Integer>> endPointProperty) {

        this.startPointProperty = startPointProperty;
        this.endPointProperty = endPointProperty;
    }

    @Override
    public void handle(MouseEvent event) {
        Object source = event.getSource();
        if (source instanceof StackPane stackPane) {
            String data = (String) stackPane.getUserData();
            int row = AppUtils.resolvePaneRow(data);
            int column = AppUtils.resolvePaneColumn(data);
            Pair<Integer, Integer> pair = new Pair<>(row, column);
            if (startPointProperty.getValue() == null) {
                startPointProperty.setValue(pair);
            } else if (endPointProperty.getValue() == null) {
                if (!startPointProperty.getValue().equals(pair)) {
                    endPointProperty.setValue(pair);
                }
            } else {
                if (!startPointProperty.getValue().equals(pair) && !endPointProperty.getValue().equals(pair)) {
                    startPointProperty.setValue(pair);
                    endPointProperty.setValue(null);
                }
            }
        }
    }
}
