package utils;

/**
 * The possible slots where items can be equipped.
 * @author Brett Taylor
 */
public enum ItemType {
    HELMETS(true),
    CAPE(true),
    NECKLACE(true),
    QUIVER(true),
    WEAPON(true),
    TORSO(true),
    SHIELD(true),
    LEGS(true),
    GLOVES(true),
    BOOTS(true),
    RING(true),
    INVENTORY_ONLY(false);

    /**
     * Whether the item is wearable or not.
     */
    private boolean isWearable;

    /**
     * Constructor.
     * @param isWearable Is the item wearable.
     */
    ItemType(boolean isWearable) {
        this.isWearable = isWearable;
    }

    /**
     * @return True if the item is wearable.
     */
    public boolean isWearable() {
        return isWearable;
    }
}
