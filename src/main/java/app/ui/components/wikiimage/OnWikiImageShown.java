package app.ui.components.wikiimage;

import javafx.scene.image.ImageView;

/**
 * Called when a wiki image is downloaded if needed and shown.
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnWikiImageShown {
    /**
     * Called when a wiki image is downloaded if needed and shown.
     * @param image the wiki image.
     * @param imageView the image view that is showing the image.
     */
    void onImageShown(WikiImage image, ImageView imageView);
}
