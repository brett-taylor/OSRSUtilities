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
     * The type of thumbnail it should load.
     */
    private LoadoutThumbnailType thumbnailType = LoadoutThumbnailType.NONE;

    /**
     * The name of what it should load.
     */
    private String thumbnailName = "";

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

    /**
     * @return The type of thumbnail.
     */
    public LoadoutThumbnailType getThumbnailType() {
        return thumbnailType;
    }

    /**
     * @return The thumbnail name.
     */
    public String getThumbnailName() {
        return thumbnailName.isEmpty() ? "NONE" : thumbnailName;
    }

    /**
     * Sets the thumbnail type.
     * @param type the type of thumbnail.
     */
    public void setThumbnailType(LoadoutThumbnailType type) {
        thumbnailType = type;
    }

    /**
     * Sets the name of the object the thumbnail will show from the type that has been selected.
     * @param name the name
     */
    public void setThumbnailName(String name) {
        thumbnailName = name;
    }
}
