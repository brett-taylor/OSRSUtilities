package app.ui.components.equipment;

import app.runescape.Item;

/**
 * The inventory tab.
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

        getItem(0, 0).attachItem(new ItemSprite(Item.load("Twisted bow")));
        getItem(1, 0).attachItem(new ItemSprite(Item.load("Avernic defender")));
        getItem(2, 0).attachItem(new ItemSprite(Item.load("Ghrazi rapier")));
        getItem(3, 0).attachItem(new ItemSprite(Item.load("Sanguinesti staff")));
        getItem(0, 1).attachItem(new ItemSprite(Item.load("Scythe of vitur")));
        getItem(1, 1).attachItem(new ItemSprite(Item.load("Justiciar faceguard")));
        getItem(2, 1).attachItem(new ItemSprite(Item.load("Justiciar chestguard")));
        getItem(3, 1).attachItem(new ItemSprite(Item.load("Justiciar legguards")));

        getItem(0, 2).attachItem(new ItemSprite(Item.load("Rune full helm")));
        getItem(1, 2).attachItem(new ItemSprite(Item.load("Dragon scimitar")));
        getItem(0, 3).attachItem(new ItemSprite(Item.load("Rune platebody")));
        getItem(1, 3).attachItem(new ItemSprite(Item.load("Rune platelegs")));

        getItem(2, 5).attachItem(new ItemSprite(Item.load("Graceful boots")));
        getItem(3, 5).attachItem(new ItemSprite(Item.load("Graceful cape")));
        getItem(0, 6).attachItem(new ItemSprite(Item.load("Guthan's helm")));
        getItem(1, 6).attachItem(new ItemSprite(Item.load("Guthan's platebody")));
        getItem(2, 6).attachItem(new ItemSprite(Item.load("Guthan's chainskirt")));
        getItem(3, 6).attachItem(new ItemSprite(Item.load("Guthan's warspear")));

        getItem(0, 5).attachItem(new ItemSprite(Item.load("Rune pouch")));
        getItem(1, 5).attachItem(new ItemSprite(Item.load("Rune thrownaxe")));

        getItem(3, 4).attachItem(new ItemSprite(Item.load("Lassar teleport")));
        getItem(2, 4).attachItem(new ItemSprite(Item.load("Barrows teleport")));
        getItem(1, 4).attachItem(new ItemSprite(Item.load("Coins")));
        getItem(0, 4).attachItem(new ItemSprite(Item.load("Tokkul")));

        getItem(3, 3).attachItem(new ItemSprite(Item.load("Saradomin brew")));
        getItem(2, 3).attachItem(new ItemSprite(Item.load("Super restore")));
        getItem(2, 2).attachItem(new ItemSprite(Item.load("Prayer potion")));
    }
}
