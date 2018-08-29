package app.ui.components.popups.searchitem;

import app.data.runescape.Item;

/**
 * Event passed by a item searchitem popup when the user has successfully chosen an item.
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnSelectItemConfirmed {
    /**
     * Called when a item searchitem result popup when the user has successfully chosen a item.
     * @param item The item the user chose
     */
    void onSuccess(Item item);
}
