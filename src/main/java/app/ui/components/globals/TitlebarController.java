package app.ui.components.globals;

import app.OSRSUtilities;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The titlebar controller.
 * @author Brett Taylor
 */
public class TitlebarController implements Initializable {
    /**
     * Required method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Called when the close button is clicked.
     * @param event The event passed.
     */
    @FXML
    public void onClosedClicked(ActionEvent event) {
        OSRSUtilities.close();
    }
}
