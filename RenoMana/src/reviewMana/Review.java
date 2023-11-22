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
     * The drop-down menu that stores search rating
     */
    private ComboBox<String> selectRating;

    /**
     * Constructor for the Review class.
     * Initializes the UI components and sets up the review table with columns and buttons.
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
                showAlert("Error!", "Something went wrong when loading reviews");
            }
        });

        // Setting up rating search combo box
        selectRating = new ComboBox<>();
        selectRating.getItems().addAll("All reviews", "5 Star", "4 Star", "3 Star", "2 Star", "1 Star");
        selectRating.setValue("All reviews");

        HBox optButton = new HBox(10, selectRating, refreshReviews);

        optButton.setPadding(new Insets(10, 0, 10, 0)); // top, right, bottom, left padding

        // Set vertical grow for the table and add it along with the buttons to the VBox
        VBox.setVgrow(reviewTable, Priority.ALWAYS);
        this.getChildren().addAll(reviewTable, optButton);

        try{
            loadReviews();
        } catch (Exception e){
            showAlert("Error!", "Something went wrong when loading reviews");
        }
    }

    /**
     * Gather all reviews from Flask server and load them into the review table
     * @throws IOException
     * @throws InterruptedException
     */
    public void loadReviews() throws IOException, InterruptedException {

        // Clear the review table
        this.data.clear();

        String searchRating;
        switch (selectRating.getValue()) {
            case "5 Star" -> searchRating = "\"5\"";
            case "4 Star" -> searchRating = "\"4\"";
            case "3 Star" -> searchRating = "\"3\"";
            case "2 Star" -> searchRating = "\"2\"";
            case "1 Star" -> searchRating = "\"1\"";
            default ->  searchRating = "\"0\"";
        }


        // Build POST message
        String msg =  "{\"rating\":" + searchRating + "}";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/getReviews"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();


        // Send POST message to Flask server
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        String[] responseBody = response.body().split("(?<=},)");

        // Loop over all reviews in the response
        for (int i = 0; i < responseBody.length; i++){
            // Create the review
            String title = getJsonValue(responseBody[i], "\"title\"").split("}")[0];
            String description = getJsonValue(responseBody[i], "\"description\"");
            String rating = getJsonValue(responseBody[i], "\"rating\"");

            // Check if review server response is empty
            if (title.equals("\"title\"" + " not found in the response") ||
                    description.equals("\"description\"" + " not found in the response") ||
                    rating.equals("\"rating\"" + " not found in the response")){
                return; // Leave the table blank
            }
            ReviewItem newReview = new ReviewItem(new SimpleStringProperty(title),
                    new SimpleStringProperty(description), new SimpleStringProperty(rating));

            // Add review to the table
            this.data.add(newReview);
        }
        reviewTable.refresh();
    }

    /**
     * Find the value of the inputted key
     *
     * @param jsonString - JSON String
     * @param key - The key of the value being searched for
     * @return The value of the inputted key
     */
    public String getJsonValue(String jsonString, String key){
        int startIndex = jsonString.indexOf(key);
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
            return key + " not found in the response";
        }
    }

    /**
     * Shows an alert dialog with the specified title and content text.
     *
     * @param title   The title of the alert dialog.
     * @param content The content text of the alert dialog.
     */
    private void showAlert(String title, String content) {
        Alert invalidNumAlert = new Alert(Alert.AlertType.ERROR);
        invalidNumAlert.setTitle(title);
        invalidNumAlert.setHeaderText(null);
        invalidNumAlert.setContentText(content);
        invalidNumAlert.showAndWait();
    }
}
