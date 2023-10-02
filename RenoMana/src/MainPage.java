/**
 * MainPage Class
 *
 * <p>
 * The application has a user interface with several tab where each tab shows a different feature
 * or part of the application. By putting the content of each tab into its own class, the
 * application makes it easier to change and maintain. The content of each tab is made and
 * put into place in its own class. This method lets developers work on single features at a time,
 * which makes the codebase more organised and easier to use.
 * <p>
 *
 * @author Jewel Magcawas
 * @version 1.0
 * @since 2023-08-01
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TabPane;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;

public class MainPage extends Application {

    @Override
    public void start(Stage stage) {
        // The set-ups of pane and scene. Additionally, creating an account profile with an event when clicked.
        TabPane tabPane = new TabPane();
        VBox mainLayout = new VBox();
        Scene scene = new Scene(mainLayout, 2000, 600);

        Label accountName = new Label("[ACCOUNT NAME]");
        Circle profileCircle = new Circle(30);
        profileCircle.setOnMouseClicked(event -> openProfileWindow());

        // Add the circle to the top right corner of the main window
        HBox topBar = new HBox(20, accountName, profileCircle);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(accountName, javafx.scene.layout.Priority.ALWAYS); // Makes the account name push other elements to the right
        mainLayout.getChildren().addAll(topBar, tabPane);

        // Adding the tabs and making them uncloseable, plus some additional
        // customization like paddings.
        Tab tab1 = new Tab("Tab 1", new MainPageTab1());
        Tab tab2 = new Tab("Tab 2", new MainPageTab2());
        Tab tab3 = new Tab("Tab 3", new MainPageTab3());
        Tab tab4 = new Tab("Tab 4", new MainPageTab4());
        Tab tab5 = new Tab("Tab 5", new MainPageTab5());
        Tab tab6 = new Tab("Tab 6", new MainPageTab6());

        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5, tab6);

        for (Tab tab : tabPane.getTabs()) {
            tab.setClosable(false);
        }
        // The TabPane's tabMinWidthProperty is bound to the TabPane's width divided by the number of tabs. This makes
        // sure that the width of the tabs will change as the window is resized, keeping the space between all tabs the
        // same.
        tabPane.tabMinWidthProperty().bind(tabPane.widthProperty().divide(tabPane.getTabs().size()).subtract(10)); // subtracting 10 for padding

        stage.setScene(scene);
        stage.setTitle("[COMPANY HOMEPAGE]");
        stage.show();
    }

    /**
     * This method creates a new window (Stage) that represents the user's profile. Inside this window,
     * we plan to display the person's information(future implementation) and a back button are displayed.
     * Clicking the back button will close the profile window and return the user to the main application.
     */
    private void openProfileWindow() {
        // Profile window creation and set up.
        Stage profileStage = new Stage();
        HBox profileLayout = new HBox(20);
        Scene profileScene = new Scene(profileLayout, 300, 200);

        // Button with an event where, upon click, will take the user back to the MainPage.
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> profileStage.close());

        profileLayout.getChildren().addAll(backButton);
        profileStage.setScene(profileScene);
        profileStage.setTitle("Profile");
        profileStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}