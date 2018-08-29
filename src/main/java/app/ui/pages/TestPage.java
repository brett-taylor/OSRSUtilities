package app.ui.pages;

import app.data.runescape.Item;
import app.ui.components.items.ItemHotspot;
import app.ui.components.items.ItemHover;
import app.ui.components.items.ItemSprite;
import app.ui.components.popups.searchitem.SelectItemPopup;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * Test page - no function.
 * @author Brett Taylor
 */
public class TestPage extends BasePage {
    /**
     * Constructor
     */
    public TestPage() {
        super("/fxml/pages/TestPage.fxml", true);
    }

    @Override
    public void onLoaded() {
        ItemHotspot ih = new ItemHotspot();
        ((AnchorPane) getParentElement()).getChildren().addAll(ih);
        AnchorPane.setLeftAnchor(ih, 300d);
        AnchorPane.setTopAnchor(ih, 300d);

        Item item = Item.load("Rune pouch");
        item.setStackSize(15643763);
        item.getItemsInside().add(Item.load("Fire rune", 12500));
        item.getItemsInside().add(Item.load("Law rune", 12500000));
        item.getItemsInside().add(Item.load("Air rune", 125000));

        ih.attachItem(new ItemSprite(item));

        ItemHover itemHover = new ItemHover(item);
    }

    @Override
    public void onRemoved() {
    }
}
