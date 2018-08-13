package app.ui.pages;

import app.ui.components.loadouts.EquipmentTab;
import app.ui.components.loadouts.InventoryTab;
import app.ui.components.loadouts.ItemDragDropController;
import javafx.scene.layout.HBox;

import java.util.Objects;

/**
 * Page that can be used to show the loadout.
 * @author Brett Taylor
 */
public class LoadoutPage extends BasePage {
    /**
     * Holds both the equipment and inventory views.
     */
    private HBox itemContainersView;

    /**
     * The equipment tab view.
     */
    private EquipmentTab equipmentTab;

    /**
     * The inventory tab view.
     */
    private InventoryTab inventoryTab;

    /**
     * Constructor
     */
    public LoadoutPage() {
        super("/fxml/pages/LoadoutPage.fxml", true);

        equipmentTab = new EquipmentTab();
        inventoryTab = new InventoryTab();

        itemContainersView = (HBox) lookup("#itemContainersView");
        Objects.requireNonNull(itemContainersView);
        itemContainersView.getChildren().addAll(equipmentTab, inventoryTab);
    }

    @Override
    public void onLoaded() {
        ItemDragDropController.activate();
        setOnMouseReleased((e) -> ItemDragDropController.get().stopDragging());
    }

    @Override
    public void onRemoved() {
        ItemDragDropController.deactive();
    }
}
