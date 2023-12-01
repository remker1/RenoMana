package inquiryMana;

import ManagerCheck.ManagerCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import reviewMana.ReviewItems;
import timeMana.Project;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class inquiry extends VBox {

    private TableView<inquiryitem> inquiryTable;
    private ObservableList<inquiryitem> data;

    public inquiry() throws IOException, InterruptedException {
        // Setting up the table
        inquiryTable = new TableView<>();
        inquiryTable.prefWidthProperty().bind(this.widthProperty());
        data = FXCollections.observableArrayList();

        // Adding column names
        TableColumn<inquiryitem, String> NameCol = new TableColumn<>("Name");
        NameCol.setCellValueFactory(cellData -> cellData.getValue().Name());
        NameCol.prefWidthProperty().bind(inquiryTable.widthProperty().multiply(0.3));

        TableColumn<inquiryitem, String> ContactCol = new TableColumn<>("Contact Info");
        ContactCol.setCellValueFactory(cellData -> cellData.getValue().Contact());
        ContactCol.prefWidthProperty().bind(inquiryTable.widthProperty().multiply(0.3));

        TableColumn<inquiryitem, String> InquiryCol = new TableColumn<>("Inquiries");
        InquiryCol.setCellValueFactory(cellData -> cellData.getValue().Inquiry());
        InquiryCol.prefWidthProperty().bind(inquiryTable.widthProperty().multiply(0.45));


        inquiryTable.getColumns().addAll(NameCol, ContactCol,
                InquiryCol);
        inquiryTable.setItems(data);

        Button deleteItem = new Button("Delete");
        Button refresh_inquiries = new Button("Refresh");

        loadProjects();

        refresh_inquiries.setOnAction(actionEvent -> {
            try {
                loadProjects();
            } catch(Exception e){
                showAlert("Error!", "Something went wrong when loading reviews");
            }
        });


        deleteItem.setOnAction(actionEvent -> deleteProjectRequest());


        HBox optButton = new HBox(10, refresh_inquiries);
        if (ManagerCheck.isManager()){
            optButton = new HBox(10, deleteItem, refresh_inquiries);
        }
        optButton.setPadding(new Insets(10));

        VBox.setVgrow(inquiryTable, Priority.ALWAYS);
        this.getChildren().addAll(inquiryTable, optButton);
    }

    public void loadProjects() throws IOException, InterruptedException {
        this.data.clear();

        String url = "http://localhost:5001/submitinquiry";

        // Create an HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Send GET request to Flask server
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Decode GET response and add new inquiries to the table data
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Deserialize the JSON response into InquiryItems
            inquiryitems inquiries = mapper.readValue(response.body(), inquiryitems.class);

            // Get the current inquiries in the table
            ObservableList<inquiryitem> currentInquiries = FXCollections.observableArrayList(inquiryTable.getItems());

            // Add only new inquiries to the table data
            for (inquiryitem inquiry : inquiries.getinquiries()) {
                if (!currentInquiries.contains(inquiry)) {
                    data.add(inquiry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Refresh table
        inquiryTable.refresh();
    }




    private void deleteProjectRequest() {
        // When item is selected ...
        inquiryitem selected_inquiry = inquiryTable.getSelectionModel().getSelectedItem();

        // Check if that item row is valid or does exists, if not, throw an alert
        if (selected_inquiry == null) {
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No inquiry is selected!");
            noSelectedAlert.setContentText("Please select an inquiry from the table to delete.");
            noSelectedAlert.showAndWait();
            return;
        }

        // Else, remove the item from the table
        data.remove(selected_inquiry);

        String msg =  "{\"Name\": \"" + selected_inquiry.Name().get() + "\", \"Contact\": \"" + selected_inquiry.Contact().get()+ "\", \"projectInq\": \"" + selected_inquiry.Inquiry().get() + "\"}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/deleteInquiry"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();


        // Send POST message to Flask server
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        inquiryTable.refresh();
    }

    private void showAlert(String title, String content) {
        Alert invalidNumAlert = new Alert(Alert.AlertType.ERROR);
        invalidNumAlert.setTitle(title);
        invalidNumAlert.setHeaderText(null);
        invalidNumAlert.setContentText(content);
        invalidNumAlert.showAndWait();
    }
}
