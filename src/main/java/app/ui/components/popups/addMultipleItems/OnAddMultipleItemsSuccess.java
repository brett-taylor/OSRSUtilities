package app.ui.components.popups.addMultipleItems;

import app.data.runescape.Item;

/**
 * Called when a add multiple items popup was successful.
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnAddMultipleItemsSuccess {
    /**
     * @param item The item that was selected.
     * @param amount The amount that was selected.
     */
    void onSucecss(Item item, int amount);
}
