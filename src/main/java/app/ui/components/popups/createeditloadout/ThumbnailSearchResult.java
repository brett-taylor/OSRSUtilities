package app.ui.components.popups.createeditloadout;

import app.data.loadouts.LoadoutThumbnailType;
import app.ui.components.loadouts.LoadoutThumbnail;
import app.utils.CSSColorParser;
import javafx.animation.FillTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * The thumbnail result that will appear in a select thumbnail popup.
 * @author Brett Taylor
 */
public class ThumbnailSearchResult extends AnchorPane {
    /**
     * The duration the hello and bye animations will last.
     */
    private static Duration ANIMATION_TIME = Duration.millis(200);

    /**
     * The background color
     */
    private final static Color BACKGROUND_COLOR = CSSColorParser.parseColor("-background-dark-color");

    /**
     * The background hover color
     */
    private final static Color BACKGROUND_HOVER_COLOR = CSSColorParser.parseColor("-background-light-color");

    /**
     * The background.
     */
    private Rectangle background;

    /**
     * The name label.
     */
    private Label nameLabel;

    /**
     * Name of what it is representing.
     */
    private String name;

    /**
     * What type it is representing.
     */
    private LoadoutThumbnailType type;

    /**
     * The popup it belongs to.
     */
    private SelectLoadoutThumbnailPopup popup;

    /**
     * Constructor.
     * @param popup The popup it belongs to.
     * @param type The type of thumbnail it will show.
     * @param fileName The name of the image if it is already downloaded.
     * @param url The address of the image if it is not downloaded.
     */
    public ThumbnailSearchResult(SelectLoadoutThumbnailPopup popup, LoadoutThumbnailType type, String fileName, String url) {
        this.popup = popup;
        this.name = fileName;
        this.type = type;
        setPrefWidth(200);
        setPrefHeight(50);

        background = new Rectangle();
        getChildren().add(background);
        background.setWidth(getPrefWidth());
        background.setHeight(getPrefHeight());
        background.setFill(BACKGROUND_COLOR);
        setNormalBorder();

        LoadoutThumbnail thumbnail = new LoadoutThumbnail(50, 46, type, fileName, url);
        getChildren().add(thumbnail);
        AnchorPane.setTopAnchor(thumbnail, 2d);
        AnchorPane.setLeftAnchor(thumbnail, 3d);

        nameLabel = new Label();
        nameLabel.setFont(new Font("Open Sans", 13));
        nameLabel.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        nameLabel.setText(fileName);
        nameLabel.setAlignment(Pos.CENTER);
        getChildren().add(nameLabel);
        AnchorPane.setTopAnchor(nameLabel, 5d);
        AnchorPane.setLeftAnchor(nameLabel, thumbnail.getPrefHeight());
        AnchorPane.setRightAnchor(nameLabel, 0d);
        AnchorPane.setBottomAnchor(nameLabel, 0d);

        Label typeLabel = new Label();
        typeLabel.setFont(new Font("Open Sans Light", 10));
        typeLabel.setTextFill(CSSColorParser.parseColor("-accent-color"));
        typeLabel.setText(type.toString());
        typeLabel.setAlignment(Pos.TOP_CENTER);
        getChildren().add(typeLabel);
        AnchorPane.setTopAnchor(typeLabel, 3d);
        AnchorPane.setLeftAnchor(typeLabel, thumbnail.getPrefHeight());
        AnchorPane.setRightAnchor(typeLabel, 0d);

        setOnMouseEntered(this::onMouseEnter);
        setOnMouseExited(this::onMouseExit);
        setOnMouseClicked(this::onMouseClicked);
    }

    /**
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The type.
     */
    public LoadoutThumbnailType getType() {
        return type;
    }

    /**
     * Selects this thumbnail search result.
     */
    public void setSelectedBorder() {
        background.setStroke(CSSColorParser.parseColor("-accent-color"));
        background.setStrokeWidth(3);
    }

    /**
     * Unselects this thumbnail search result.
     */
    public void setNormalBorder() {
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(1);
    }

    /**
     * Called when the mouse enters
     * @param e The mouse event.
     */
    private void onMouseEnter(MouseEvent e) {
        FillTransition ft = new FillTransition(ANIMATION_TIME , background, BACKGROUND_COLOR, BACKGROUND_HOVER_COLOR);
        ft.setCycleCount(1);
        ft.play();
        setCursor(Cursor.HAND);
        nameLabel.setTextFill(CSSColorParser.parseColor("-text-color"));
    }

    /**
     * Called when the mouse exits.
     * @param e The mouse event.
     */
    private void onMouseExit(MouseEvent e) {
        FillTransition ft = new FillTransition(ANIMATION_TIME , background, BACKGROUND_HOVER_COLOR, BACKGROUND_COLOR);
        ft.setCycleCount(1);
        ft.play();
        setCursor(Cursor.DEFAULT);
        nameLabel.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
    }

    /**
     * Called when the mouse clicks.
     * @param e The mouse event
     */
    private void onMouseClicked(MouseEvent e) {
        popup.thumbnailSearchResultSelected(this);
    }
}
