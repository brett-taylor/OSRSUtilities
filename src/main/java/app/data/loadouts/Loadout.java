package app.data.loadouts;

/**
 * A loadout
 * @author Brett Taylor
 */
public class Loadout {
    /**
     * The name of the loadout.
     */
    private String name;

    /**
     * The items in the equipment tab in this loadout.
     */
    private ItemContainer equipment;

    /**
     * The items in the inventory tab in this loadout.
     */
    private ItemContainer inventory;

    /**
     * Creates a loadout.
     * @param name The name of the loadout.
     */
     public Loadout(String name) {
        this.name = name;
        equipment = new ItemContainer(ItemContainer.EQUIPMENT_CONTAINER_SIZE);
        inventory = new ItemContainer(ItemContainer.INVENTORY_CONTAINER_SIZE);
    }

    /**
     * @return The name of the loadout.
     */
    public String getName() {
         return name;
    }

    /**
     * @return The equipment item container.
     */
    public ItemContainer getEquipment() {
        return equipment;
    }

    /**
     * @return The inventory item container.
     */
    public ItemContainer getInventory() {
        return inventory;
    }
}
