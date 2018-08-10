package app.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

import java.io.IOException;

/**
 * Basic fxml element that will be loaded and displayed in the center of a border pane.
 * @author Brett Taylor
 */
public abstract class FXMLElement extends BorderPane {
    /**
     * The parent.
     */
    private Region parent;

    /**
     * Constructor
     * @param fxmlAddress The fxml page.
     */
    protected FXMLElement(String fxmlAddress) {
        try {
            parent = FXMLLoader.load(getClass().getResource(fxmlAddress));
            setCenter(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the parent.
     */
    public Region getParentElement() {
        return parent;
    }
}
