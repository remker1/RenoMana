import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Scheduler {
    public void start(Stage stage) {



        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll();

        Scene scene = new Scene(root, 500, 800);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}