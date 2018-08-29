package app.ui.components.popups.edititem;

import app.data.runescape.Item;

/**
 * Called when a on edit item popup was successful.
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnEditItemSuccess {
    /**
     * @param item The new item that was created.
     */
    void onSucecss(Item item);
}
