package inventoryMana;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InventoryItems {

    private List<InventoryItem> inventoryItems;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public InventoryItems(@JsonProperty("items") List<InventoryItem> inventoryItems){
        this.inventoryItems = inventoryItems;
    }
    public InventoryItems(){
    }

    public List<InventoryItem> getItems() {
        return inventoryItems;
    }
}
