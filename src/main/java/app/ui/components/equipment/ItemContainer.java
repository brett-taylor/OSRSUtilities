package app.ui.components.equipment;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A X by X rectangle of item boxes with a fixed spacing between.
 * @author Brett Taylor
 */
public abstract class ItemContainer extends VBox {
    /**
     * The spacing between the items. This is both vertical and horizontal.
     */
    private final static int SPACING_BETWEEN_ITEMS = 3;

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
    private ItemSquare[][] items;

    /**
     * Constructor.
     */
    public ItemContainer(int itemsCountX, int itemsCountY) {
        this.itemsCountX = itemsCountX;
        this.itemsCountY = itemsCountY;
        items = new ItemSquare[itemsCountX][itemsCountY];
        setBackground(new Background(new BackgroundFill(null, null, null)));
        setSpacing(SPACING_BETWEEN_ITEMS);
        createItemElements();
    }

    /**
     * Populates the array of items and add them.
     */
    private void createItemElements() {
        for (int y = 0; y < itemsCountY; y++) {
            HBox hbox = new HBox();
            getChildren().add(hbox);
            hbox.setSpacing(SPACING_BETWEEN_ITEMS);
            for (int x = 0; x < itemsCountX; x++) {
                items[x][y] = new ItemSquare();
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
    public ItemSquare getItem(int x, int y) {
        if (x < 0 || y < 0 || x >= itemsCountX || y >= itemsCountY)
            return null;

        return items[x][y];
    }
}
