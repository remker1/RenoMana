package inventoryMana;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("itemID")
    private int itemID;

    /**
     * Property for the name of the item
     */
    @JsonProperty("itemName")
    private String itemName;
    /**
     * Property for the description of the item
     */
    @JsonProperty("itemDescription")
    private String itemDescription;

    /**
     * Property for the project that the item belongs to
     */
    @JsonProperty("itemProject")
    private String itemProject;

    /**
     * Property fpr the SN of the tool
     */
    @JsonProperty("itemSN")
    private String itemSN;

    /**
     * Property fpr the Model Number of the tool
     */
    @JsonProperty("itemMN")
    private String itemMN;

    @JsonProperty("itemQuantity")
    private int itemQuantity;

    /**
     *
     * @param itemID            unique id number for the item
     * @param itemName          name of the item
     * @param itemDescription   textual description of the item
     * @param itemProject       project the item belongs to
     * @param itemSerialNumber  item's serial number
     * @param itemModelNumber   item's model number
     */
    public InventoryItem(int itemID, String itemName, String itemDescription,
                         String itemProject, String itemSerialNumber, String itemModelNumber, int itemQuantity) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemProject = itemProject;
        this.itemSN = itemSerialNumber;
        this.itemMN = itemModelNumber;
        this.itemQuantity = itemQuantity;
    }

    public InventoryItem(){
    }
    //============BASE GETTERS===============

    /**
     * This method gets the id of the item.
     *
     * @return The id of the item.
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * This method gets the name of the item.
     *
     * @return The name of the item.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * This method gets the quantity of the tool.
     *
     * @return The quantity of the tool.
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public String getItemProject() { return itemProject; }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public String getItemSN() { return itemSN; }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public String getItemMN() { return itemMN; }

    public int getItemQuantity() {
        return itemQuantity;
    }

    //=============PROPERTY GETTERS==============

    /**
     * This method gets the id of the item.
     *
     * @return The id of the item.
     */
    public int itemIDProperty() {
        return itemID;
    }

    /**
     * This method gets the tool name property.
     *
     * @return The tool name property.
     */
    public String itemNameProperty() {
        return itemName;
    }

    /**
     * This method gets the quantity of the tool.
     *
     * @return The quantity of the tool.
     */
    public String itemDescriptionPropety() {
        return itemDescription;
    }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public String itemProjectPropety() {
        return itemProject;
    }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public String itemSNProperty() { return itemSN; }

    /**
     * This method gets the project the item belongs to.
     *
     * @return The estimation of the tool.
     */
    public String itemMNProperty() {
        return itemMN;
    }

    //==============SETTERS=============

    public void setToolID(int id) {
        this.itemID = id;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public void setItemDescription(String desc) {
        this.itemDescription = desc;
    }

    public void setItemProject(String project) { this.itemProject = project; }

    public void setItemSerialNumber(String sn) { this.itemSN = sn; }

    public void setItemModelNumber(String mn) { this.itemMN = mn; }

    public int setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
        return itemQuantity;
    }

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
