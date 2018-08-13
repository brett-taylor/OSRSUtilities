package app.ui.components.loadouts;

import app.data.models.Item;

import java.util.Objects;

/**
 * A item container with a fixed size to match the inventory screen.
 * @author Brett Taylor
 */
public class InventoryTab extends ItemContainer {
    /**
     * The amount of items in the x coordinate;
     */
    private final static int ITEMS_COUNT_X = 4;

    /**
     * The amount of items in the y coordinate;
     */
    private final static int ITEMS_COUNT_Y = 7;

    public InventoryTab() {
        super(ITEMS_COUNT_X, ITEMS_COUNT_Y);

        getItem(0, 0).attachItem(new ItemSprite(Objects.requireNonNull(Item.load("Twisted bow"))));
        getItem(0, 1).attachItem(new ItemSprite(Objects.requireNonNull(Item.load("Red partyhat"))));
        getItem(1, 0).attachItem(new ItemSprite(Objects.requireNonNull(Item.load("Purple partyhat"))));
        getItem(2, 0).attachItem(new ItemSprite(Objects.requireNonNull(Item.load("Overload"))));
        getItem(3, 0).attachItem(new ItemSprite(Objects.requireNonNull(Item.load("Dharok's helm"))));
        getItem(3, 1).attachItem(new ItemSprite(Objects.requireNonNull(Item.load("Dharok's platebody"))));
        getItem(3, 2).attachItem(new ItemSprite(Objects.requireNonNull(Item.load("Dharok's platelegs"))));
        getItem(3, 3).attachItem(new ItemSprite(Objects.requireNonNull(Item.load("Dharok's greataxe"))));
    }
}
