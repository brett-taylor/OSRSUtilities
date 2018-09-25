package app.ui.components.loadouts;

import app.data.DataManager;
import app.data.loadouts.LoadoutThumbnailType;
import app.ui.components.wikiimage.OnWikiImageShown;
import app.ui.components.wikiimage.WikiImage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;

/**
 * Thumbnail for loadouts. Capable of showing items, monsters and skill images.
 * @author Brett
 */
public class LoadoutThumbnail extends AnchorPane {
    /**
     * Creates and loads the wiki image.
     * @param width Width
     * @param height Height
     * @param type The type of thumbnail it will show.
     * @param fileName The name of the image if it is already downloaded.
     * @param url The address of the image if it is not downloaded.
     */
    public LoadoutThumbnail(double width, double height, LoadoutThumbnailType type, String fileName, String url) {
        setPrefWidth(width);
        setPrefHeight(height);
        setMouseTransparent(true);

        if (type == LoadoutThumbnailType.SKILL) {
            File file = new File(DataManager.DATA_LOCATION + "/Default_Images/" + fileName + ".png");
            if (file.exists()) {
                ImageView imageView = new ImageView();
                imageView.setImage(new Image(file.toURI().toString()));
                imageView.setMouseTransparent(true);
                imageView.setFitHeight(height);
                imageView.setPreserveRatio(true);

                getChildren().add(imageView);
                AnchorPane.setLeftAnchor(imageView, 0d);
                AnchorPane.setTopAnchor(imageView, 0d);
            }
        } else {
            WikiImage image;
            OnWikiImageShown onImageLoaded = (wikiImage, imageView) -> {
                imageView.setFitHeight(height);
                imageView.setPreserveRatio(true);
            };

            if (type == LoadoutThumbnailType.ITEM) {
                image = new WikiImage(fileName, url, true, onImageLoaded);
            }
            else {
                image = new WikiImage(fileName, url, false, onImageLoaded);
            }

            getChildren().add(image);
            AnchorPane.setLeftAnchor(image, 0d);
            AnchorPane.setTopAnchor(image, 0d);
        }
    }
}
