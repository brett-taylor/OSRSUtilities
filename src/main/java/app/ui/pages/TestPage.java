package app.ui.pages;

import app.ui.components.WikiImage;
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
        WikiImage wi = new WikiImage("Red partyhat", "/wiki/Red_partyhat");
        wi.setPrefWidth(50d);
        wi.setPrefHeight(50d);
        ((AnchorPane) getParentElement()).getChildren().addAll(wi);
        AnchorPane.setLeftAnchor(wi, 100d);
        AnchorPane.setTopAnchor(wi, 100d);
    }

    @Override
    public void onRemoved() {
    }
}
