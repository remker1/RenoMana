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
import inquiryMana.inquiry;
import inventoryMana.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import reviewMana.Review;
import timeMana.Project;
import timeMana.Scheduler;
import timeMana.Calendar;
import employeeMana.EmployeeList;
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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import ProjectMana.Project2;

public class MainPage extends BasicPage {
    HBox mainLayout = new HBox();
    private VBox sideBar;
    private VBox contentArea;
    private Label contentTitle = new Label();
    private LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4B1517")),
            new Stop(1, Color.web("#C49102")));

    private boolean isDarkMode = false;

    private ObservableList<Project> allProjects = FXCollections.observableArrayList();

    private String userFname;
    private String userLname;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        String dashboardData = fetchDashboardData(COOKIES);

        this.userFname = parseJson(dashboardData, "fname");
        this.userLname = parseJson(dashboardData, "lname");

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
        createTabButton("Dashboard", new Dashboard(COOKIES, dashboardData), "Dashboard");
        createTabButton("Scheduler", new Scheduler(COOKIES), "Scheduler");
        //loadProjects();
        createTabButton("Calendar", new Calendar(allProjects), "Calendar");
        createTabButton("Inventory", new Inventory(), "Inventory");
        createTabButton("Employees", new EmployeeList(COOKIES), "Employees");
        createTabButton("Reviews", new Review(), "Reviews");
        createTabButton("Projects", new Project2(), "Projects");
        createTabButton("Inquiries", new inquiry(), "Inquiries");

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
     * This method creates a new window (Stage) that represents the user's profile. Inside this window,
     * we plan to display the person's information(future implementation) and a back button are displayed.
     * Clicking the back button will close the profile window and return the user to the main application.
     */
    private void openProfileWindow() {
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

    private void confirmLogout(Stage mainStage) {
        // Profile window creation and set up.
        Stage logoutStage = new Stage();
        VBox logoutLayout = new VBox(20);
        Scene logoutScene = new Scene(logoutLayout, 300, 200);

        Label titlelabel = new Label("Are you sure you want to log out?");

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(event -> {
            COOKIES = null;
            System.out.println(COOKIES);
            logoutStage.close();
            mainStage.close();
            try {
                new Login().start(new Stage());
            } catch (Exception exc) {
                System.out.println("Something went wrong when going into main page.");
            }
        });

        // Button with an event where, upon click, will take the user back to the MainPage.
        Button noButton = new Button("No");
        noButton.setOnAction(event -> {
            logoutStage.close();
        });

        logoutLayout.getChildren().addAll(titlelabel, yesButton, noButton);
        logoutStage.setScene(logoutScene);
        logoutStage.setTitle("Log out");
        logoutStage.show();
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

    private String fetchDashboardData(String COOKIES) throws IOException, InterruptedException {
        System.out.println(COOKIES);
        String msg = "{" +
                "\"cookie\":\"" + COOKIES +
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

    public static void main(String[] args) {
        launch(args);
    }
}