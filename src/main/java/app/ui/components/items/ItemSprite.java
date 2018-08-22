package app.ui.components.items;

import app.data.runescape.Item;
import app.ui.components.WikiImage;

/**
 * Shows the associated item's image.
 * @author Brett Taylor
 */
public class ItemSprite extends WikiImage {
    /**
     * The item it is representing.
     */
    private Item item;

    /**
     * Constructs a ItemSprite.
     * @param item The item the item sprite should represent.
     */
    public ItemSprite(Item item) {
        super(item.getName(), item.getWikiURLEnding());
        this.item = item;
        setPrefWidth(ItemHotspot.WIDTH);
        setPrefHeight(ItemHotspot.HEIGHT);
        setMinWidth(ItemHotspot.WIDTH);
        setMinHeight(ItemHotspot.HEIGHT);
        setMaxWidth(ItemHotspot.WIDTH);
        setMaxHeight(ItemHotspot.HEIGHT);
        setMouseTransparent(true);
    }

    /**
     * @return The item it is representing.
     */
    public Item getItem() {
        return item;
    }
}
