package inventoryMana;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.*;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Inventory extends VBox {

    private TreeSet<Integer> availableIds = new TreeSet<>();

    public static TableView<InventoryItem> inventoryTable;
    public static ObservableList<InventoryItem> data;

    public Inventory(List<InventoryItem> initialData) throws IOException, InterruptedException {
        // Setting up the table
        inventoryTable = new TableView<>();
        inventoryTable.prefWidthProperty().bind(this.widthProperty());
        System.out.println("[INVENTORY] About to add data to inventory");
        if (initialData != null && !initialData.isEmpty()) {
            data = FXCollections.observableArrayList(initialData);
        } else {
            data = FXCollections.observableArrayList();
        }

        System.out.println("[INVENTORY] Data added to inventory");

        //determine which IDs are currently available
        for (int i = 1; i <= 1000; i++) {
            availableIds.add(i);
        }

        // =================COLUMN NAMES================
        TableColumn<InventoryItem, Integer> itemID = new TableColumn<>("id");
        itemID.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemID()));
        itemID.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 30% width

        TableColumn<InventoryItem, String> itemNameCol = getItemNameCol();
        itemNameCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16));

        TableColumn<InventoryItem, String> itemDescCol = getItemDescCol();
        itemDescCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16));


        TableColumn<InventoryItem, String> itemProjectCol = new TableColumn<>("Project");
        itemProjectCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemProject()));
        itemProjectCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 30% width

        TableColumn<InventoryItem, String> itemSerialCol = new TableColumn<>("Serial Number");
        itemSerialCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemSN()));
        itemSerialCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 30% width

        TableColumn<InventoryItem, String> itemModelCol = new TableColumn<>("Model Number");
        itemModelCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemMN()));
        itemModelCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 30% width


        inventoryTable.getColumns().addAll(itemID, itemNameCol, itemDescCol, itemProjectCol, itemSerialCol, itemModelCol);
        inventoryTable.setItems(data);


        // Setting up the button options for things user can do in this tab
        HBox optButton = getOptButton();


        VBox.setVgrow(inventoryTable, Priority.ALWAYS);
        this.getChildren().addAll(inventoryTable, optButton);
    }


    /**
     * This method creates and returns a TableColumn for item descriptions in the inventory table.
     * It sets up how each cell in the column should display the item description so a tooltip
     * shows the full description when the user hovers over it.
     *
     * @return TableColumn for item descriptions.
     */
    private static TableColumn<InventoryItem, String> getItemDescCol() {
        TableColumn<InventoryItem, String> itemDescCol = new TableColumn<>("Item Description");
        itemDescCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemDescription()));;
        itemDescCol.setCellFactory(column -> {
            return new TableCell<InventoryItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "" : getItem()); // Set the text of the cell
                    setGraphic(null);

                    // Create and set a tooltip showing the full description
                    if (item != null) {
                        Tooltip tooltip = new Tooltip(item);
                        tooltip.setWrapText(true);
                        tooltip.setMaxWidth(300);
                        setTooltip(tooltip);
                    }
                }
            };
        });
        return itemDescCol;
    }

    /**
     * This method creates and returns a TableColumn for item names in the inventory table.
     * It sets up how each cell in the column should display the item name, so a tooltip
     * shows the full name when the user hovers over it.
     *
     * @return TableColumn for item names.
     */
    private static TableColumn<InventoryItem, String> getItemNameCol() {
        TableColumn<InventoryItem, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getItemName()));;
        itemNameCol.setCellFactory(column -> {
            return new TableCell<InventoryItem, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? "" : getItem());
                    setGraphic(null);
                    if (item != null) {
                        Tooltip tooltip = new Tooltip(item);
                        tooltip.setWrapText(true);
                        tooltip.setMaxWidth(300);
                        setTooltip(tooltip);
                    }
                }
            };
        });
        return itemNameCol;
    }

    /**
     * This method creates and returns an HBox containing buttons for
     * adding, deleting, modifying, importing, and exporting inventory items.
     * Each button is also set up with an action event that triggers the respective method.
     *
     * @return HBox containing operation buttons.
     */
    private HBox getOptButton() {
        Button addItem = new Button("Add");
        addItem.setOnAction(actionEvent -> {
            try {
                addInventoryItem();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Button deleteItem = new Button("Delete");
        deleteItem.setOnAction(actionEvent -> {
            try {
                deleteInventoryItem();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Button modifyItem = new Button("Modify");
        modifyItem.setOnAction(actionEvent -> {modifyInventoryItem();});

        Button importFile = new Button("Import");
        importFile.setOnAction(actionEvent -> {importInventoryFile();});

        Button exportFile = new Button("Export");
        exportFile.setOnAction(actionEvent -> {exportInventoryFile();});


        HBox optButton = new HBox(10, addItem, deleteItem, modifyItem, importFile, exportFile);
        optButton.setPadding(new Insets(10, 0, 10, 0)); // top, right, bottom, left padding
        return optButton;
    }

    /**
     * This method allows the user to export the current inventory data to a CSV file.
     * It will open a file chooser dialog for the user to select a save location and then writes
     * the inventory data to this file in CSV format.
     */
    private void exportInventoryFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Inventory as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // If a file is selected, write the TableView data to the file as CSV.
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // Write the header and data in the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("Item ID,Item Name,Item Description,Item Project,Item Serial Number,Item Model Number\n");

                // Looping through each item in the data and writing it to the file
                for (InventoryItem item : data) {
                    bw.write(item.getItemID() + "," + item.getItemName() + "," + item.getItemDescription() + "," +
                            item.getItemProject() + "," + item.getItemSN() + "," + item.getItemMN() + "\n");
                }

                // Show a success alert after successfully writing the file
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success!");
                successAlert.setHeaderText("Export Successful");
                successAlert.setContentText("The inventory data has been successfully exported to " + file.getName());
                successAlert.showAndWait();

            } catch (IOException e) {
                // Else, show an error alert in case of any issues during the file write
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error!");
                errorAlert.setHeaderText("File Writing Error");
                errorAlert.setContentText("There was an error writing to the file. Please try again.");
                errorAlert.showAndWait();
            }
        }
    }

    /**
     * This method allows the user to import inventory data from a CSV file.
     * It opens a file chooser dialog for the user to select a CSV file, then reads
     * the data from the file and adds it to the inventory.
     */
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
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] values = line.split(",");

                    // Check if the line has at least 6 values (for ID, Name, Desc, Project, SN, and MN) and create an
                    // InventoryItem Object from it to add in our table
                    // If not, error alert will be displayed
                    if (values.length >= 6) {
                        int itemID;
                        if (availableIds.contains(Integer.parseInt(values[0].trim()))) {
                            itemID = Integer.parseInt(values[0].trim());
                            availableIds.remove(itemID);
                        }
                        else
                            itemID = availableIds.first();
                        String itemName = values[1];
                        String itemDescription = values[2];
                        String itemProject = values[3];
                        String itemSN = values[4];
                        String itemMN = values[5];

                        InventoryItem newItem = new InventoryItem(
                                itemID,
                                itemName,
                                itemDescription,
                                itemProject,
                                itemSN,
                                itemMN
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

    /**
     * This method modifies the details of the selected inventory item. It allows the user to modify
     * the name, quantity, and estimation of an inventory item selected from the table. If no item is
     * selected, it will display an alert.
     */
    private void modifyInventoryItem() {
        // When item is selected ...
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();

        // Check if that item row is valid or does exist, if not, alert
        if (selectedItem == null) {
            Alert noSelectedAlert = new Alert(Alert.AlertType.WARNING);
            noSelectedAlert.setTitle("Error!");
            noSelectedAlert.setHeaderText("No tool is selected!");
            noSelectedAlert.setContentText("Please select a tool from the table to modify.");
            noSelectedAlert.showAndWait();
            return;
        }

        // Stage for modification options
        Stage modifyStage = new Stage();
        TilePane modifyTitle = new TilePane();

        // Set up UI elements for modifying the item.
        Label promptMessage = new Label("Select information to modify:");
        CheckBox itemNameMod = new CheckBox("Item Name");
        CheckBox itemDescriptionMod = new CheckBox("Item Description");
        CheckBox itemProjectMod = new CheckBox("Project");
        CheckBox itemSNMod = new CheckBox("Serial Number");
        CheckBox itemMNMod = new CheckBox("Model Number");

        Button doneBtn = new Button("Done");
        doneBtn.setOnAction(event -> {
            Optional<String> result;

            // Modify the tool's name if the corresponding checkbox is selected.
            if (itemNameMod.isSelected()) {
                TextInputDialog nameInput = new TextInputDialog(selectedItem.getItemName());
                nameInput.setTitle("Modify Item Name");
                nameInput.setHeaderText("Enter New Item Name");
                result = nameInput.showAndWait();
                result.ifPresent(selectedItem::setItemName);
            }

            // Modify the item's description, validating the input.
            if (itemDescriptionMod.isSelected()) {
                TextInputDialog descriptionInput = new TextInputDialog(selectedItem.getItemDescription());
                descriptionInput.setTitle("Modify Description");
                descriptionInput.setHeaderText("Enter New Description");
                result = descriptionInput.showAndWait();
                result.ifPresent(selectedItem::setItemDescription);
            }

            // Modify the item's project, validating the input.
            if (itemProjectMod.isSelected()) {
                TextInputDialog projectInput = new TextInputDialog(selectedItem.getItemProject());
                projectInput.setTitle("Modify Project");
                projectInput.setHeaderText("Enter New Project");
                result = projectInput.showAndWait();
                result.ifPresent(selectedItem::setItemProject);
            }

            // Modify the item's serial number, validating the input.
            if (itemSNMod.isSelected()) {
                TextInputDialog snInput = new TextInputDialog(selectedItem.getItemSN());
                snInput.setTitle("Modify Estimation");
                snInput.setHeaderText("Enter New Serial Number");
                result = snInput.showAndWait();
                result.ifPresent(selectedItem::setItemSerialNumber);

                result.ifPresent(newSN -> {
                    // Check if the new serial number already exists in any other item
                    boolean duplicateExists = data.stream()
                            .anyMatch(item -> item != selectedItem && item.getItemSN().equals(newSN));

                    if (duplicateExists) {
                        // Alert the user about the duplicate
                        Optional<ButtonType> confirmationResult = snDuplicateAlert("Duplicate Serial Number Detected", "An item with this serial number already exists. Do you want to continue?");
                        if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
                            // If user confirms, proceed with the change
                            selectedItem.setItemSerialNumber(newSN);
                        }
                    } else {
                        // If no duplicate, proceed with the change
                        selectedItem.setItemSerialNumber(newSN);
                    }
                });
            }

            // Modify the item's serial number, validating the input.
            if (itemMNMod.isSelected()) {
                TextInputDialog mnInput = new TextInputDialog(selectedItem.getItemMN());
                mnInput.setTitle("Modify Estimation");
                mnInput.setHeaderText("Enter New Model Number");
                result = mnInput.showAndWait();
                result.ifPresent(selectedItem::setItemModelNumber);
            }
            try {
                syncToDatabase(Collections.singletonList(selectedItem));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            inventoryTable.refresh();
            modifyStage.close();
        });

        // Add UI elements to the modification stage
        modifyTitle.getChildren().addAll(promptMessage, itemNameMod, itemDescriptionMod, itemProjectMod, itemSNMod, itemMNMod, doneBtn);

        Scene modifyScene = new Scene(modifyTitle, 200, 200);
        modifyStage.setResizable(false);
        modifyStage.setTitle("Modify Options");
        modifyStage.setScene(modifyScene);
        modifyStage.show();
    }


    /**
     * This method displays an alert dialog to the user when a duplicate serial number is detected.
     * It will ask the user to confirm whether they wish to proceed with using the duplicate serial number.
     *
     * @param Duplicate_Serial_Number_Detected The header text for the alert.
     * @param s The content text for the alert, usually asking the user if they want to continue despite the duplicate.
     * @return An Optional containing the ButtonType, which indicates the user's choice in the confirmation dialog.
     */
    private static Optional<ButtonType> snDuplicateAlert(String Duplicate_Serial_Number_Detected, String s) {
        Alert duplicateAlert = new Alert(Alert.AlertType.CONFIRMATION);
        duplicateAlert.setTitle("Duplicate Serial Number");
        duplicateAlert.setHeaderText(Duplicate_Serial_Number_Detected);
        duplicateAlert.setContentText(s);

        Optional<ButtonType> confirmationResult = duplicateAlert.showAndWait();
        return confirmationResult;
    }

    /**
     * This method removes a selected inventory item from the table. If no item is selected,
     * it shows an alert to the user asking them to select an item.
     *
     * @throws IOException          If an I/O error occurs during database synchronization.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    private void deleteInventoryItem() throws IOException, InterruptedException {
        // When item is selected ...
        InventoryItem selectedItem = inventoryTable.getSelectionModel().getSelectedItem();

        // Check if that item row is valid or does exist, if not, throw an alert
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
        availableIds.add(selectedItem.getItemID());
        syncToDatabase(selectedItem.getItemID());
        inventoryTable.refresh();
    }

    /**
     * This method adds a new item to be added to the table and database
     *
     * @throws IOException          If an I/O error occurs during database synchronization.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    private void addInventoryItem() throws IOException, InterruptedException {
        int itemID = availableIds.isEmpty() ? 1 : availableIds.first();
        availableIds.remove(itemID);

        // Gather user input for tool, quantity, and estimation values
        String itemName = null;
        while (true) { // Keep asking item name until user click `Cancel` or a valid string.
            TextInputDialog newItemDialog = new TextInputDialog();
            newItemDialog.setTitle("Add New Item");
            newItemDialog.setHeaderText(itemName == null ? "Enter Item Name" : "Item Name Cannot be Empty, Try Again!");

            Optional<String> result = newItemDialog.showAndWait();
            if (result.isPresent()) {
                itemName = result.get();
                if (!itemName.isEmpty()) {
                    break; // Exit the loop if itemName is not empty
                }
            } else {
                return; // Exit the method if user cancels or closes the dialog
            }
        }

        TextInputDialog descriptionInput = new TextInputDialog("");
        descriptionInput.setHeaderText("Enter New Description");
        String itemDescription = descriptionInput.showAndWait().orElse("");

        TextInputDialog projectInput = new TextInputDialog("");
        projectInput.setHeaderText("Enter Project to Assign Item to");
        String itemProject = projectInput.showAndWait().orElse("");

        TextInputDialog snInput = new TextInputDialog("");
        snInput.setHeaderText("Enter Item's Serial Number");
        String serialNumber = snInput.showAndWait().orElse("");

        // Check for duplicate serial number
        for (InventoryItem item : data) {
            if (item.getItemSN().equals(serialNumber)) {
                Optional<ButtonType> result = snDuplicateAlert("An item with the same serial number already exists.", "Do you want to continue, or use the Modify button instead?");
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // If user chooses to continue, continue with adding the item.
                    break;
                } else {
                    // If user cancels, stop the item addition process.
                    return;
                }
            }
        }

        TextInputDialog mnInput = new TextInputDialog("");
        mnInput.setHeaderText("Enter Item's Model Number");
        String modelNumber = mnInput.showAndWait().orElse("");

        // Create the InventoryItem
        InventoryItem newItem = new InventoryItem(itemID, itemName, itemDescription, itemProject, serialNumber, modelNumber);

        data.add(newItem);
        syncToDatabase(Collections.singletonList(newItem));
        inventoryTable.refresh();
    }

    /**
     * Reflect item deletion in database
     * @param id                    item to be deleted
     * @throws IOException          IOException
     * @throws InterruptedException InterruptedException
     */
    private void syncToDatabase(int id) throws IOException, InterruptedException {
        String msg = "{" +
                "\"deleteItemID\":\"" + id + "\"" +
                "}";
        System.out.println("[INVENTORY DELETE]: " + msg);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/syncInventoryDelete"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println("[INVENTORY]: " + request.toString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Reflect item add in database
     * @param items items to be added in database
     */
    private void syncToDatabase(List<InventoryItem> items) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String itemsJson = objectMapper.writeValueAsString(items);
        String msg = "{\"" + "items" + "\":" + itemsJson + "}";
        System.out.println("[INVENTORY ADD]: " + msg);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:5001/syncInventoryAdd"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(msg, StandardCharsets.UTF_8))
                .build();

        System.out.println("[INVENTORY]: " + request.toString());
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    }

}
