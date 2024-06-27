package com.maks.mazegenerator;

import com.maks.mazegenerator.util.Forwarder;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        Forwarder.wrapStage(stage);
        Forwarder.changeScene(stage, "maze-generator.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}
