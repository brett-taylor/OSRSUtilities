package app.ui.components.items.containers;

import app.data.loadouts.ItemContainer;
import app.data.runescape.Item;
import app.ui.components.items.ItemHotspot;
import app.ui.components.items.ItemSprite;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * A X by X rectangle of item boxes with a fixed spacing between.
 * @author Brett Taylor
 */
public class ItemContainerView extends Group {
    /**
     * The spacing between the items. This is both vertical and horizontal.
     */
    private final static int SPACING_BETWEEN_ITEMS = 3;

    /**
     * The vbox
     */
    private VBox vbox;

    /**
     * The item container it is representing.
     */
    private ItemContainer itemContainer;

    /**
     * The positions of the item container grid and the associated item hotspot.
     */
    private Map<Point, ItemHotspot> itemHotspots;

    /**
     * Constructor.
     * @param itemContainer The item container it is representing.
     */
    public ItemContainerView(ItemContainer itemContainer) {
        this.itemContainer = itemContainer;
        itemHotspots = new HashMap<>();

        vbox = new VBox();
        getChildren().add(vbox);
        vbox.setBackground(new Background(new BackgroundFill(null, null, null)));
        vbox.setSpacing(SPACING_BETWEEN_ITEMS);
        createItemElements();
        updateItems();
    }

    /**
     * Populates the array of items and add them.
     */
    private void createItemElements() {
        for (int y = 0; y < itemContainer.getSize().y; y++) {
            HBox hbox = new HBox();
            vbox.getChildren().add(hbox);
            hbox.setSpacing(SPACING_BETWEEN_ITEMS);

            for (int x = 0; x < itemContainer.getSize().x; x++) {
                ItemHotspot ih = new ItemHotspot();
                hbox.getChildren().add(ih);
                itemHotspots.put(new Point(x, y), ih);
                ih.setOnItemHotspotItemChanged(this::onItemHotspotItemChanged);
            }
        }
    }

    /**
     * Updates the item hotspots.
     */
    private void updateItems() {
        for (int y = 0; y < itemContainer.getSize().y; y++) {
            for (int x = 0; x < itemContainer.getSize().x; x++) {
                Point point = new Point(x, y);
                ItemHotspot itemHotspot = getItemHotspotAtPoint(point);
                Item itemAtHotspot = itemContainer.getItemAtPoint(point);

                itemHotspot.unattachItem();
                if (itemAtHotspot != null) {
                    itemHotspot.attachItem(new ItemSprite(itemAtHotspot));
                }
            }
        }
    }

    /**
     * Gets the assoicated item hotspot at the given point.
     * @param point The point.
     * @return The item hotspot.
     */
    public ItemHotspot getItemHotspotAtPoint(Point point) {
        return itemHotspots.getOrDefault(point, null);
    }

    /**
     * Called when a item changes.
     * @param item The item changed.
     * @param hotspot The item hotspot that was changed.
     */
    private void onItemHotspotItemChanged(ItemHotspot hotspot, Item item) {
        for(Map.Entry<Point, ItemHotspot> set : itemHotspots.entrySet()) {
            if (set.getValue() == hotspot) {
                itemContainer.setItemAtPoint(set.getKey(), item);
            }
        }
    }

    /**
     * Attempts to add multiple items.
     * @param item The item to add.
     * @param amountToBeAdded The amount to add.
     */
    public void addMultipleItems(Item item, int amountToBeAdded) {
        int currentAmountAdded = 0;

        for (int y = 0; y < itemContainer.getSize().y; y++) {
            for (int x = 0; x < itemContainer.getSize().x; x++) {
                Point point = new Point(x, y);
                if (itemContainer.getItemAtPoint(point) == null) {
                    ItemHotspot hotspot = getItemHotspotAtPoint(point);
                    hotspot.attachItem(new ItemSprite(item));
                    currentAmountAdded++;

                    if (currentAmountAdded >= amountToBeAdded) {
                        return;
                    }
                }
            }
        }
    }
}
