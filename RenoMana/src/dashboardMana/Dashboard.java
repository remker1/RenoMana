package dashboardMana;


import COOKIES.COOKIES;
import employeeMana.Employee;
import inventoryMana.Inventory;
import inventoryMana.InventoryItem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import timeMana.Project;
import timeMana.Scheduler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


public class Dashboard extends HBox {

    // Second option Project timelines as List (inline commented)
    // private final ListView<String> timeLineListView;

    private final TableView<Project> projectTableView;
    private final TableView<InventoryItem> dashboardInventoryTable;

    private static String userFname;
    private static String userLname;


    public Dashboard(COOKIES COOKIES, String dashboardData) throws IOException, InterruptedException {

        this.userFname = parseJson(dashboardData, "fname");
        this.userLname = parseJson(dashboardData, "lname");

        dashboardInventoryTable = new TableView<>();
        projectTableView = new TableView<>();

        // initialize boxes
        VBox leftBox = new VBox();
        VBox rightBox = new VBox();
        VBox welcomeBox = new VBox();
        HBox pieBox = new HBox();
        VBox inventoryBox = new VBox();

        // set up labels for Dashboard
        final Label welcomeMsg = new Label("Welcome, " + this.userFname + " " + this.userLname + "!");
        welcomeMsg.setFont(new Font("Arial", 20));
        final Label inventoryTitle = new Label("Inventory Items: ");
        inventoryTitle.setFont(new Font("Arial", 20));
        final Label pieTitle = new Label("Employee Section: ");
        pieTitle.setFont(new Font("Arial", 20));
        final Label projectTimeLineTitle = new Label("Project TimeLines: ");
        projectTimeLineTitle.setFont(new Font("Arial", 20));

        // setting up portions of window each part will take
        welcomeBox.prefHeightProperty().bind(heightProperty().multiply(0.1));
        pieBox.prefHeightProperty().bind(heightProperty().multiply(0.4));
        inventoryBox.prefHeightProperty().bind(heightProperty().subtract(welcomeBox.heightProperty().add(pieBox.heightProperty())));

        leftBox.prefWidthProperty().bind(widthProperty().multiply(0.7));
        rightBox.prefWidthProperty().bind(widthProperty().multiply(0.3));

        projectTableView.prefHeightProperty().bind(heightProperty());

        // Create columns of TableView for Inventory and add them into inventory table
        TableColumn<InventoryItem, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemName() ));
        itemNameCol.prefWidthProperty().bind(dashboardInventoryTable.widthProperty().multiply(0.333334));

        TableColumn<InventoryItem, String> itemDescriptionCol = new TableColumn<>("Item Description");
        itemDescriptionCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemDescription()));
        itemDescriptionCol.prefWidthProperty().bind(dashboardInventoryTable.widthProperty().multiply(0.333334));

        TableColumn<InventoryItem, String> itemProjectCol = new TableColumn<>("Item Project");
        itemProjectCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemProject()));
        itemProjectCol.prefWidthProperty().bind(dashboardInventoryTable.widthProperty().multiply(0.333334));

        dashboardInventoryTable.getColumns().addAll(itemNameCol,itemDescriptionCol, itemProjectCol);

        TableColumn<Project, String> projectNameCol = new TableColumn<>("Project Name");
        projectNameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        projectNameCol.prefWidthProperty().bind(projectTableView.widthProperty().multiply(0.5));

        TableColumn<Project, String> timeLineCol = new TableColumn<>("Project Time Line");
        timeLineCol.setCellValueFactory(cellData -> cellData.getValue().timelineProperty());
        timeLineCol.prefWidthProperty().bind(projectTableView.widthProperty().multiply(0.5));

        projectTableView.getColumns().addAll(timeLineCol,projectNameCol);

        // setup timer for updating tables or listview by every 1 sec.
        Timeline timeline = new Timeline();
        Duration duration = Duration.seconds(1);
        KeyFrame keyFrame = new KeyFrame(duration, event -> {
            dashboardInventoryTable.setItems(Inventory.data);
            dashboardInventoryTable.refresh();

            projectTableView.setItems(Scheduler.data);
            projectTableView.refresh();

        });
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        Button refreshDashboard = new Button("Refresh");
        refreshDashboard.setOnAction(actionEvent -> {
            try {
                fetchDashboardData(COOKIES);
            } catch(Exception e){
                showAlert("Error!", "Something went wrong when loading STUFF");
            }
        });

        Button filterButton = new Button("Filter Options");

        filterButton.setOnAction(e -> showFilterWindow());

        pieBox.getChildren().addAll(displayPieChart());

        welcomeBox.getChildren().addAll(welcomeMsg,refreshDashboard,filterButton);

        pieBox.getChildren().addAll(pieTitle);
        inventoryBox.getChildren().addAll(inventoryTitle,dashboardInventoryTable);

        leftBox.getChildren().addAll(welcomeBox,pieBox,inventoryBox);
        rightBox.getChildren().addAll(projectTimeLineTitle,projectTableView);

        getChildren().addAll(leftBox,rightBox);
    }

    private PieChart displayPieChart(){

        PieChart.Data slice1 = new PieChart.Data("Category 1", 30);
        PieChart.Data slice2 = new PieChart.Data("Category 2", 45);
        PieChart.Data slice3 = new PieChart.Data("Category 3", 25);

        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(slice1, slice2, slice3);

        return pieChart;
    }

    private String fetchDashboardData(COOKIES COOKIES) throws IOException, InterruptedException {
        String msg = "{" +
                "\"cookie\":\"" + COOKIES.getUsername() +
                "\"}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/getDashboardDataTest"))
                .timeout(java.time.Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println("[DASHBOARD] " + request.toString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        return responseBody;
    }

    private void showAlert(String title, String content) {
        Alert invalidNumAlert = new Alert(Alert.AlertType.ERROR);
        invalidNumAlert.setTitle(title);
        invalidNumAlert.setHeaderText(null);
        invalidNumAlert.setContentText(content);
        invalidNumAlert.showAndWait();
    }

    private static String parseJson(String string, String target) {
        // Find the index of the cookie key
        int startIndex = string.indexOf("\"" + target + "\"");

        // Check if the key is found
        if (startIndex != -1) {
            // Move the index to the start of the value
            startIndex = string.indexOf(":", startIndex) + 1;

            // Find the end of the value (up to the next comma or the end of the JSON object)
            int endIndex = string.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = string.indexOf("}", startIndex);
            }

            // Extract the value
            String value = string.substring(startIndex, endIndex).trim().replace("\"", "");

            return value;

        } else {
            System.out.println(target + " not found in the response");
            return null;
        }
    }

    private void showFilterWindow() {
        Stage filterStage = new Stage();
        filterStage.setTitle("Filter Options");

        CheckBox inventoryCheckbox = new CheckBox("Show Inventory Table");
        inventoryCheckbox.setSelected(true);
        inventoryCheckbox.setOnAction(e -> dashboardInventoryTable.setVisible(inventoryCheckbox.isSelected()));

        CheckBox projectCheckbox = new CheckBox("Show Project Table");
        projectCheckbox.setSelected(true);
        projectCheckbox.setOnAction(e -> projectTableView.setVisible(projectCheckbox.isSelected()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(inventoryCheckbox, projectCheckbox);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);
        filterStage.setScene(scene);
        filterStage.show();
    }

    public static String getUserFname() {
        return userFname;
    }

    public static String getUserLname() {
        return userLname;
    }
}
