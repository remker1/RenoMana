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

/**
 * MainPage Class
 *
 * <p>
 * This class represents the main page or home page of the application. It provides a user interface with multiple tabs,
 * where each tab showcases a different feature of the application. By modularizing the content of each tab
 * into its own class, the application becomes more maintainable and organized.
 * </p>
 *
 * @author Jewel Magcawas
 * @since 2023-08-01
 */
public class MainPage extends Application {
    /**
     * Main layout components
     */
    private HBox mainLayout = new HBox();
    /**
     * Sidebar for navigation
     */
    private VBox sideBar;
    private VBox contentArea;
    /**
     * Title of the currently displayed content
     */
    private Label contentTitle = new Label();

    /**
     * Gradient style for UI components
     */
    private LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4B1517")),
            new Stop(1, Color.web("#C49102")));

    /**
     * Flag to track if dark mode is enabled
     */
    private boolean isDarkMode = false;
    private ProfileCircle profileCircleHandler;



    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox();
        mainLayout.setStyle("-fx-background-color: lightGray");
        Scene scene = new Scene(rootLayout, 1920, 1080);

        // ----- Setting up the top bar of the application -----
        HBox topBar = new HBox(20);
        topBar.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        // Simple color background: topBar.setStyle("-fx-background-color: #4B1517; -fx-padding: 10px;");

        // The top bar will contain account profile and search tab
        Label accountName = new Label("Hello, User!");
        accountName.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        profileCircleHandler = new ProfileCircle(this);
        Circle profileCircle = profileCircleHandler.getProfileCircle();

        // Toggle button for the sidebar
        ToggleButton toggleSidebar = getToggleButton();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

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
     * This method opens the user's profile in a new window. The profile window contains options
     * for switching between light and dark mode and a back button to return to the main application.
     */
    public void openProfileWindow() {
        // Profile window creation and set up.
        Stage profileStage = new Stage();
        VBox profileLayout = new VBox(20);
        Scene profileScene = new Scene(profileLayout, 300, 200);

        // Slider for light and dark mode
        Slider modeSlider = new Slider();
        modeSlider.setMin(0);
        modeSlider.setMax(1);
        modeSlider.setValue(0); // 0 for light mode, 1 for dark mode
        modeSlider.setShowTickLabels(true);
        modeSlider.setShowTickMarks(true);
        modeSlider.setMajorTickUnit(1);
        modeSlider.setMinorTickCount(0);
        modeSlider.setSnapToTicks(true);
        modeSlider.setBlockIncrement(1);

        Label modeLabel = new Label("Light Mode");

        if (isDarkMode) {
            modeSlider.setValue(1);
            modeLabel.setText("Dark Mode");
        } else {
            modeSlider.setValue(0);
            modeLabel.setText("Light Mode");
        }


        modeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                modeLabel.setText("Dark Mode");
                setDarkMode();
            } else {
                modeLabel.setText("Light Mode");
                setLightMode();
            }
        });

        // Button with an event where, upon click, will take the user back to the MainPage.
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> profileStage.close());

        profileLayout.getChildren().addAll(modeLabel, modeSlider, backButton);
        profileStage.setScene(profileScene);
        profileStage.setTitle("Profile");
        profileStage.show();
    }

    /**
     * This method switches the application's theme to dark mode.
     */
    public void setDarkMode() {
        contentArea.setStyle("-fx-background-color: #1C1C1C; -fx-padding: 10px;");
        contentTitle.setTextFill(Color.WHITE);
        mainLayout.setStyle("-fx-background-color: #1C1C1C;");
        isDarkMode = true;

    }

    /**
     * This method switches the application's theme to light mode.
     */
    public void setLightMode() {
        contentArea.setStyle("-fx-background-color: lightGray; -fx-padding: 10px;");
        contentTitle.setTextFill(Color.BLACK);
        mainLayout.setStyle("-fx-background-color: lightGray;");
        isDarkMode = false;
    }

    /**
     * This method displays the content of the selected tab in the content area.
     *
     * @param content The content to be displayed.
     * @param title The title of the content.
     */
    public void displayContent(Node content, String title) {
        contentArea.getChildren().clear();
        contentTitle.setText(title);
        contentTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 10px;");
        contentArea.getChildren().addAll(contentTitle, content);
    }

    /**
     * This method creates a button for a tab and adds it to the sidebar.
     *
     * @param title The title of the button.
     * @param content The content associated with the button.
     * @param contentTitle The title of the content.
     */
    public void createTabButton(String title, Node content, String contentTitle) {
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

    /**
     * This method creates and returns a toggle button for the sidebar.
     *
     * @return The toggle button.
     */
    public ToggleButton getToggleButton() {
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