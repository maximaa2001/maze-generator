module com.maks.maze.mazegenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.maks.mazegenerator to javafx.fxml;
    exports com.maks.mazegenerator;
}