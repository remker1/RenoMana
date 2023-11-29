package inventoryMana;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

     public Inventory(){
         // Setting up the table
         inventoryTable = new TableView<>();
         inventoryTable.prefWidthProperty().bind(this.widthProperty());
         data = FXCollections.observableArrayList();

         //determine which IDs are currently available
         for (int i = 1; i <= 1000; i++) {
             availableIds.add(i);
         }

         // =================COLUMN NAMES================
         TableColumn<InventoryItem, Integer> itemID = new TableColumn<>("id");
         itemID.setCellValueFactory(cellData -> cellData.getValue().itemIDProperty().asObject());
         itemID.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 30% width

         TableColumn<InventoryItem, String> itemNameCol = new TableColumn<>("Item Name");
         itemNameCol.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
         itemNameCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 40% width


         TableColumn<InventoryItem, String> itemDescCol = new TableColumn<>("Item Description");
         itemDescCol.setCellValueFactory(cellData -> cellData.getValue().itemDescriptionPropety());
         itemDescCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 30% width


         TableColumn<InventoryItem, String> itemProjectCol = new TableColumn<>("Project");
         itemProjectCol.setCellValueFactory(cellData -> cellData.getValue().itemProjectPropety());
         itemProjectCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 30% width

         TableColumn<InventoryItem, String> itemSerialCol = new TableColumn<>("Serial Number");
         itemSerialCol.setCellValueFactory(cellData -> cellData.getValue().itemSNProperty());
         itemSerialCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 30% width

         TableColumn<InventoryItem, String> itemModelCol = new TableColumn<>("Model Number");
         itemModelCol.setCellValueFactory(cellData -> cellData.getValue().itemMNProperty());
         itemModelCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.16)); // 30% width


         inventoryTable.getColumns().addAll(itemID, itemNameCol, itemDescCol, itemProjectCol, itemSerialCol, itemModelCol);
         inventoryTable.setItems(data);


         // Setting up the button options for things user can do in this tab
         HBox optButton = getOptButton();


         VBox.setVgrow(inventoryTable, Priority.ALWAYS);
         this.getChildren().addAll(inventoryTable, optButton);
     }

    private HBox getOptButton() {
        Button addItem = new Button("Add");
        addItem.setOnAction(actionEvent -> {
            try {
                addInventoryItem();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Button deleteItem = new Button("Delete");
        deleteItem.setOnAction(actionEvent -> {
            try {
                deleteInventoryItem();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
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

    private void exportInventoryFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Inventory as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // If a file is selected, write the TableView data to the file as CSV
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // Write the header and data in the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("Item ID,Item Name,Item Description,Item Project,Item Serial Number,Item Model Number\n");

                for (InventoryItem item : data) {
                    bw.write(item.getItemID() + "," + item.getItemName() + "," + item.getItemDescription() + "," +
                            item.getItemProject() + "," + item.getItemSN() + "," + item.getItemMN() + "\n");
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
                                new SimpleIntegerProperty(itemID),
                                new SimpleStringProperty(itemName),
                                new SimpleStringProperty(itemDescription),
                                new SimpleStringProperty(itemProject),
                                new SimpleStringProperty(itemSN),
                                new SimpleStringProperty(itemMN)
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

        // Check if that item row is valid or does exists, if not, alert
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

            // Modify the tool's name if the corresponding checkbox is selected.
            if (itemNameMod.isSelected()) {
                TextInputDialog nameInput = new TextInputDialog(selectedItem.getItemName());
                nameInput.setTitle("Modify Item Name");
                nameInput.setHeaderText("Enter New Item Name");
                String newName = nameInput.showAndWait().orElse("");

                // Update the tool name after validation.
                selectedItem.setItemName(newName);
            }

            // Modify the item's description, validating the input.
            if (itemDescriptionMod.isSelected()) {
                TextInputDialog descriptionInput = new TextInputDialog(selectedItem.getItemDescription());
                descriptionInput.setTitle("Modify Description");
                descriptionInput.setHeaderText("Enter New Description");
                String newDescription = descriptionInput.showAndWait().orElse("");

                selectedItem.setItemDescription(newDescription);
            }

            // Modify the item's project, validating the input.
            if (itemProjectMod.isSelected()) {
                TextInputDialog projectInput = new TextInputDialog(selectedItem.getItemProject());
                projectInput.setTitle("Modify Project");
                projectInput.setHeaderText("Enter New Project");
                String newProject = projectInput.showAndWait().orElse("");

                selectedItem.setItemProject(newProject);
            }

            // Modify the item's serial number, validating the input.
            if (itemSNMod.isSelected()) {
                TextInputDialog snInput = new TextInputDialog(selectedItem.getItemSN());
                snInput.setTitle("Modify Estimation");
                snInput.setHeaderText("Enter New Serial Number");
                String newSN = snInput.showAndWait().orElse("");

                selectedItem.setItemSerialNumber(newSN);
            }

            // Modify the item's serial number, validating the input.
            if (itemMNMod.isSelected()) {
                TextInputDialog mnInput = new TextInputDialog(selectedItem.getItemMN());
                mnInput.setTitle("Modify Estimation");
                mnInput.setHeaderText("Enter New Model Number");
                String newMN = mnInput.showAndWait().orElse("");

                selectedItem.setItemModelNumber(newMN);
            }
            try {
                syncToDatabase(Collections.singletonList(selectedItem));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
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

    private void addInventoryItem() throws IOException, InterruptedException {
        int itemID = availableIds.isEmpty() ? 1 : availableIds.first();
        availableIds.remove(itemID);

         // Gather user input for tool, quantity, and estimation values
        TextInputDialog newItemDialog = new TextInputDialog();
        newItemDialog.setTitle("Add New Item");
        newItemDialog.setHeaderText("Enter Item Name");
        String itemName = newItemDialog.showAndWait().orElse("");

        while (itemName.isEmpty()) {
            newItemDialog.setHeaderText("Item Name Cannot be Empty, Try Again!");
            itemName = newItemDialog.showAndWait().orElse("");
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

        TextInputDialog mnInput = new TextInputDialog("");
        mnInput.setHeaderText("Enter Item's Model Number");
        String modelNumber = mnInput.showAndWait().orElse("");

        // Create the InventoryItem
        InventoryItem newItem = new InventoryItem(new SimpleIntegerProperty(itemID), new SimpleStringProperty(itemName),
                new SimpleStringProperty(itemDescription), new SimpleStringProperty(itemProject), new SimpleStringProperty(serialNumber),
                new SimpleStringProperty(modelNumber));

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
