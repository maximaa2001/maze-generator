module com.maks.maze.mazegenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;


    opens com.maks.mazegenerator to javafx.fxml;
    exports com.maks.mazegenerator;
    exports com.maks.mazegenerator.lifecycle;
    opens com.maks.mazegenerator.lifecycle to javafx.fxml;
    exports com.maks.mazegenerator.viewmodel;
    opens com.maks.mazegenerator.viewmodel to javafx.fxml;
    exports com.maks.mazegenerator.view;
    opens com.maks.mazegenerator.view to javafx.fxml;
}