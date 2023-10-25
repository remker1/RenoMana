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

import dashboardMana.Dashboard;
import inventoryMana.Inventory;
import timeMana.Scheduler;
import timeMana.Calendar;
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

public class MainPage extends BasicPage {
    HBox mainLayout = new HBox();
    private VBox sideBar;
    private VBox contentArea;

    private Label contentTitle = new Label();
    private LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4B1517")),
            new Stop(1, Color.web("#C49102")));

    private boolean isDarkMode = false;
    private ProfileCircle profileCircleInstance;

    @Override
    public void start(Stage stage) {
        VBox rootLayout = new VBox();
        mainLayout.setStyle("-fx-background-color: lightGray");
        Scene scene = new Scene(rootLayout, 1280, 900);

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

        profileCircleInstance = new ProfileCircle(this);
        Circle profileCircle = profileCircleInstance.getProfileCircle();
        profileCircle.setStyle("-fx-background-color: #9E1C29; -fx-stroke: #7C1715; -fx-border-radius: 2;");
        profileCircle.setOnMouseClicked(event -> profileCircleInstance.openProfileWindow());

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
        createTabButton("Dashboard", new Dashboard(), "Dashboard");
        createTabButton("Scheduler", new Scheduler(), "Scheduler");
        createTabButton("Calendar", new Calendar(), "Calendar");
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
        stage.setTitle("The Reno Group Management App");
        stage.show();
    }

    private void setDarkMode() {
        contentArea.setStyle("-fx-background-color: #1C1C1C; -fx-padding: 10px;");
        contentTitle.setTextFill(Color.WHITE);
        mainLayout.setStyle("-fx-background-color: #1C1C1C;");
        isDarkMode = true;

    }

    private void setLightMode() {
        contentArea.setStyle("-fx-background-color: lightGray; -fx-padding: 10px;");
        contentTitle.setTextFill(Color.BLACK);
        mainLayout.setStyle("-fx-background-color: lightGray;");
        isDarkMode = false;
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

    public VBox getContentArea() {
        return contentArea;
    }

    public HBox getMainLayout() {
        return mainLayout;
    }

    public VBox getSideBar() {
        return sideBar;
    }

    public Label getContentTitle() {
        return contentTitle;
    }

    public LinearGradient getGradient() {
        return gradient;
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    public Slider createModeSlider() {
        Slider modeSlider = new Slider();
        modeSlider.setMin(0);
        modeSlider.setMax(1);
        if (isDarkMode) {
            modeSlider.setValue(1);
        } else {
            modeSlider.setValue(0);  // 0 for light mode, 1 for dark mode
        }

        modeSlider.setShowTickLabels(true);
        modeSlider.setShowTickMarks(true);
        modeSlider.setMajorTickUnit(1);
        modeSlider.setMinorTickCount(0);
        modeSlider.setSnapToTicks(true);
        modeSlider.setBlockIncrement(1);

        modeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                setDarkMode();
            } else {
                setLightMode();
            }
        });

        return modeSlider;
    }
    public static void main(String[] args) {
        launch(args);
    }

}