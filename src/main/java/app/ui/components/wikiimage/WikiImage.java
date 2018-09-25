package app.ui.components;

import app.data.ImageManager;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Wiki image. Used to show images that need to be downloaded from the wiki. Checks the image cache first.
 * If it doesn't exist it will download the image into the cache and display it while showing a spinner.
 * @author Brett Taylor
 */
public class WikiImage extends BorderPane {
    /**
     * The name of the image if it is already downloaded.
     */
    private String fileName;

    /**
     * The address of the image if it is not downloaded.
     */
    private String url;

    /**
     * The loading spinner till a image can be displayed.
     */
    private JFXSpinner spinner;

    /**
     * If true it will pick the last image it found. Useful for items not useful for monsters.
     */
    private Boolean pickLastPicture;

    /**
     * The image view.
     */
    private ImageView imageView;

    /**
     * A runnable that gets executed when the image is finally loaded.
     */
    private Runnable onImageLoaded;

    /**
     * Constructs a image box that has the ability to grab a image from the wiki and show a spinner while it downloads.
     * @param fileName The name of the image if it is already downloaded.
     * @param url The address of the image if it is not downloaded.
     * @param pickLastPicture If true it will pick the last image it found. Useful for items not useful for monsters.
     * @param onImageLoaded A runnable that gets executed when the image is finally loaded.
     */
    public WikiImage(String fileName, String url, boolean pickLastPicture, Runnable onImageLoaded) {
        this.fileName = fileName;
        this.url = url;
        this.pickLastPicture = pickLastPicture;

        if (fileName.isEmpty() || url.isEmpty())
            throw new Error("WikiImage must have a valid fileName and url.");

        new Thread(this::loadImage).start();
    }

    /**
     * Starts loading the image either from the wiki or the local cache.
     */
    private void loadImage() {
        if (ImageManager.doesImageAlreadyExist(fileName)) {
            Image image = ImageManager.getImageOnDisk(fileName);
            if (image != null) {
                Platform.runLater(() -> showImage(image));
            }
        } else {
            Platform.runLater(this::showSpinner);
            String url = ImageManager.wikiArticleImageUrl(this.url, pickLastPicture);
            ImageManager.downloadAndSaveImage(url, fileName, () -> {
                Image image = ImageManager.getImageOnDisk(fileName);
                if (image == null) {
                    return;
                }
                Platform.runLater(() -> showImage(image));
            });
        }
    }

    /**
     * Shows the image.
     * @param image the image to show.
     */
    private void showImage(Image image) {
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setMouseTransparent(true);
        setCenter(imageView);

        if (onImageLoaded != null) {
            onImageLoaded.run();
        }
    }

    /**
     * Shows a loading spinner.
     */
    private void showSpinner() {
        spinner = new JFXSpinner();
        spinner.setRadius(10d);
        spinner.setMouseTransparent(true);
        setCenter(spinner);
    }

    /**
     * @return The image view.
     */
    public ImageView getImageView() {
        return imageView;
    }
}
