package inventoryMana;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;

import java.io.*;

public class Inventory extends VBox {

    public static TableView<InventoryItem> inventoryTable;
    public static ObservableList<InventoryItem> data;

     public Inventory(){
         // Setting up the table
         inventoryTable = new TableView<>();
         inventoryTable.prefWidthProperty().bind(this.widthProperty());
         data = FXCollections.observableArrayList();

         // Adding column names
         TableColumn<InventoryItem, String> toolNameCol = new TableColumn<>("Tool Name");
         toolNameCol.setCellValueFactory(cellData -> cellData.getValue().toolNameProperty());
         toolNameCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.3)); // 40% width


         TableColumn<InventoryItem, Integer> quantityCol = new TableColumn<>("Total Quantity");
         quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
         quantityCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.3)); // 30% width


         TableColumn<InventoryItem, Integer> estCol = new TableColumn<>("Estimation for Project");
         estCol.setCellValueFactory(cellData -> cellData.getValue().estimationProperty().asObject());
         estCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.3)); // 30% width


         inventoryTable.getColumns().addAll(toolNameCol, quantityCol, estCol);
         inventoryTable.setItems(data);


         // Setting up the button options for things user can do in this tab
         Button addItem = new Button("Add");
         addItem.setOnAction(actionEvent -> addInventoryItem());

         Button deleteItem = new Button("Delete");
         deleteItem.setOnAction(actionEvent -> deleteInventoryItem());

         Button modifyItem = new Button("Modify");
         modifyItem.setOnAction(actionEvent -> {modifyInventoryItem();});

         Button importFile = new Button("Import");
         importFile.setOnAction(actionEvent -> {importInventoryFile();});

         Button exportFile = new Button("Export");
         exportFile.setOnAction(actionEvent -> {exportInventoryFile();});


         HBox optButton = new HBox(10, addItem, deleteItem, modifyItem, importFile, exportFile);
         optButton.setPadding(new Insets(10, 0, 10, 0)); // top, right, bottom, left padding


         VBox.setVgrow(inventoryTable, Priority.ALWAYS);
         this.getChildren().addAll(inventoryTable, optButton);
     }


    private void exportInventoryFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Inventory as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // If a file is selected, write the TableView data to the file as CSV
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // Write the header and data in the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("Tool Name,Total Quantity,Estimation for Project\n");

                for (InventoryItem item : data) {
                    bw.write(item.getToolName() + "," + item.getQuantity() + "," + item.getEstimation() + "\n");
                }

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success!");
                successAlert.setHeaderText("Export Successful");
                successAlert.setContentText("The inventory data has been successfully exported to " + file.getName());
                successAlert.showAndWait();

            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error!");
                errorAlert.setHeaderText("File Writing Error");
                errorAlert.setContentText("There was an error writing to the file. Please try again.");
                errorAlert.showAndWait();
            }
        }
    }
    private void importInventoryFile() {
        // Create a new FileChooser object to let the user select a file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open an Inventory File");

        // Defining the types of files the user can select in the FileChooser
        // Only CSV file is supported right now
        FileChooser.ExtensionFilter csvFilter =
                new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        fileChooser.getExtensionFilters().addAll(csvFilter);

        // gathering the file selected by user to extract data from and add to our table
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;

                // Read each line of the file until there are no more lines left
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");

                    // Check if the line has at least 3 values (for toolName, quantity, and estimation) and create an
                    // InventoryItem Object from it to add in our table
                    // If not, error alert will be displayed
                    if (values.length >= 3) {
                        String toolName = values[0];
                        int quantity = Integer.parseInt(values[1].trim());
                        int estimation = Integer.parseInt(values[2].trim());

                        InventoryItem newItem = new InventoryItem(
                                new SimpleStringProperty(toolName),
                                new SimpleIntegerProperty(quantity),
                                new SimpleIntegerProperty(estimation)
                        );

                        this.data.add(newItem);
                    }
                }

                inventoryTable.refresh();

            } catch (IOException | NumberFormatException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error!");
                errorAlert.setHeaderText("File Reading Error");
                errorAlert.setContentText("There was an error reading the file. Please ensure it's a valid CSV file and " +
                        "try again.");
                errorAlert.showAndWait();
            }
        }

    }

    private void modifyInventoryItem() {
        // When item is selected ...
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();

        // Check if that item row is valid or does exists, if not, throw an alert
        if (selectedItem == null) {
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No tool is selected!");
            noSelectedAlert.setContentText("Please select a tool from the table to modify.");
            noSelectedAlert.showAndWait();
            return;
        }

        // Else, get new user values
        TextInputDialog toolInput = new TextInputDialog();
        toolInput.setTitle("Modify Tool");
        toolInput.setHeaderText("Enter New Tool Name");
        String newToolName = toolInput.showAndWait().orElse("");

        // Error Handling: If tool name is already in the table (excluding the currently selected item)
        for (InventoryItem item : this.data) {
            if (item.getToolName().equals(newToolName) && item != selectedItem){
                Alert duplicateAlert = new Alert(Alert.AlertType.ERROR);
                duplicateAlert.setTitle("Error!");
                duplicateAlert.setHeaderText("Tool already exists!");
                duplicateAlert.setContentText("Please choose a different tool name.");
                duplicateAlert.showAndWait();
                return;
            }
        }

        int newQuantity = 0;
        int newEstimation = 0;

        try {
            TextInputDialog quantityInput = new TextInputDialog("0");
            quantityInput.setHeaderText("Enter New Quality");
            newQuantity = Integer.parseInt(quantityInput.showAndWait().orElse("Invalid Input!"));

            if (newQuantity < 0) {
                throw new NumberFormatException();
            }

            TextInputDialog estimateInput = new TextInputDialog("0");
            estimateInput.setHeaderText("Enter New Estimate");
            newEstimation = Integer.parseInt(estimateInput.showAndWait().orElse("Invalid Input!"));

            if (newEstimation < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert invalidNumAlert = new Alert(Alert.AlertType.ERROR);
            invalidNumAlert.setTitle("Error!");
            invalidNumAlert.setHeaderText("Invalid Input!");
            invalidNumAlert.setContentText("Please enter a valid positive integer for quantity and estimation.");
            invalidNumAlert.showAndWait();
            return;
        }

        // Update the InventoryItem
        selectedItem.toolNameProperty().set(newToolName);
        selectedItem.quantityProperty().set(newQuantity);
        selectedItem.estimationProperty().set(newEstimation);
        inventoryTable.refresh();

    }

    private void deleteInventoryItem() {
         // When item is selected ...
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();

        // Check if that item row is valid or does exists, if not, throw an alert
        if (selectedItem == null) {
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No tool is selected!");
            noSelectedAlert.setContentText("Please select a tool from the table to delete.");
            noSelectedAlert.showAndWait();
            return;
        }

        // Else, remove the item from the table
        data.remove(selectedItem);
        inventoryTable.refresh();
    }

    private void addInventoryItem() {
         // Gather user input for tool, quantity, and estimation values
        TextInputDialog toolInput = new TextInputDialog();
        toolInput.setTitle("Add New Tool");
        toolInput.setHeaderText("Enter Tool Name");
        String toolName = toolInput.showAndWait().orElse("");

        // Error Handling: If tool name is already in the table
        for (InventoryItem item : this.data) {
            if (item.getToolName().equals(toolName)){
                Alert duplicateAlert = new Alert(Alert.AlertType.ERROR);
                duplicateAlert.setTitle("Error!");
                duplicateAlert.setHeaderText("Tool already exists!");
                duplicateAlert.setContentText("Please use the modify button to change the existing tool's " +
                        "details instead!");
                duplicateAlert.showAndWait();
                return;
            }
        }

        int quantity = 0;
        int estimation = 0;

        try {
            TextInputDialog quantityInput = new TextInputDialog("0");
            quantityInput.setHeaderText("Enter Quality");
            quantity = Integer.parseInt(quantityInput.showAndWait().orElse("Invalid Input!"));

            if (quantity < 0) {
                throw new NumberFormatException();
            }

            TextInputDialog estimateInput = new TextInputDialog("0");
            estimateInput.setHeaderText("Enter Estimate");
            estimation = Integer.parseInt(estimateInput.showAndWait().orElse("Invalid Input!"));

            if (estimation < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Alert invalidNumAlert = new Alert(Alert.AlertType.ERROR);
            invalidNumAlert.setTitle("Error!");
            invalidNumAlert.setHeaderText("Invalid Input!");
            invalidNumAlert.setContentText("Please enter a valid positive integer for quantity and estimation.");
            invalidNumAlert.showAndWait();
            return;
        }

        // Create the InventoryItem
        InventoryItem newItem = new InventoryItem(new SimpleStringProperty(toolName),
                new SimpleIntegerProperty(quantity), new SimpleIntegerProperty(estimation));

        this.data.add(newItem);
        inventoryTable.refresh();

    }

}
