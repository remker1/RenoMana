package reviewMana;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

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
        refreshReviews.setOnAction(actionEvent -> loadReviews());

        HBox optButton = new HBox(10, refreshReviews);
        optButton.setPadding(new Insets(10, 0, 10, 0)); // top, right, bottom, left padding

        // Set vertical grow for the table and add it along with the buttons to the VBox
        VBox.setVgrow(reviewTable, Priority.ALWAYS);
        this.getChildren().addAll(reviewTable, optButton);
    }

    public void loadReviews(){
        System.out.println("Refresh Page");
    }
}
