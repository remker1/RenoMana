package dashboardMana;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import employeeMana.Employee;
import employeeMana.EmployeeList;
import inventoryMana.InventoryItem;
import inventoryMana.Inventory;
import javafx.scene.text.Font;
import javafx.util.Duration;
import timeMana.Project;
import timeMana.Scheduler;


public class Dashboard extends HBox {

    // Second option Project timelines as List (inline commented)
    // private final ListView<String> timeLineListView;

    private final TableView<Project> projectTableView;
    private final TableView<InventoryItem> dashboardInventoryTable;

    public Dashboard() {

        // Second option Project timelines as List (inline commented)timeLineListView = new ListView<>();

        dashboardInventoryTable = new TableView<>();
        projectTableView = new TableView<>();

        // initialize boxes
        VBox leftBox = new VBox();
        VBox rightBox = new VBox();
        VBox welcomeBox = new VBox();
        HBox pieBox = new HBox();
        VBox inventoryBox = new VBox();

        // set up labels for Dashboard
        final Label welcomeMsg = new Label("Welcome,_____");
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
        TableColumn<InventoryItem, String> toolNameCol = new TableColumn<>("Tool Name");
        toolNameCol.setCellValueFactory(cellData -> cellData.getValue().toolNameProperty());
        toolNameCol.prefWidthProperty().bind(dashboardInventoryTable.widthProperty().multiply(0.5));

        TableColumn<InventoryItem, Integer> quantityCol = new TableColumn<>("Total Quantity");
        quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        quantityCol.prefWidthProperty().bind(dashboardInventoryTable.widthProperty().multiply(0.5));

        dashboardInventoryTable.getColumns().addAll(toolNameCol,quantityCol);

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

            //projectTableView.setItems(Scheduler.data);
            //projectTableView.refresh();

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

        getChildren().addAll(leftBox,rightBox);
    }

    // TODO:
    //  For Vince:
    //      displayPieChart and Hello to currently Logged in User

    private PieChart displayPieChart (ObservableList<Employee> employeeList){

        for (Employee employee:employeeList){
            System.out.println(employee.getEmployeeFirstName());
        }

        return null;
    }

}
