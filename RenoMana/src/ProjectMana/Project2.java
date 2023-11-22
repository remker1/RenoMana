package ProjectMana;

import inventoryMana.InventoryItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import timeMana.Project;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class Project2 extends VBox {

    private TableView<ProjectItem> ProjectTable;
    private ObservableList<ProjectItem> data;

    public Project2() throws IOException, InterruptedException {
        // Setting up the table
        ProjectTable = new TableView<>();
        ProjectTable.prefWidthProperty().bind(this.widthProperty());
        data = FXCollections.observableArrayList();

        // Adding column names
        TableColumn<ProjectItem, String> ProjectCol = new TableColumn<>("Name");
        ProjectCol.setCellValueFactory(cellData -> cellData.getValue().Name());
        ProjectCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.15));

        TableColumn<ProjectItem, String> EmailCol = new TableColumn<>("Email");
        EmailCol.setCellValueFactory(cellData -> cellData.getValue().Email());
        EmailCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        TableColumn<ProjectItem, String> ContactCol = new TableColumn<>("Contact");
        ContactCol.setCellValueFactory(cellData -> cellData.getValue().Contact());
        ContactCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        TableColumn<ProjectItem, String> CompanyCol = new TableColumn<>("Company");
        CompanyCol.setCellValueFactory(cellData -> cellData.getValue().Company());
        CompanyCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        TableColumn<ProjectItem, String> Start_dateCol = new TableColumn<>("Start date");
        Start_dateCol.setCellValueFactory(cellData -> cellData.getValue().Start_date());
        Start_dateCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        TableColumn<ProjectItem, String> End_dateCol = new TableColumn<>("End date");
        End_dateCol.setCellValueFactory(cellData -> cellData.getValue().End_date());
        End_dateCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.10));

        TableColumn<ProjectItem, String> DescriptionCol = new TableColumn<>("Description");
        DescriptionCol.setCellValueFactory(cellData -> cellData.getValue().Description());
        DescriptionCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.15));

        TableColumn<ProjectItem, String> InquiryCol = new TableColumn<>("Inquiry");
        InquiryCol.setCellValueFactory(cellData -> cellData.getValue().Inquiry());
        InquiryCol.prefWidthProperty().bind(ProjectTable.widthProperty().multiply(0.15));


        ProjectTable.getColumns().addAll(ProjectCol,
                EmailCol,
                ContactCol,
                CompanyCol,
                Start_dateCol,
                End_dateCol,
                DescriptionCol,
                InquiryCol);
        ProjectTable.setItems(data);

        Button deleteItem = new Button("Delete");
        Button refreshProjects = new Button("Refresh");

        refreshProjects.setOnAction(actionEvent -> {
            try {
                loadProjects();
            } catch(Exception e){
                showAlert("Error!", "Something went wrong when loading reviews");
            }
        });

        deleteItem.setOnAction(actionEvent -> deleteProjectRequest());

        HBox optButton = new HBox(10, deleteItem, refreshProjects);
        optButton.setPadding(new Insets(10));

        VBox.setVgrow(ProjectTable, Priority.ALWAYS);
        this.getChildren().addAll(ProjectTable, optButton);
    }

    public void loadProjects() {
        try {
            // Define the target URL
            String url = "http://localhost:5001/submitRequest";

            // Create an HttpClient
            HttpClient httpClient = HttpClient.newHttpClient();

            // Build the request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Send the request and receive the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response body
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body:\n" + response.body());

            // Parse form data from the response body
            Map<String, String> formData = parseFormData(response.body());

            // Extract specific values
            String customerName = formData.get("customerName");
            String customerEmail = formData.get("customerEmail");
            String customerCell = formData.get("customerCell");
            String company = formData.get("company");
            String startDate = formData.get("startDate");
            String endDate = formData.get("endDate");
            String projectDesc = formData.get("projectDesc");
            String projectInq = formData.get("projectInq");

            // Add more keys as needed...

            // Print extracted values
            ProjectItem newProject = new ProjectItem(new SimpleStringProperty(customerName),
                    new SimpleStringProperty(customerEmail), new SimpleStringProperty(customerCell),
            new SimpleStringProperty(company), new SimpleStringProperty(startDate), new SimpleStringProperty(endDate),
            new SimpleStringProperty(projectDesc), new SimpleStringProperty(projectInq));
            data.add(newProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> parseFormData(String formDataString) throws UnsupportedEncodingException {
        Map<String, String> formData = new HashMap<>();

        // Split the formDataString into key-value pairs
        String[] pairs = formDataString.split("&");

        // Extract key-value pairs and populate the map
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                // URL decode the value if needed
                value = java.net.URLDecoder.decode(value, java.nio.charset.StandardCharsets.UTF_8.name());
                formData.put(key, value);
            }
        }

        return formData;
    }

    private void deleteProjectRequest() {
        // When item is selected ...
        ProjectItem selectedproject = ProjectTable.getSelectionModel().getSelectedItem();

        // Check if that item row is valid or does exists, if not, throw an alert
        if (selectedproject == null) {
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No project is selected!");
            noSelectedAlert.setContentText("Please select a project from the table to delete.");
            noSelectedAlert.showAndWait();
            return;
        }

        // Else, remove the item from the table
        data.remove(selectedproject);
        ProjectTable.refresh();
    }

    private void showAlert(String title, String content) {
        Alert invalidNumAlert = new Alert(Alert.AlertType.ERROR);
        invalidNumAlert.setTitle(title);
        invalidNumAlert.setHeaderText(null);
        invalidNumAlert.setContentText(content);
        invalidNumAlert.showAndWait();
    }
}
