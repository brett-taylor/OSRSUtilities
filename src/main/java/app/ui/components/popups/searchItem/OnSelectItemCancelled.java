package app.ui.components.popups.searchItem;

/**
 * Event passed by a item searchItem popup when the user has cancelled choosing an item.
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnSelectItemCancelled {
    /**
     * Called when a item searchItem result popup when the user has cancelled choosing a item.
     */
    void onCancelled();
}
