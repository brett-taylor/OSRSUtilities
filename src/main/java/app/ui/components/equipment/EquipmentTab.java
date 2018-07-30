package app.ui.components.equipment;

import app.runescape.Item;

/**
 * The equipment tab.
 * Has certain inventory items hidden to recreate the equipment tab.
 * @author Brett Taylor
 */
public class EquipmentTab extends ItemContainer {
    /**
     * The amount of items in the x coordinate;
     */
    private final static int ITEMS_COUNT_X = 3;

    /**
     * The amount of items in the y coordinate;
     */
    private final static int ITEMS_COUNT_Y = 5;

    /**
     * Constructor.
     */
    public EquipmentTab() {
        super(ITEMS_COUNT_X, ITEMS_COUNT_Y);
        hideUnusedEquipmentSlots();

        getItem(1, 0).registerItem(new ItemSprite(Item.load("Void ranger helm")));
        getItem(0, 1).registerItem(new ItemSprite(Item.load("Ava's assembler")));
        getItem(1, 1).registerItem(new ItemSprite(Item.load("Necklace of anguish")));
        getItem(2, 1).registerItem(new ItemSprite(Item.load("Broad bolts")));
        getItem(0, 2).registerItem(new ItemSprite(Item.load("Dragon crossbow")));
        getItem(1, 2).registerItem(new ItemSprite(Item.load("Elite void top")));
        getItem(2, 2).registerItem(new ItemSprite(Item.load("Book of law")));
        getItem(1, 3).registerItem(new ItemSprite(Item.load("Elite void robe")));
        getItem(0, 4).registerItem(new ItemSprite(Item.load("Void knight gloves")));
        getItem(1, 4).registerItem(new ItemSprite(Item.load("Pegasian boots")));
        getItem(2, 4).registerItem(new ItemSprite(Item.load("Archers ring (i)")));
    }

    /**
     * Hide slots that are there for the inventory but not there for the equipment
     */
    private void hideUnusedEquipmentSlots() {
        // Hide to the left and right of helmet slot.
        getItem(0, 0).setVisible(false);
        getItem(2, 0).setVisible(false);

        // Hide to the left and right of legs slot.
        getItem(0, 3).setVisible(false);
        getItem(2, 3).setVisible(false);
    }
}
