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

import COOKIES.COOKIES;
import ManagerCheck.ManagerCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import dashboardMana.Dashboard;
import inquiryMana.inquiry;
import inventoryMana.Inventory;
import inventoryMana.InventoryItem;
import inventoryMana.InventoryItems;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import reviewMana.Review;
import timeMana.Projects;
import timeMana.Scheduler;
import timeMana.Calendar;
import employeeMana.EmployeeList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import ProjectRequestsMana.ProjectRequests;

public class MainPage extends BasicPage {
    HBox mainLayout = new HBox();
    private VBox sideBar;
    private VBox contentArea;
    private Label contentTitle = new Label();
    private LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4B1517")),
            new Stop(1, Color.web("#C49102")));

    private boolean isDarkMode = false;

    private Projects allProjectData = new Projects();

    private String userFname;
    private String userLname;

    String userEmail;
    String userProjects;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        String dashboardData = fetchDashboardData(COOKIES);
        List<InventoryItem> inventoryData = fetchInventoryData();

        this.userFname = parseJson(dashboardData, "fname");
        this.userLname = parseJson(dashboardData, "lname");
        this.userEmail = parseJson(dashboardData, "email");
        this.userProjects = parseJson(dashboardData, "projects");

//        stage.getIcons().add(new Image("./resources/icon.png"));
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
        Label accountName = new Label(this.userFname + " " + this.userLname);
        accountName.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        Circle profileCircle = new Circle(25);
        Image image = new Image("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png");
        profileCircle.setStyle("-fx-background-color: #9E1C29; -fx-stroke: #7C1715; -fx-border-radius: 2;");
        profileCircle.setOnMouseClicked(event -> openSettingsWindow());
        profileCircle.setFill(new ImagePattern(image));

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
        createTabButton("Dashboard", new Dashboard(COOKIES, dashboardData), "Dashboard");
        if (ManagerCheck.isManager(COOKIES)){
            createTabButton("Project Requests", new ProjectRequests(), "Project Requests");
        }
        createTabButton("Scheduler", new Scheduler(COOKIES, allProjectData.getProjects()), "Scheduler");
        createTabButton("Calendar", new Calendar(allProjectData.getProjects()), "Calendar");
        createTabButton("Employees", new EmployeeList(COOKIES), "Employees");
        createTabButton("Inventory", new Inventory(inventoryData), "Inventory");
        createTabButton("Reviews", new Review(), "Reviews");
        createTabButton("Inquiries", new inquiry(COOKIES), "Inquiries");

        Button button = new Button("Log out");
        button.setPrefWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        button.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: #7C1715; -fx-text-fill: white;");
            } else {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            }
        });

        button.setOnAction(e -> {
            confirmLogout(stage);
        });

        sideBar.getChildren().add(button);

        mainLayout.getChildren().addAll(sideBar, contentArea);
        rootLayout.getChildren().addAll(topBar, mainLayout);

        // Display Dashboard content by default
        sideBar.setMaxWidth(0);
        displayContent(new Dashboard(COOKIES, dashboardData), "Dashboard");

        // Ensure system expand vertically and horizontally to fill available space
        VBox.setVgrow(mainLayout, Priority.ALWAYS);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        stage.setScene(scene);
        stage.setTitle("The Reno Group Management App");
        stage.show();
    }

    /**
     * This method creates a new window (Stage) that will show the settings of the application such as
     * light mode and dark mode.
     */
    private void openSettingsWindow() {
        // Setting up layout and spacing for the window
        Stage settingsStage = new Stage();
        VBox settingsLayout = new VBox(10);
        settingsLayout.setPadding(new Insets(15, 20, 15, 20));


        // ====== Personal information section ======
        TitledPane personalInfoTitledPane = getPersonalInfoTitledPane();

        // ====== Display Mode Section ======
        TitledPane modeTitledPane = new TitledPane();
        modeTitledPane.setText("Display Mode");

        // Slider for light and dark mode
        Slider modeSlider = new Slider();
        modeSlider.setMin(0);
        modeSlider.setMax(1);
        modeSlider.setValue(isDarkMode ? 1 : 0);
        modeSlider.setShowTickLabels(true);
        modeSlider.setShowTickMarks(true);
        modeSlider.setMajorTickUnit(1);
        modeSlider.setMinorTickCount(0);
        modeSlider.setSnapToTicks(true);
        modeSlider.setBlockIncrement(1);

        Label modeLabel = new Label(isDarkMode ? "Dark Mode" : "Light Mode");

        // Listen for changes in slider value to update the label and mode
        modeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean darkMode = newValue.intValue() == 1;
            modeLabel.setText(darkMode ? "Dark Mode" : "Light Mode");
            if (darkMode) {
                setDarkMode();
            } else {
                setLightMode();
            }
        });

        VBox modeBox = new VBox(5); // Use 5px spacing between children
        modeBox.getChildren().addAll(modeLabel, modeSlider);
        modeTitledPane.setContent(modeBox);

        // Buttons
        Button backButton = new Button("Back");
        Button logoutButton = new Button("Log out");

        // ButtonBar for a more native look
        ButtonBar buttonBar = new ButtonBar();
        ButtonBar.setButtonData(backButton, ButtonBar.ButtonData.LEFT);
        ButtonBar.setButtonData(logoutButton, ButtonBar.ButtonData.RIGHT);

        buttonBar.getButtons().addAll(backButton, logoutButton);

        backButton.setOnAction(event -> settingsStage.close());
        logoutButton.setOnAction(event -> confirmLogout(settingsStage));

        settingsLayout.getChildren().add(0, personalInfoTitledPane);
        settingsLayout.getChildren().addAll(modeTitledPane, buttonBar);
        Scene settingsScene = new Scene(settingsLayout);
        settingsStage.setScene(settingsScene);
        settingsStage.setTitle("Settings");
        settingsStage.show();
    }

    /**
     * This method creates a titled pane object that displays the employee's information in when profile
     * circle is clicked
     * @return TitledPane that will display employee information.
     */
    private TitledPane getPersonalInfoTitledPane() {
        TitledPane personalInfoTitledPane = new TitledPane();
        personalInfoTitledPane.setText("Personal Information");

        GridPane personalInfoGrid = new GridPane();
        personalInfoGrid.setVgap(10);
        personalInfoGrid.setHgap(10);

        Label NameLabel = new Label("Name: " + userFname + ' ' + userLname);
        Label emailLabel = new Label("Email: " + userEmail);
        Label projectsLabel = new Label("Projects: " + userProjects);


        // Adding labels and fields to the grid
        personalInfoGrid.add(NameLabel, 0, 0);
        personalInfoGrid.add(emailLabel, 0, 1);
        personalInfoGrid.add(projectsLabel, 0, 2);
        personalInfoTitledPane.setContent(personalInfoGrid);
        return personalInfoTitledPane;
    }


    private void setDarkMode() {
        // Set the dark mode styles
        contentArea.setStyle("-fx-background-color: #1C1C1C; -fx-padding: 10px;");
        mainLayout.setStyle("-fx-background-color: #1C1C1C;");
        isDarkMode = true;
        updateTextFill(contentArea, Color.WHITE);
    }

    private void setLightMode() {
        // Set the light mode styles
        contentArea.setStyle("-fx-background-color: lightGray; -fx-padding: 10px;");
        mainLayout.setStyle("-fx-background-color: lightGray;");
        isDarkMode = false;
        updateTextFill(contentArea, Color.BLACK);
    }

    // Helper method to update text fill of all labels in a page
    private void updateTextFill(Pane page, Color colour) {
        for (Node node : page.getChildren()) {
            if (node instanceof Label) {
                ((Label) node).setTextFill(colour);
            } else if (node instanceof Pane) {
                updateTextFill((Pane) node, colour);
            }
        }
    }

    private void confirmLogout(Stage profileStage) {
        // Confirmation dialog for logging out
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?", ButtonType.YES, ButtonType.NO);
        confirmDialog.setHeaderText(null);
        confirmDialog.setTitle("Log out");

        // Wait for user response
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            // User chose to log out
            COOKIES = null;
            profileStage.close();
            Stage mainStage = (Stage) mainLayout.getScene().getWindow(); // get the main stage
            mainStage.close();
            // Open login window
            try {
                new Login().start(new Stage());
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
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

    private String fetchDashboardData(COOKIES COOKIES) throws IOException, InterruptedException {
        System.out.println(COOKIES.getUsername());
        String msg = "{" +
                "\"cookie\":\"" + COOKIES.getUsername() +
                "\"}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/getDashboardData"))
                .timeout(java.time.Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println("[DASHBOARD] " + request.toString());
        System.out.println(msg);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        System.out.println("[DASHBOARD] " + responseBody);
        this.userFname = parseJson(responseBody, "fname");
        this.userLname = parseJson(responseBody, "lname");
        return responseBody;
    }

    private List<InventoryItem> fetchInventoryData() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/getInventoryDataInitial"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body()); // For debugging

            ObjectMapper mapper = new ObjectMapper();

            InventoryItems inventoryItems = mapper.readValue(response.body(), InventoryItems.class);
            return inventoryItems.getItems();
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return  null;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}