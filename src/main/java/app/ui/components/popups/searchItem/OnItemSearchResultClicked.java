package app.ui.components.popups.searchItem;

import app.data.runescape.Item;

/**
 * Event passed by a item searchItem result when it was clicked.
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnItemSearchResultClicked {
    /**
     * Called when a item searchItem result when it was clicked.
     * @param self The item searchItem result that was clicked.
     * @param item The item the item searchItem was representing.
     */
    void onClicked(ItemSearchResult self, Item item);
}
