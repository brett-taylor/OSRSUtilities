package app.ui.components;

import app.data.DataManager;
import app.ui.FXMLElement;
import app.utils.CSSColorParser;
import app.data.DownloadCategories;
import app.data.DownloadCategoriesStatus;
import com.jfoenix.controls.JFXProgressBar;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FillTransition;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.util.Objects;

public class WikiCategoryDownloadButton extends FXMLElement {
    /**
     * The time it takes to complete the hover animation time.
     */
    private static final Duration HOVER_ANIMATION_TIME = Duration.millis(300);

    /**
     * The background color
     */
    private static final Color BACKGROUND_COLOR = CSSColorParser.parseColor("-background-color");

    /**
     * The background color when hovered.
     */
    private static final Color BACKGROUND_HOVER_COLOR = CSSColorParser.parseColor("-background-light-color");

    /**
     * The wiki category it is representing.
     */
    private DownloadCategories category;

    /**
     * The main background.
     */
    private Rectangle mainBackground;

    /**
     * The progress bar
     */
    private JFXProgressBar progressBar;

    /**
     * The download information label.
     */
    private Label downloadLabel;

    /**
     * The glyph holder
     */
    private BorderPane glyphBackground;

    /**
     * The glyph.
     */
    private FontAwesomeIconView glyph;

    /**
     * Constructor
     * @param category The category it is representing.
     */
    public WikiCategoryDownloadButton(DownloadCategories category) {
        super("/fxml/components/wikiCategoryDownloadButton.fxml");
        this.category = category;

        mainBackground = (Rectangle) lookup("#mainBackground");
        Objects.requireNonNull(mainBackground);

        progressBar = (JFXProgressBar) lookup("#progressBar");
        Objects.requireNonNull(progressBar);

        Label nameLabel = (Label) lookup("#nameLabel");
        Objects.requireNonNull(nameLabel);
        nameLabel.setText(category.getNiceName());

        ImageView imageView = (ImageView) lookup("#imageView");
        Objects.requireNonNull(imageView);
        File file = new File(DataManager.DATA_LOCATION + "/Default_Images/" + category.getImageName() + ".png");
        imageView.setImage(new Image(file.toURI().toString()));

        glyphBackground = (BorderPane) lookup("#glyphBackground");
        Objects.requireNonNull(glyphBackground);

        downloadLabel = (Label) lookup("#downloadLabel");
        Objects.requireNonNull(downloadLabel);

        setOnMouseEntered(this::onMouseEnters);
        setOnMouseExited(this::onMouseExits);
        setOnMouseClicked(this::onMouseClicks);

        if (category.getDownloadStatus() == DownloadCategoriesStatus.DOWNLOADED) {
            setProgressBarProgress(1.0d);
            setLabelToDownloaded();
        }
        else {
            setProgressBarProgress(-1.0d);
            setLabelToNotDownloading();
        }
    }

    /**
     * Removes the current glyph if present.
     */
    private void removeCurrentGlyph() {
        if (glyph != null) {
            glyphBackground.setCenter(null);
            glyph = null;
        }
    }

    /**
     * Adds a glyph on top of the circle.
     * @param icon The glyph
     * @param color The color of the glyph.
     * @param size The size of the glyph.
     */
    public void setGlyph(FontAwesomeIcon icon, Color color, String size) {
        removeCurrentGlyph();

        glyph = new FontAwesomeIconView(icon);
        glyph.setSize(size);
        glyph.setMouseTransparent(true);
        glyph.setFill(color);
        glyph.setMouseTransparent(true);
        glyphBackground.setCenter(glyph);
    }

    /**
     * Sets the downloading information to not downloading.
     */
    private void setLabelToNotDownloading() {
        if (downloadLabel == null)
            return;

        downloadLabel.setText("Not Downloaded");
        downloadLabel.setTextFill(CSSColorParser.parseColor("-bad-color"));
        setGlyph(FontAwesomeIcon.TIMES, CSSColorParser.parseColor("-bad-color"), "25");
    }

