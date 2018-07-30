package app.ui.pages;

import app.ui.components.equipment.EquipmentTab;
import app.ui.components.equipment.InventoryTab;
import io.datafx.controller.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import javax.annotation.PostConstruct;

@FXMLController("/fxml/pages/CharacterSelection.fxml")
public class CharacterSelectionController {
    /**
     * The anchor pane on the character selection screen.
     */
    @FXML
    private AnchorPane background;

    /**
     * Constructor.
     */
    @PostConstruct
    public void init() {
        InventoryTab a = new InventoryTab();
        a.setLayoutX(300);
        a.setLayoutY(5);
        background.getChildren().add(a);

        EquipmentTab b = new EquipmentTab();
        b.setLayoutX(5);
        b.setLayoutY(5);
        background.getChildren().add(b);
    }
}
