package app.ui.components.loadouts;

import app.data.models.Item;

import java.util.Objects;

/**
 * A item container with a fixed size to match the equipment screen.
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

        getItem(1, 0).attachItem(new ItemSprite(Objects.requireNonNull(Item.load("Void ranger helm"))));
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
