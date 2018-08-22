package app.ui.pages;

import app.ui.components.popups.searchItem.SelectItemPopup;
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
        button.setText("Click");
        ((AnchorPane) getParentElement()).getChildren().addAll(button);
        AnchorPane.setLeftAnchor(button, 300d);
        AnchorPane.setTopAnchor(button , 300d);
        button.setOnMouseClicked((e) -> {
            SelectItemPopup popup = SelectItemPopup.show();
            popup.setOnSelectItemCancelled(() -> System.out.println("Cancelled searchItem"));
            popup.setOnSelectItemConfirmed((item) -> System.out.println("Item chosne: " + item.getName()));
        });
    }

    @Override
    public void onRemoved() {
    }
}
