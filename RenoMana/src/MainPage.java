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
 * @since 2023-08-01
 */

import inventoryMana.Inventory;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.geometry.Insets;

public class MainPage extends Application {
    private VBox sideBar;
    private VBox contentArea;
    private Label contentTitle = new Label();
    private LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4B1517")),
            new Stop(1, Color.web("#C49102")));


    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox();
        HBox mainLayout = new HBox();
        mainLayout.setStyle("-fx-background-color: lightGray");
        Scene scene = new Scene(rootLayout, 1920, 1080);

        // ----- Top Bar Setup -----
        HBox topBar = new HBox(20);
        topBar.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        // Simple color background: topBar.setStyle("-fx-background-color: #4B1517; -fx-padding: 10px;");

        // Toggle button for the sidebar
        ToggleButton toggleSidebar = getToggleButton();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // The top bar will contain account profile and search tab
        Label accountName = new Label("Hello, User!");
        accountName.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        Circle profileCircle = new Circle(30);
        profileCircle.setStyle("-fx-background-color: #9E1C29; -fx-stroke: #7C1715; -fx-border-radius: 2;");
        profileCircle.setOnMouseClicked(event -> openProfileWindow());

        topBar.getChildren().addAll(toggleSidebar, spacer, accountName, profileCircle);
        topBar.setAlignment(Pos.CENTER_RIGHT);

        // ----- Sidebar setup -----
        sideBar = new VBox(10);
        sideBar.setPrefWidth(200);
        gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#431114")),
                new Stop(1, Color.web("#844600")));
        sideBar.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        // sideBar.setStyle("-fx-background-color: #4B1517; -fx-padding: 10px;");
        sideBar.setVisible(false);

        // Content area setup
        contentArea = new VBox(10);
        contentArea.setStyle("-fx-background-color: lightGray; -fx-padding: 10px;");

        // Create buttons for each tab and add them to the sidebar
        createTabButton("Dashboard", new MainPageTab1(), "Dashboard");
        createTabButton("Schedule", new MainPageTab2(), "Schedule");
        createTabButton("Inventory", new Inventory(), "Inventory");
        createTabButton("Employees", new MainPageTab4(), "Employees");

        mainLayout.getChildren().addAll(sideBar, contentArea);
        rootLayout.getChildren().addAll(topBar, mainLayout);

        // Display Dashboard content by default
        sideBar.setMaxWidth(0);
        displayContent(new MainPageTab1(), "Dashboard");

        // Ensure system expand vertically and horizontally to fill available space
        VBox.setVgrow(mainLayout, Priority.ALWAYS);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

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

    private void displayContent(Node content, String title) {
        contentArea.getChildren().clear();
        contentTitle.setText(title);
        contentTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px;");
        contentArea.getChildren().addAll(contentTitle, content);
    }

    private void createTabButton(String title, Node content, String contentTitle) {
        // shades of red : #4B1517 | #7C1715 | #9E1C29 | #AB2838 | #B84656
        Button button = new Button(title);
        button.setPrefWidth(Double.MAX_VALUE);

        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        button.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: #7C1715; -fx-text-fill: white;");
            } else {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            }
        });

        button.setOnAction(e -> displayContent(content, contentTitle));

        sideBar.getChildren().add(button);
    }

    private ToggleButton getToggleButton() {
        ToggleButton toggleSidebar = new ToggleButton("Menu");
        toggleSidebar.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        toggleSidebar.setOnAction(e -> {
            if (toggleSidebar.isSelected()) {
                sideBar.setVisible(true);
                sideBar.setMaxWidth(sideBar.getPrefWidth());
            } else {
                sideBar.setVisible(false);
                sideBar.setMaxWidth(0);
            }
        });
        return toggleSidebar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}