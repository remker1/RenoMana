import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Report extends VBox {

    public Report() {
        // Set padding and spacing for the VBox layout
        this.setPadding(new Insets(15, 15, 15, 15));
        this.setSpacing(10);

        // Input Frequency Box
        HBox frequencyBox = new HBox();
        frequencyBox.setSpacing(10); // Set spacing between elements in HBox
        Label frequencyLabel = new Label("Send Report Frequency: ");
        ComboBox<String> timeFrameDropdown = new ComboBox<>();
        timeFrameDropdown.getItems().addAll("Daily", "Monthly", "Yearly");
        frequencyBox.getChildren().addAll(frequencyLabel, timeFrameDropdown);

        // Checkboxes
        CheckBox inventoryCheckbox = new CheckBox("Include Inventory List");
        CheckBox deadlineCheckbox = new CheckBox("Include Project Deadlines");

        // Send Report Button
        Button sendReportButton = new Button("Send Report");
        sendReportButton.setOnAction(e -> sendReport());

        // Adding components to the VBox layout
        this.getChildren().addAll(frequencyBox, inventoryCheckbox, deadlineCheckbox, sendReportButton);
    }

    private String fetchInventoryData() {
        // Fetch inventory data from database
        return null;
    }

    private String fetchProjectData() {
        // Fetch project data from database
        return null;
    }

    private void createReport() {
        // Create report (PDF or other formats)
    }

    private void sendReport() {
        // Send the report
    }
}
