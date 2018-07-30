package app.ui.components.equipment;

import app.data.ImageManager;
import app.runescape.Item;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * The drag and dropable item.
 * Will display a image of the item once downloaded from the wiki.
 * Will show a loading sprite while it does this.
 * @author Brett Taylor
 */
public class ItemSprite extends BorderPane {
    /**
     * The width of the item overlay.
     */
    private final static float WIDTH = 35;

    /**
     * The height of the item overlay.
     */
    private final static float HEIGHT = 35;

    /**
     * The item the item overlay is to be.
     */
    private Item item;

    /**
     * The loading spinner till a image can be displayed.
     */
    private JFXSpinner spinner;

    /**
     * The image.
     */
    private ImageView imageView;

    /**
     * Temporally stores the image. To allow the main thread to display it.
     */
    private Image image;

    /**
     * Constructor.
     */
    public ItemSprite(Item item) {
        this.item = item;
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setMinWidth(WIDTH);
        setMinHeight(HEIGHT);
        setMaxWidth(WIDTH);
        setMaxHeight(HEIGHT);

        createSpinner();
        new Thread(this::loadImage).start();

        // Displays the image as soon as it is available then stops running.
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                if (image != null) {
                    stop();
                    destroySpinner();
                    createImageView();
                    imageView.setImage(image);
                    image = null;
                }
            }
        }.start();
    }

    /**
     * Hides the spinner.
     */
    private void destroySpinner() {
        setCenter(null);
        spinner = null;
    }

    /**
     * Shows the spinner. If the spinner does not exist it will be created.
     */
    private void createSpinner() {
        spinner = new JFXSpinner();
        spinner.getStyleClass().add("item-loading-sprite");
        setCenter(new JFXSpinner());
    }

    /**
     * Creates the image view.
     */
    private void createImageView() {
        imageView = new ImageView();
        setCenter(imageView);
    }

    /**
     * Shows a image. If needed it will download the image from its associated wikipedia article.
     */
    private void loadImage() {
        if (ImageManager.doesImageAlreadyExist(item.getItemName())) {
            Image image = ImageManager.getImageOnDisk(item.getItemName());
            if (image == null)
                return;

            this.image = image;
        } else {
            String url = ImageManager.wikiArticleImageUrl(item.getWikiURLEnding());
            if (ImageManager.downloadAndSaveImage(url, item.getItemName()))
                loadImage();
        }
    }
}
