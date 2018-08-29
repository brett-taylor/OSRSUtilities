package app.ui.components.popups.searchitem;

/**
 * Event passed by a item searchitem popup when the user has cancelled choosing an item.
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnSelectItemCancelled {
    /**
     * Called when a item searchitem result popup when the user has cancelled choosing a item.
     */
    void onCancelled();
}
