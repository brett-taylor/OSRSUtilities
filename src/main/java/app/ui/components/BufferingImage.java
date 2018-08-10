package app.ui.components;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**
 * Buffering image. Used to show images that need to be downloaded from the internet. Checks the image cache first.
 * If it doesn't exist it will download the image into the cache and display it while showing a spinner.
 * @author Brett Taylor
 */
public class BufferingImage extends AnchorPane {
    public BufferingImage() {
      setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
    }
}
