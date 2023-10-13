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

import inventoryMana.Inventory;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


// color red : #4B1517 | #7C1715 | #9E1C29 | #AB2838 | #B84656
public class MainPage extends Application {

    @Override
    public void start(Stage stage) {
        // The set-ups of pane and scene. Additionally, creating an account profile with an event when clicked.
        TabPane tabPane = new TabPane();
        VBox mainLayout = new VBox();
        mainLayout.setStyle("-fx-background-color: lightGray");
        Scene scene = new Scene(mainLayout, 2000, 600);

        Separator redDivider = new Separator();
        redDivider.setStyle("-fx-background-color: #7C1715; -fx-padding: 5 0 5 0;");

        // The account profile entrance
        Label accountName = new Label("Hello, User!");
        accountName.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        Circle profileCircle = new Circle(30);
        profileCircle.setStyle("-fx-background-color: #9E1C29; -fx-stroke: #7C1715; -fx-border-radius: 2;");
        profileCircle.setOnMouseClicked(event -> openProfileWindow());

        // Add the circle to the top right corner of the main window
        HBox topBar = new HBox(20, accountName, profileCircle);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setStyle("-fx-background-color: lightGray");

        HBox.setHgrow(accountName, javafx.scene.layout.Priority.ALWAYS); // Makes the account name push other elements to the right
        mainLayout.getChildren().addAll(topBar, redDivider, tabPane);

        // Adding the tabs and making them uncloseable, plus some additional
        // customization like paddings.
        Tab tab1 = new Tab("Dashboard", new MainPageTab1());
        Tab tab2 = new Tab("Schedule", new MainPageTab2());
        Tab tab3 = new Tab("Inventory", new Inventory());
        Tab tab4 = new Tab("Employees", new MainPageTab4());

        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);
        for (Tab tab : tabPane.getTabs()) {
            tab.setClosable(false);
            tab.setStyle("-fx-background-color: lightGray; -fx-border-radius: 5px 5px 0 0;");
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