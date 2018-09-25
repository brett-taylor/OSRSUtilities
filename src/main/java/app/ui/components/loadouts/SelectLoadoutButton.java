package app.ui.components.buttons;

import app.OSRSUtilities;
import app.data.loadouts.Loadout;
import app.ui.FXMLElement;
import app.ui.OSRSUtilitiesWindow;
import app.ui.pages.LoadoutPage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

/**
 * Represnts a loadout on the loadout view page.
 * @author Brett Taylor
 */
public class SelectLoadoutButton extends FXMLElement {
    /**
     * The loadout it is representing.
     */
    private Loadout loadout;

    /**
     * Constructor.
     * @param loadout The loadout it should be representing.
     */
    public SelectLoadoutButton(Loadout loadout) {
        super("/fxml/components/SelectLoadoutButton.fxml");
        this.loadout = loadout;

        Button button = new Button(loadout.getName());
        button.setMouseTransparent(true);
        ((AnchorPane) getParentElement()).getChildren().add(button);

        setOnMouseClicked(this::onMouseClicked);
    }

    /**
     * Called when it is clicked.
     * @param e The mouse event
     */
    private void onMouseClicked(MouseEvent e) {
        LoadoutPage lp = new LoadoutPage(loadout);
        OSRSUtilities.getWindow().showPage(lp);
    }
}
