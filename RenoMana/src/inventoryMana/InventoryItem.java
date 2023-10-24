package inventoryMana;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InventoryItem {
    private SimpleStringProperty toolName;
    private SimpleIntegerProperty quantity;
    private SimpleIntegerProperty estimation;

    public InventoryItem(SimpleStringProperty toolName, SimpleIntegerProperty quantity, SimpleIntegerProperty estimation) {
        this.toolName = toolName;
        this.quantity = quantity;
        this.estimation = estimation;
    }

    public String getToolName() {
        return toolName.get();
    }

    public SimpleStringProperty toolNameProperty() {
        return toolName;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public int getEstimation() {
        return estimation.get();
    }

    public SimpleIntegerProperty estimationProperty() {
        return estimation;
    }
}
