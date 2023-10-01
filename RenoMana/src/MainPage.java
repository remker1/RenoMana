/**
 * MainPage Class
 * <p>
 * The application has a user interface with several tab where each tab shows a different feature
 * or part of the application. By putting the content of each tab into its own class, the
 * application makes it easier to change and maintain. The content of each tab is made and
 * put into place in its own class. This method lets developers work on single features at a time,
 * which makes the codebase more organised and easier to use.
 *
 * @author Jewel Magcawas
 * @version 1.0
 * @since 2023-08-01
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.TabPane;
import javafx.scene.Scene;

public class MainPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        Scene scene = new Scene(tabPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("[COMPANY HOMEPAGE]");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}