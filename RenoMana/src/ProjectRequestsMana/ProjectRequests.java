package ProjectRequestsMana;

import com.fasterxml.jackson.databind.ObjectMapper;
import employeeMana.Employee;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import timeMana.Scheduler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class ProjectRequests extends VBox {

    private TableView<ProjectRequestsItem> ProjectTable;
    private ObservableList<ProjectRequestsItem> data;
    private int id;

    public ProjectRequests() throws IOException, InterruptedException {
        // Setting up the table
        ProjectTable = new TableView<>();
        ProjectTable.prefWidthProperty().bind(this.widthProperty());
        data = FXCollections.observableArrayList();

        // Adding column names
        // TODO remove
        TableColumn<ProjectRequestsItem, Integer> idCol = new TableColumn<>("id");
        idCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        idCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.15));

        TableColumn<ProjectRequestsItem, String> ProjectCol = new TableColumn<>("Name");
        ProjectCol.setCellValueFactory(cellData -> cellData.getValue().Name());
        ProjectCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.15));

        TableColumn<ProjectRequestsItem, String> EmailCol = new TableColumn<>("Email");
        EmailCol.setCellValueFactory(cellData -> cellData.getValue().Email());
        EmailCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        TableColumn<ProjectRequestsItem, String> ContactCol = new TableColumn<>("Contact");
        ContactCol.setCellValueFactory(cellData -> cellData.getValue().Contact());
        ContactCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        TableColumn<ProjectRequestsItem, String> CompanyCol = new TableColumn<>("Company");
        CompanyCol.setCellValueFactory(cellData -> cellData.getValue().Company());
        CompanyCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        TableColumn<ProjectRequestsItem, String> End_dateCol = new TableColumn<>("End date");
        End_dateCol.setCellValueFactory(cellData -> cellData.getValue().End_date());
        End_dateCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        TableColumn<ProjectRequestsItem, String> DescriptionCol = new TableColumn<>("Description");
        DescriptionCol.setCellValueFactory(cellData -> cellData.getValue().Description());
        DescriptionCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.35));

        TableColumn<ProjectRequestsItem, String> ActionCol = new TableColumn<>("Action");
        ActionCol.setCellFactory(col -> new TableCell<ProjectRequestsItem, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll("Accept", "Decline");
                comboBox.setOnAction(e -> handleAction(comboBox.getValue(), getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(comboBox);
                }
            }
        });
        ActionCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        // set a custom row factory to change the row appearance based on the 'accepted' property
        ProjectTable.setRowFactory(tv -> new TableRow<ProjectRequestsItem>() {
            @Override
            protected void updateItem(ProjectRequestsItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    // if the project is accepted, highlight the row in green
                    if (item.isAccepted()) {
                        setStyle("-fx-background-color: lightgreen;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        ProjectTable.getColumns().addAll(
                ProjectCol,
                EmailCol,
                ContactCol,
                CompanyCol,
                End_dateCol,
                DescriptionCol,
                ActionCol
        );
        ProjectTable.setItems(data);

        loadProjects();

        Button deleteItem = new Button("Delete Project Transferred to Scheduler");
        Button refreshProjects = new Button("Refresh");
        refreshProjects.setOnAction(actionEvent -> {
            try {
                loadProjects();
            } catch(Exception e){
                showAlert("Error!", "Something went wrong when loading reviews");
            }
        });
        Button copyToClipboard = new Button("Copy to Clipboard");
        copyToClipboard.setOnAction(actionEvent -> copyProjectDetails());
//        Button addProject = new Button("Add Project");
//        addProject.setOnAction(actionEvent -> addProjectForDebugging());

        ProjectTable.setItems(data);

        deleteItem.setOnAction(actionEvent -> deleteProjectRequest());

        HBox optButton = new HBox(10, copyToClipboard, deleteItem, refreshProjects);
//        HBox optButton = new HBox(10, addProject, copyToClipboard, deleteItem, refreshProjects);
        optButton.setPadding(new Insets(10));

        VBox.setVgrow(ProjectTable, Priority.ALWAYS);
        this.getChildren().addAll(ProjectTable, optButton);
    }

    private void copyProjectDetails() {
        ProjectRequestsItem selectedProject = ProjectTable.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            String projectDetails = formatProjectDetails(selectedProject);
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(projectDetails);
            clipboard.setContent(content);
            showAlert("Success", "Project details copied to clipboard.");
        } else {
            showAlert("Error", "No project selected.");
        }
    }

    private String formatProjectDetails(ProjectRequestsItem project) {
        // Format the project details as a string
        return "Client Name: " + project.Name().get() +
                " Email: " + project.Email().get() +
                " Contact: " + project.Contact().get() +
                " Company: " + project.Company().get() +
                " End Date: " + project.End_date().get() +
                " Description: " + project.Description().get();
    }


//    private void addProjectForDebugging() {
//        ProjectRequestsItem debugItem = new ProjectRequestsItem(
//                "Debug Project", "debug@example.com", "1234567890", "Debug Company",
//                "2023-01-01", "2023-12-31", "This is a debug project", "Debug inquiry"
//        );
//        data.add(debugItem);
//        ProjectTable.refresh();
//    }
    private void handleAction(String action, ProjectRequestsItem project) {
        if ("Accept".equals(action)) {
            project.setAccepted(true);
            Scheduler.addProject(project.Name().getValue(), project.End_date().getValue(), project.Description().getValue());
        } else if ("Decline".equals(action)) {
            deleteProject(project.getId());
            data.remove(project);
        }
        ProjectTable.refresh();
    }



    public void loadProjects() throws IOException, InterruptedException {
        this.data.clear();

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();


        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5001/submitRequest"))
                .GET()
                .build();

        // Send POST message to Flask server
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Decode POST response and add reviews to table data
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProjectRequestsItems projects = mapper.readValue(response.body(), ProjectRequestsItems.class);
            this.data.addAll(projects.getprojects());
        } catch (Exception e){
            System.out.println(e);
        }

        // Refresh table
        ProjectTable.refresh();

    }

    private void deleteProjectRequest() {
        // When item is selected ...
        ProjectRequestsItem selected_project = ProjectTable.getSelectionModel().getSelectedItem();

        // Check if that item row is valid or does exist, if not, throw an alert
        if (selected_project == null) {
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No project is selected!");
            noSelectedAlert.setContentText("Please select a project from the table to delete.");
            noSelectedAlert.showAndWait();
            return;
        }

        // Get the projectInq or any unique identifier for the selected project
        deleteProject(selected_project.getId());
    }

    private void deleteProectRequest(int id){
        deleteProject(id);
    }

    private void deleteProject(int selected_project1){
        // Set the URL for the delete endpoint on your server
        String deleteUrl = "http://localhost:5001/deleteproject";

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Build the JSON payload
        String jsonPayload = "{\"delete_project\":\"" + selected_project1 + "\"}";

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(deleteUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            // Send the POST request to delete the project
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response status and show an alert
            if (response.statusCode() == 200) {
                showAlert("Success", "Project deleted successfully.");
            } else {
                showAlert("Error", "Failed to delete the project.");
            }

            // Refresh the table if needed
            loadProjects();
        } catch (Exception e) {
            showAlert("Error", "An error occurred while deleting the project: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

}
