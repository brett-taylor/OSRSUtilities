package app.data.loadouts.events;

import app.data.runescape.Item;
import app.ui.components.items.ItemHotspot;

/**
 * Event is triggered when a item changes on the item hotspot..
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnItemHotspotItemChanged {
    /**
     * @param item The item changed.
     * @param itemHotspot The item hotspot that was changed.
     */
    void onItemHotspotItemChanged(ItemHotspot itemHotspot, Item item);
}
