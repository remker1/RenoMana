package inventoryMana;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * InventoryItem Class
 *
 * <p>
 * This class represents an individual item in the inventory. Each item has a tool name,
 * a quantity, and an estimation for a project.
 * </p>
 */
public class InventoryItem {
    /**
     * Property for the name of the tool
     */
    private SimpleStringProperty toolName;
    /**
     * Property for the quantity of the tool
     */
    private SimpleIntegerProperty quantity;

    /**
     * Property for the estimation of the tool for a project
     */
    private SimpleIntegerProperty estimation;

    /**
     * Constructor for the InventoryItem class.
     *
     * @param toolName    The name of the tool.
     * @param quantity    The quantity of the tool.
     * @param estimation  The estimation of the tool for a project.
     */
    public InventoryItem(SimpleStringProperty toolName, SimpleIntegerProperty quantity, SimpleIntegerProperty estimation) {
        this.toolName = toolName;
        this.quantity = quantity;
        this.estimation = estimation;
    }

    /**
     * This method gets the name of the tool.
     *
     * @return The name of the tool.
     */
    public String getToolName() {
        return toolName.get();
    }

    /**
     * This method gets the tool name property.
     *
     * @return The tool name property.
     */
    public SimpleStringProperty toolNameProperty() {
        return toolName;
    }

    /**
     * This method gets the quantity of the tool.
     *
     * @return The quantity of the tool.
     */
    public int getQuantity() {
        return quantity.get();
    }

    /**
     * This method gets the quantity property of the tool.
     *
     * @return The quantity property.
     */
    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    /**
     * This method gets the estimation of the tool for a project.
     *
     * @return The estimation of the tool.
     */
    public int getEstimation() {
        return estimation.get();
    }

    /**
     * This method gets the estimation property of the tool for a project.
     *
     * @return The estimation property.
     */
    public SimpleIntegerProperty estimationProperty() {
        return estimation;
    }
}
