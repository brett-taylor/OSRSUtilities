package app.ui.pages;

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
    }

    @Override
    public void onRemoved() {
    }
}
