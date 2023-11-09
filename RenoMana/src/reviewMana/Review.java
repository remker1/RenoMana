package reviewMana;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * This class represents a review List UI component, which displays a list of reviews from the RenoGrp webpage
 */
public class Review extends VBox {

    /**
     * Table view for displaying reviews
     */
    private TableView<ReviewItem> reviewTable;

    /**
     * Observable list for storing review data
     */
    private ObservableList<ReviewItem> data;
    /**
     * Constructor for the EmployeeList class.
     * Initializes the UI components and sets up the employee table with columns and buttons.
     */
    public Review() {
        // Setting up the table
        reviewTable = new TableView<>();
        reviewTable.prefWidthProperty().bind(this.widthProperty());
        data = FXCollections.observableArrayList();

        // Adding column names
        TableColumn<ReviewItem, String> titleCol = new TableColumn<>("Review Title");
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        titleCol.prefWidthProperty().bind(reviewTable.widthProperty().multiply(0.20)); // 15% width

        TableColumn<ReviewItem, String> descriptionCol = new TableColumn<>("Review Description");
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        descriptionCol.prefWidthProperty().bind(reviewTable.widthProperty().multiply(0.60)); // 50% width

        TableColumn<ReviewItem, String> ratingCol = new TableColumn<>("Review Rating");
        ratingCol.setCellValueFactory(cellData -> cellData.getValue().ratingProperty());
        ratingCol.prefWidthProperty().bind(reviewTable.widthProperty().multiply(0.20)); // 15% width

        // Add columns to the table
        reviewTable.getColumns().addAll(titleCol, descriptionCol, ratingCol);
        reviewTable.setItems(data);

        // Setting up refresh reviews button
        Button refreshReviews = new Button("Refresh");
        refreshReviews.setOnAction(actionEvent -> {
            try {
                loadReviews();
            } catch(Exception e){
                System.out.println("Something went wrong when loading reviews");
            }

        });

        HBox optButton = new HBox(10, refreshReviews);
        optButton.setPadding(new Insets(10, 0, 10, 0)); // top, right, bottom, left padding

        // Set vertical grow for the table and add it along with the buttons to the VBox
        VBox.setVgrow(reviewTable, Priority.ALWAYS);
        this.getChildren().addAll(reviewTable, optButton);
    }

    public void loadReviews() throws IOException, InterruptedException {

        this.data.clear();

        String searchRating = "\"0\"";
        String msg =  "{\"rating\":" + searchRating + "}";

        System.out.println("Refreshing Review Page");

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/getReviews"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String[] responseBody = response.body().split("(?<=},)");

        for (int i = 0; i < responseBody.length; i++){
            System.out.println(i + ": ");

            // Create the review
            String title = getJsonValue(responseBody[i], "\"title\"").split("}")[0];
            String description = getJsonValue(responseBody[i], "\"description\"");
            String rating = getJsonValue(responseBody[i], "\"rating\"");
            ReviewItem newReview = new ReviewItem(new SimpleStringProperty(title),
                    new SimpleStringProperty(description), new SimpleStringProperty(rating));

            this.data.add(newReview);
            reviewTable.refresh();

        }
    }
    public String getJsonValue(String jsonString, String id){
        int startIndex = jsonString.indexOf(id);
        // Check if the key is found
        if (startIndex != -1) {
            // Move the index to the start of the value
            startIndex = jsonString.indexOf(":", startIndex) + 1;

            // Find the end of the value (up to the next comma or the end of the JSON object)
            int endIndex = jsonString.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = jsonString.indexOf("}", startIndex);
            }

            // Extract the value
            return jsonString.substring(startIndex, endIndex).trim();
        }else {
            return id + " not found in the response";
        }
    }
}
