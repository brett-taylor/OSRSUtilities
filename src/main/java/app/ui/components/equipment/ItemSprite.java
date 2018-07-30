package app.ui.components.equipment;

import app.OSRSUtilities;
import app.data.ImageManager;
import app.runescape.Item;
import app.ui.ItemSpriteDragDropController;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

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
     * The item square the item is attached to.
     */
    private ItemSquare itemSquare;

    /**
     * Constructor.
     * @param item the item the item sprite will display - can be null.
     * If item is null then the spinner will not display As there is no image to load.
     */
    public ItemSprite(Item item) {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setMinWidth(WIDTH);
        setMinHeight(HEIGHT);
        setMaxWidth(WIDTH);
        setMaxHeight(HEIGHT);
        setBackground(new Background(new BackgroundFill(null, null, null)));

        if (item != null)
            setItem(item);
    }

    /**
     * When called will set up the item sprite in preparation to show a item.
     */
    private void setUpWithItem() {
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
                    setOnDragDetected(e -> onDragStarted());
                }
            }
        }.start();
    }

    /**
     * Loads the item image into the image sprite.
     * @param item the item to show.
     */
    public void setItem(Item item) {
        this.item = item;

        if (item == null) {
            image = null;
            imageView.setImage(null);
        } else {
            setUpWithItem();
        }
    }

    /**
     * @return The item
     */
    public Item getItem() {
        return item;
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
            createSpinner();
            String url = ImageManager.wikiArticleImageUrl(item.getWikiURLEnding());
            if (ImageManager.downloadAndSaveImage(url, item.getItemName()))
                loadImage();
        }
    }

    /**
     * Attaches the item to a square.
     * @param newItemSquare
     */
    public void attachToSquare(ItemSquare newItemSquare) {
        itemSquare = newItemSquare;
    }

    /**
     * Unattaches the item from the square.
     */
    public void unAttachFromSquare() {
        itemSquare = null;
    }

    /**
     * Called on drag started
     */
    private void onDragStarted() {
        if (itemSquare != null) {
            ItemSpriteDragDropController.startDragging(item, itemSquare);
            itemSquare.unAttachItem();
        }
    }

    /**
     * @return True if the image is currently loading.
     */
    public boolean isLoading() {
        return spinner != null;
    }
}
