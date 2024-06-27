package com.maks.mazegenerator.util;

import com.maks.mazegenerator.MainApplication;
import com.maks.mazegenerator.lifecycle.Event;
import com.maks.mazegenerator.lifecycle.LifecycleProvider;
import com.maks.mazegenerator.view.AbstractView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class Forwarder {
    private static final LifecycleProvider lifecycleProvider = LifecycleProvider.getInstance();

    public static void wrapStage(Stage stage) {
        stage.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                lifecycleProvider.getLifecycle(oldValue).registerEvent(Event.STOP);
                lifecycleProvider.removeLifecycle(oldValue);
            }
            lifecycleProvider.getLifecycle(newValue).registerEvent(Event.START);
        });
    }

    public static void changeScene(Stage stage, String fxmlName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlName));
            Scene scene = new Scene(fxmlLoader.load(), stage.getWidth(), stage.getHeight());
            AbstractView view = fxmlLoader.getController();
            view.setStage(stage);
            view.setScene(scene);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
