package dashboardMana;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import employeeMana.Employee;
import employeeMana.EmployeeList;
import inventoryMana.InventoryItem;
import inventoryMana.Inventory;
import javafx.scene.text.Font;
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

    private String userFname;
    private String userLname;


    public Dashboard(String COOKIES, String dashboardData) throws IOException, InterruptedException {

        this.userFname = parseJson(dashboardData, "fname");
        this.userLname = parseJson(dashboardData, "lname");

        // Second option Project timelines as List (inline commented)
        // timeLineListView = new ListView<>();

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

        // Second option Project timelines as List (inline commented)
        // timeLineListView.prefHeightProperty().bind(heightProperty());

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

        // First option projects as table
        // Create columns of TableView for Project and add them into project table
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

            // Second option Project timelines as List (inline commented)
            // timeLineListView.setItems(Scheduler.projectsTimelineList);

            projectTableView.setItems(Scheduler.data);
            projectTableView.refresh();

        });
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        // Adding each parts into their parent node until the root node.
        welcomeBox.getChildren().addAll(welcomeMsg);
        pieBox.getChildren().addAll(pieTitle);
        //pieBox.getChildren().addAll(pieTitle,employeePie);
        inventoryBox.getChildren().addAll(inventoryTitle,dashboardInventoryTable);

        leftBox.getChildren().addAll(welcomeBox,pieBox,inventoryBox);
        //rightBox.getChildren().addAll(projectTimeLineTitle,timeLineListView);
        rightBox.getChildren().addAll(projectTimeLineTitle,projectTableView);

        Button refreshDashboard = new Button("Refresh");
        refreshDashboard.setOnAction(actionEvent -> {
            try {
                fetchDashboardData(COOKIES);
            } catch(Exception e){
                showAlert("Error!", "Something went wrong when loading STUFF");
            }
        });

        getChildren().addAll(leftBox,rightBox, refreshDashboard);
    }

    private PieChart displayPieChart (ObservableList<Employee> employeeList){

        for (Employee employee:employeeList){
            System.out.println(employee.getEmployeeFirstName());
        }

        return null;
    }

    private String fetchDashboardData(String COOKIES) throws IOException, InterruptedException {
        System.out.println(COOKIES);
        String msg = "{" +
                "\"cookie\":\"" + COOKIES +
                "\"}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/getDashboardDataTest"))
                .timeout(java.time.Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println("[DASHBOARD] " + request.toString());
        System.out.println(msg);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        System.out.println("[DASHBOARD] " + responseBody);
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

}
