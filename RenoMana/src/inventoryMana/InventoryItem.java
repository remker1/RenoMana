package inventoryMana;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

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
     * Property fpr the id of the tool
     */
    private SimpleIntegerProperty itemID;

    /**
     * Property for the name of the item
     */
    private SimpleStringProperty itemName;
    /**
     * Property for the description of the item
     */
    private SimpleStringProperty itemDescription;

    /**
     * Property for the project that the item belongs to
     */
    private SimpleStringProperty itemProject;

    /**
     * Property fpr the SN of the tool
     */
    private SimpleStringProperty itemSerialNumber;

    /**
     * Property fpr the Model Number of the tool
     */
    private SimpleStringProperty itemModelNumber;

    /**
     *
     * @param itemID            unique id number for the item
     * @param itemName          name of the item
     * @param itemDescription   textual description of the item
     * @param itemProject       project the item belongs to
     * @param itemSerialNumber  item's serial number
     * @param itemModelNumber   item's model number
     */
    public InventoryItem(SimpleIntegerProperty itemID, SimpleStringProperty itemName, SimpleStringProperty itemDescription,
                         SimpleStringProperty itemProject, SimpleStringProperty itemSerialNumber, SimpleStringProperty itemModelNumber) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemProject = itemProject;
        this.itemSerialNumber = itemSerialNumber;
        this.itemModelNumber = itemModelNumber;
    }

    //============BASE GETTERS===============

    /**
     * This method gets the id of the item.
     *
     * @return The id of the item.
     */
    public int getItemID() {
        return itemID.get();
    }

    /**
     * This method gets the name of the item.
     *
     * @return The name of the item.
     */
    public String getItemName() {
        return itemName.get();
    }

    /**
     * This method gets the quantity of the tool.
     *
     * @return The quantity of the tool.
     */
    public String getItemDescription() {
        return itemDescription.get();
    }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public String getItemProject() { return itemProject.get(); }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public String getItemSN() { return itemSerialNumber.get(); }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public String getItemMN() { return itemModelNumber.get(); }

    //=============PROPERTY GETTERS==============

    /**
     * This method gets the id of the item.
     *
     * @return The id of the item.
     */
    public SimpleIntegerProperty itemIDProperty() {
        return itemID;
    }

    /**
     * This method gets the tool name property.
     *
     * @return The tool name property.
     */
    public SimpleStringProperty itemNameProperty() {
        return itemName;
    }

    /**
     * This method gets the quantity of the tool.
     *
     * @return The quantity of the tool.
     */
    public SimpleStringProperty itemDescriptionPropety() {
        return itemDescription;
    }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public SimpleStringProperty itemProjectPropety() {
        return itemProject;
    }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public SimpleStringProperty itemSNProperty() { return itemSerialNumber; }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public SimpleStringProperty itemMNProperty() {
        return itemModelNumber;
    }

    //==============SETTERS=============

    public void setToolID(int id) {
        this.itemID.set(id);
    }

    public void setItemName(String name) {
        this.itemName.set(name);
    }

    public void setItemDescription(String desc) {
        this.itemDescription.set(desc);
    }

    public void setItemProject(String project) { this.itemProject.set(project); }

    public void setItemSerialNumber(String sn) { this.itemSerialNumber.set(sn); }

    public void setItemModelNumber(String mn) { this.itemModelNumber.set(mn); }

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
