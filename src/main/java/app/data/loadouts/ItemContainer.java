package app.data.loadouts;

import app.data.runescape.Item;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores items in a 2d grid.
 * The top left hand corner of the grid will be point(0, 0)
 * @author Brett Taylor
 */
public class ItemContainer {
    /**
     * The size of the equipment item container.
     */
    public final static Point EQUIPMENT_CONTAINER_SIZE = new Point(3, 5);

    /**
     * The size of the inventory item container.
     */
    public final static Point INVENTORY_CONTAINER_SIZE = new Point(4, 7);

    /**
     * Stores the point of each grid with its associated item.
     */
    private Map<Point, Item> items;

    /**
     * The size of the item container.
     */
    private Point size;

    /**
     * Creates a item container.
     * @param size The size of the item container.
     */
    public ItemContainer(Point size) {
        items = new HashMap<>();
        this.size = size;

        for (int y = 0; y < size.y; y++) {
            for (int x = 0; x < size.x; x++) {
                items.put(new Point(x, y), null);
            }
        }
    }

    /**
     * Sets the given item at the given point in the grid.
     * @param point The point where the item will be placed.
     * @param item The item in question. Can be null.
     */
    public void setItemAtPoint(Point point, Item item) {
        if (!items.containsKey(point)) {
            return;
        }

        items.remove(point);
        items.put(point, item);
    }

    /**
     * Returns the item at a given point.
     * @param point
     * @return
     */
    public Item getItemAtPoint(Point point) {
        return items.getOrDefault(point, null);
    }

    /**
     * Place the following array of items into the item container.
     * It will fill up every column in the top most row going down to the bottom most row.
     * If the item array is bigger than the item container then some items will be left out.
     * @param items The item array.
     */
    public void placeItems(Item[] items) {
        int positionInArray = 0;
        for (int y = 0; y < size.y; y++) {
            for (int x = 0; x < size.x; x++) {
                setItemAtPoint(new Point(x, y), items[positionInArray]);
                positionInArray++;
            }
        }
    }

    /**
     * @return The size of the item container.
     */
    public Point getSize() {
        return size;
    }

    /**
     * Gets all the items as an array.
     * @return The item array.
     */
    public Item[] getItems() {
        Item[] itemArray = new Item[items.size()];
        int positionInArray = 0;
        for (int y = 0; y < size.y; y++) {
            for (int x = 0; x < size.x; x++) {
                Item i = getItemAtPoint(new Point(x, y));
                itemArray[positionInArray] = i;
                positionInArray++;
            }
        }

        return itemArray;
    }
}
