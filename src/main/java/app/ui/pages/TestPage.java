package app.ui.pages;

import app.data.loadouts.LoadoutThumbnailType;
import app.data.runescape.Item;
import app.ui.components.items.ItemHotspot;
import app.ui.components.items.ItemHover;
import app.ui.components.items.ItemSprite;
import app.ui.components.popups.createeditloadout.CreateEditLoadoutPopup;
import app.ui.components.popups.createeditloadout.ThumbnailSearchResult;
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
        Button button = new Button();
        button.setText("Click here");
        ((AnchorPane) getParentElement()).getChildren().addAll(button);
        AnchorPane.setLeftAnchor(button, 300d);
        AnchorPane.setTopAnchor(button, 300d);

        button.setOnMouseClicked((e) -> {
            CreateEditLoadoutPopup popup = new CreateEditLoadoutPopup(null);
            popup.startHelloAnimation();
        });


        ThumbnailSearchResult result = new ThumbnailSearchResult(null, LoadoutThumbnailType.SKILL, "Fishing", "/wiki/Fishing");
        ((AnchorPane) getParentElement()).getChildren().addAll(result);
        AnchorPane.setLeftAnchor(result, 300d);
        AnchorPane.setTopAnchor(result, 500d);
    }

    @Override
    public void onRemoved() {
    }
}
