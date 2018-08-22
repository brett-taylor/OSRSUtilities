package app.ui.components.items.containers;

import app.data.loadouts.ItemContainer;
import app.ui.components.popups.DialogBox;

import java.awt.*;

/**
 * A item container view but expects a item container with a certain size
 * and will hide certain item hotspots to make it appear like the equipment tab.
 * @author Brett Taylor
 */
public class EquipmentContainerView extends ItemContainerView {
    /**
     * Constructor.
     * @param itemContainer The item container it is representing.
     */
    public EquipmentContainerView(ItemContainer itemContainer) {
        super(itemContainer);

        if (itemContainer.getSize() != ItemContainer.EQUIPMENT_CONTAINER_SIZE) {
            DialogBox.showError("Incorrect item container given to a equipment container view.");
            return;
        }

        getItemHotspotAtPoint(new Point(0, 0)).setVisible(false); // Left of the helmet slot
        getItemHotspotAtPoint(new Point(2, 0)).setVisible(false); // Right of the helmet slot.
        getItemHotspotAtPoint(new Point(0, 3)).setVisible(false); // Left of the legs slot.
        getItemHotspotAtPoint(new Point(2, 3)).setVisible(false); // Right of the legs slot.
    }
}
