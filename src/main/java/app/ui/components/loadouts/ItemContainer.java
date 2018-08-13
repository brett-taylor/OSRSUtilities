package app.ui.components.loadouts;

import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A X by X rectangle of item boxes with a fixed spacing between.
 * @author Brett Taylor
 */
public class ItemContainer extends Group {
    /**
     * The spacing between the items. This is both vertical and horizontal.
     */
    private final static int SPACING_BETWEEN_ITEMS = 3;

    /**
     * The vbox
     */
    private VBox vbox;

    /**
     * The amount of items in the horizontal axis.
     */
    private int itemsCountX = 0;

    /**
     * The amount of items in the vertical axis.
     */
    private int itemsCountY = 0;

    /**
     * The items.
     */
    private ItemHotspot[][] items;

    /**
     * Constructor.
     */
    public ItemContainer(int itemsCountX, int itemsCountY) {
        this.itemsCountX = itemsCountX;
        this.itemsCountY = itemsCountY;
        items = new ItemHotspot[itemsCountX][itemsCountY];
        vbox = new VBox();
        getChildren().add(vbox);
        vbox.setBackground(new Background(new BackgroundFill(null, null, null)));
        vbox.setSpacing(SPACING_BETWEEN_ITEMS);
        createItemElements();
    }

    /**
     * Populates the array of items and add them.
     */
    private void createItemElements() {
        for (int y = 0; y < itemsCountY; y++) {
            HBox hbox = new HBox();
            vbox.getChildren().add(hbox);
            hbox.setSpacing(SPACING_BETWEEN_ITEMS);
            for (int x = 0; x < itemsCountX; x++) {
                items[x][y] = new ItemHotspot();
                hbox.getChildren().add(items[x][y]);
            }
        }
    }

    /**
     * Gets a item square at the given coordinate. Starts from 0
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Returns the item at the given coordinates. If the coordinates are invalid it will return null;
     */
    public ItemHotspot getItem(int x, int y) {
        if (x < 0 || y < 0 || x >= itemsCountX || y >= itemsCountY)
            return null;

        return items[x][y];
    }
}
