package app.wiki;

import utils.ItemType;

/**
 * All the different categories on the app.wiki the api allows us to access.
 * @author Brett Taylor
 */
public enum WikiCategories {
    HELMETS("Head_slot_items", ItemType.HELMETS),
    CAPES("Cape_slot_items", ItemType.CAPE),
    NECKLACE("Neck_slot_items", ItemType.NECKLACE),
    QUIVER("Ammunition_slot_items", ItemType.QUIVER),
    WEAPON("Weapon_slot_items", ItemType.WEAPON),
    TWO_HANDED_WEAPONS("Two-handed_slot_items", ItemType.WEAPON),
    TORSO("Body_slot_items", ItemType.TORSO),
    SHIELD("Shield_slot_items", ItemType.SHIELD),
    LEGS("Leg_slot_items", ItemType.LEGS),
    GLOVES("Hand_slot_items", ItemType.GLOVES),
    BOOTS("Feet_slot_items", ItemType.BOOTS),
    RING("Ring_slot_items", ItemType.RING),
    FOOD("Food", ItemType.INVENTORY_ONLY),
    POTIONS("Potions", ItemType.INVENTORY_ONLY),
    STORAGE_IETMS("Storage_items", ItemType.INVENTORY_ONLY),
    STACKABLE_IETMS("Stackable_items", ItemType.INVENTORY_ONLY);

    /**
     * The category the enum represents on the app.wiki.
     */
    private String wikiCategoryName;

    /**
     * If it is a item - the item type.
     */
    private ItemType itemType;

    /**
     * Constructor.
     * @param wikiCategoryName The category the enum represents on the app.wiki.
     * @param itemType If it is a item the type of the item.
     */
    WikiCategories(String wikiCategoryName, ItemType itemType) {
        this.wikiCategoryName = wikiCategoryName;
        this.itemType = itemType;
    }

    /**
     * @return The category the enum represents on the app.wiki.
     */
    public String getWikiCategoryName() {
        return wikiCategoryName;
    }

    /**
     * @return True if it is a item.
     */
    public boolean isItem() {
        return itemType != null;
    }

    /**
     * @return The type of the item
     */
    public ItemType typeOfItem() {
        return itemType;
    }
}
