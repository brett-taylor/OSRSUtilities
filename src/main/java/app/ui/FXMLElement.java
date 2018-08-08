package app.ui.pages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * The base page that all pages will be built of.
 * @author Brett Taylor
 */
public abstract class BasePage extends BorderPane {

    /**
     * Constructor
     * @param fxmlAddress The fxml page the base page is based on.
     */
    protected BasePage(String fxmlAddress) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlAddress));
            setCenter(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the basepage has been shown
     */
    public abstract void onLoaded();
}
