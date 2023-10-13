package inventoryMana;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
public class Inventory extends VBox {

    private TableView<InventoryItem> inventoryTable;
    private ObservableList<InventoryItem> data;

     public Inventory(){
         // Setting up the table.
         inventoryTable = new TableView<>();
         data = FXCollections.observableArrayList();

         // Adding column names.
         TableColumn<InventoryItem, String> toolNameCol = new TableColumn<>("Tool Name");
         toolNameCol.setCellValueFactory(cellData -> cellData.getValue().toolNameProperty());
         toolNameCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.3)); // 40% width


         TableColumn<InventoryItem, Integer> quantityCol = new TableColumn<>("Total Quantity");
         quantityCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
         quantityCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.3)); // 30% width


         TableColumn<InventoryItem, Integer> estCol = new TableColumn<>("Estimation for Project");
         estCol.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
         estCol.prefWidthProperty().bind(inventoryTable.widthProperty().multiply(0.3)); // 30% width


         inventoryTable.getColumns().addAll(toolNameCol, quantityCol, estCol);
         inventoryTable.setItems(data);


         // Setting up the button options for things user can do in this tab.
         Button addItem = new Button("Add");
         addItem.setOnAction(actionEvent -> addInventoryItem());

         Button deleteItem = new Button("Delete");
         deleteItem.setOnAction(actionEvent -> deleteInvetoryItem());

         Button modifyItem = new Button("Modify");
         modifyItem.setOnAction(actionEvent -> {modifyInventoryItem();});

         Button importFile = new Button("Import");
         modifyItem.setOnAction(actionEvent -> {importInventoryFile();});

         Button exportFile = new Button("Export");
         exportFile.setOnAction(actionEvent -> {exportInventoryFile();});


         HBox optButton = new HBox(10, addItem, deleteItem, modifyItem, importFile, exportFile);
         optButton.setPadding(new Insets(10, 0, 10, 0)); // top, right, bottom, left padding


         this.getChildren().addAll(inventoryTable, optButton);
         VBox.setVgrow(inventoryTable, Priority.ALWAYS);
     }

    private void exportInventoryFile() {
    }

    private void importInventoryFile() {
    }

    private void modifyInventoryItem() {
    }

    private void deleteInvetoryItem() {
    }

    private void addInventoryItem() {
    }

}
