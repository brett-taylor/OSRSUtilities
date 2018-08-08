package app.ui.pages;

import app.ui.components.BufferingImage;

/**
 * Test page - no function.
 * @author Brett Taylor
 */
public class TestPage extends BasePage {
    /**
     * Constructor
     */
    public TestPage() {
        super("/fxml/pages/TestPage.fxml");
    }

    @Override
    public void onLoaded() {
        BufferingImage image = new BufferingImage();
        getChildren().add(image);
    }
}