    /**
     * Sets the downloading information to unknown.
     */
    private void setLabelToUnknown() {
        if (downloadLabel == null)
            return;

        downloadLabel.setText("Unknown");
        downloadLabel.setTextFill(CSSColorParser.parseColor("-alert-color"));
        setGlyph(FontAwesomeIcon.QUESTION, CSSColorParser.parseColor("-alert-color"), "20");
    }

    /**
     * Sets the downloading information to downloading.
     */
    private void setLabelToDownloading() {
        if (downloadLabel == null)
            return;

        downloadLabel.setText("Downloading");
        downloadLabel.setTextFill(CSSColorParser.parseColor("-alert-color"));
        setGlyph(FontAwesomeIcon.ARROW_DOWN, CSSColorParser.parseColor("-alert-color"), "20");
    }

    /**
     * Sets the downloading information to downloaded.
     */
    private void setLabelToDownloaded() {
        if (downloadLabel == null)
            return;

        downloadLabel.setText("Downloaded");
        downloadLabel.setTextFill(CSSColorParser.parseColor("-good-color"));
        setGlyph(FontAwesomeIcon.CHECK, CSSColorParser.parseColor("-good-color"), "20");
    }

    /**
     * Sets the progress bar's progress.
     * @param progress The progress 0-1. Pass -1.0d if you wish for a red bar to appear.
     */
    private void setProgressBarProgress(double progress) {
        if (progress == -1.0d) {
            progressBar.getStyleClass().add("wiki-category-progress-bar-no-progress");
            progressBar.getStyleClass().remove("wiki-category-progress-bar-progress");
            progressBar.getStyleClass().remove("wiki-category-progress-bar-done");
            progressBar.setProgress(1.d);
            return;
        }

       if (progress >= 1d) {
           progressBar.getStyleClass().remove("wiki-category-progress-bar-progress");
           progressBar.getStyleClass().add("wiki-category-progress-bar-done");
       } else {
           progressBar.getStyleClass().add("wiki-category-progress-bar-progress");
           progressBar.getStyleClass().remove("wiki-category-progress-bar-done");
       }

        progressBar.getStyleClass().remove("wiki-category-progress-bar-no-progress");
        progressBar.setProgress(progress);
    }

    /**
     * Called when the mouse enters the button.
     * @param e The mouse event
     */
    private void onMouseEnters(MouseEvent e) {
        FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, mainBackground, BACKGROUND_COLOR, BACKGROUND_HOVER_COLOR);
        ft.setCycleCount(1);
        ft.play();
    }

    /**
     * Called when the mouse exits the button.
     * @param e The mouse event
     */
    private void onMouseExits(MouseEvent e) {
        FillTransition ft = new FillTransition(HOVER_ANIMATION_TIME, mainBackground, BACKGROUND_HOVER_COLOR, BACKGROUND_COLOR);
        ft.setCycleCount(1);
        ft.play();
    }

    /**
     * Called when the mouse clicks the button.
     * @param e The mouse event
     */
    private void onMouseClicks(MouseEvent e) {
        if (category.getDownloadStatus() == DownloadCategoriesStatus.NOT_DOWNLOADED)
            startDownload();
        else if (category.getDownloadStatus() == DownloadCategoriesStatus.DOWNLOADED)
            removeCategoryNoticeOfDownload();
    }

    /**
     * Starts downloading this category.
     */
    private void startDownload() {
        category.setDownloadStatus(DownloadCategoriesStatus.DOWNLOADED);
        category.saveDownloadStatus();
    }

    /**
     * Resets a categorys download status to not downloaded.
     */
    private void removeCategoryNoticeOfDownload() {
        category.setDownloadStatus(DownloadCategoriesStatus.NOT_DOWNLOADED);
        category.saveDownloadStatus();
    }
}
