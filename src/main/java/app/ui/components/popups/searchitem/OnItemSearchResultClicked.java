package app.ui.components.popups.searchitem;

import app.data.runescape.Item;

/**
 * Event passed by a item searchitem result when it was clicked.
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnItemSearchResultClicked {
    /**
     * Called when a item searchitem result when it was clicked.
     * @param self The item searchitem result that was clicked.
     * @param item The item the item searchitem was representing.
     */
    void onClicked(ItemSearchResult self, Item item);
}
