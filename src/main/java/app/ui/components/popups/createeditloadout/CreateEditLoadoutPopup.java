package app.ui.components.popups.createeditloadout;

import app.data.loadouts.Loadout;
import app.data.loadouts.LoadoutManager;
import app.data.loadouts.LoadoutThumbnailType;
import app.data.runescape.Item;
import app.data.runescape.Monster;
import app.ui.components.buttons.CircularButton;
import app.ui.components.buttons.SquareButton;
import app.ui.components.loadouts.LoadoutThumbnail;
import app.ui.components.popups.PopupMenu;
import app.utils.CSSColorParser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Popup where a loadout can be created or edited.
 */
public class CreateEditLoadoutPopup extends PopupMenu {
    /**
     * Layout container.
     */
    private VBox layout;

    /**
     * Textfield containing the name of the loadout.
     */
    private TextField nameLoadoutField;

    /**
     * Loadout it may be representing.
     */
    private Loadout loadout;

    /**
     * The submit button.
     */
    private CircularButton submitButton;

    /**
     * Boolean checking if the button can be submitted or not
     */
    private boolean canBeSubmitted = false;

    /**
     * Thumbnail
     */
    private LoadoutThumbnail thumbnail;

    /**
     * Name of thumbnail label.
     */
    private Label thumbnailName;

    /**
     * Holds the thumbnail.
     */
    private StackPane thumbnailHolder;

    /**
     * The type of the chosen thumbnail.
     */
    private LoadoutThumbnailType chosenThumbnailType;

    /**
     * The name of the chosen thumbnail.
     */
    private String chosenThumbnailName;

    /**
     * Creates a popup that allows the user to create or edit a loadout.
     * @param loadout If a loadout is given it will edit the current loadout rather than create a new one.
     */
    public CreateEditLoadoutPopup(Loadout loadout) {
        this.loadout = loadout;
        heading.setText(loadout == null ? "Create Loadout" : "Modify Loadout: " + loadout.getName());

        CircularButton byeButton = CircularButton.failedButton();
        buttonRow.getChildren().add(byeButton);
        byeButton.setOnClicked(this::startByeAnimation);
        setOnShortcutFailed(this::startByeAnimation);

        submitButton = CircularButton.successButton();
        buttonRow.getChildren().add(submitButton);
        Runnable onSubmit = () -> {
            if (canBeSubmitted) {
                startByeAnimation();
                if (loadout == null) {
                    Loadout newLoadout = new Loadout(nameLoadoutField.getText().trim());
                    if (chosenThumbnailName != null && chosenThumbnailType != null) {
                        newLoadout.setThumbnailName(chosenThumbnailName);
                        newLoadout.setThumbnailType(chosenThumbnailType);
                    }
                    LoadoutManager.save(newLoadout);
                } else {
                    System.out.println("Add code to modify a loadout.");
                }
            }
        };

        submitButton.setOnClicked(onSubmit);
        setOnShortcutSuccess(onSubmit);

        layout = new VBox();
        mainBody.getChildren().add(layout);
        AnchorPane.setTopAnchor(layout, 0d);
        AnchorPane.setRightAnchor(layout, 0d);
        AnchorPane.setBottomAnchor(layout, 0d);
        AnchorPane.setLeftAnchor(layout, 0d);
        layout.setFillWidth(true);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(5, 5, 5,5 ));

        addNameRow();
        addSelectThumbnailRow();
        checkSubmitStatus();
    }

    /**
     * Checks to see if the dialog can be submitted or not.
     */
    private void checkSubmitStatus() {
        canBeSubmitted = true;

        if (nameLoadoutField.getText().trim().isEmpty()) {
            canBeSubmitted = false;
        }

        if (LoadoutManager.doesLoadoutExist(nameLoadoutField.getText())) {
            canBeSubmitted = false;
        }

        if (canBeSubmitted) {
            submitButton.setBackgroundColor(CSSColorParser.parseColor("-good-color"));
            submitButton.setBackgroundHoverColor(CSSColorParser.parseColor("-good-secondary-color"));
            submitButton.setGlyph(FontAwesomeIcon.CHECK, CSSColorParser.parseColor("-good-overlay-color"));
        } else {
            submitButton.setBackgroundColor(CSSColorParser.parseColor("-background-light-color"));
            submitButton.setBackgroundHoverColor(CSSColorParser.parseColor("-background-light-color"));
            submitButton.setGlyph(FontAwesomeIcon.CHECK, CSSColorParser.parseColor("-background-color"));
        }
    }

    /**
     * Adds the row dedicated to editing the name of the loadout.
     */
    private void addNameRow() {
        Label label = new Label();
        label.setFont(new Font("Open Sans", 13));
        label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        label.setText("Name of loadout");
        layout.getChildren().add(label);
        label.setPadding(new Insets(5, 5, 2,5 ));

        nameLoadoutField = new TextField();
        nameLoadoutField.setAlignment(Pos.CENTER);
        nameLoadoutField.getStyleClass().add("nice-textfield");
        nameLoadoutField.setId("dark-searchbox");
        nameLoadoutField.setPrefHeight(30d);
        nameLoadoutField.prefWidthProperty().bind(layout.widthProperty());
        nameLoadoutField.setPromptText("Name");
        nameLoadoutField.setText(loadout == null ? "" : loadout.getName());
        nameLoadoutField.setOnKeyTyped((e) -> checkSubmitStatus());
        layout.getChildren().add(nameLoadoutField);
    }

    /**
     * Adds the row dedicated to editing the thumbnail of the loadout.
     */
    private void addSelectThumbnailRow() {
        Label headingLable = new Label();
        headingLable.setFont(new Font("Open Sans", 13));
        headingLable.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        headingLable.setText("Thumbnail");
        layout.getChildren().add(headingLable);
        headingLable.setPadding(new Insets(5, 5, 2,5 ));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(3d);
        layout.getChildren().add(hBox);

        thumbnailHolder = new StackPane();
        hBox.getChildren().add(thumbnailHolder);

        SquareButton pickThumbnail = SquareButton.defaultButton();
        pickThumbnail.setSize(60, 60);
        thumbnailHolder.getChildren().add(pickThumbnail);
        pickThumbnail.setText("");
        pickThumbnail.setBackgroundColor(CSSColorParser.parseColor("-background-dark-color"));
        pickThumbnail.setBackgroundHoverColor(CSSColorParser.parseColor("-background-light-color"));
        pickThumbnail.setOnClicked(() -> {
            SelectLoadoutThumbnailPopup popup = new SelectLoadoutThumbnailPopup();
            popup.startHelloAnimation();
            this.startByeAnimation();
            popup.setOnCancelled(this::startHelloAnimation);
            popup.setOnSuccess((type, name) -> {
                this.startHelloAnimation();
                setThumbnail(type, name);
            });
        });

        thumbnailName = new Label();
        thumbnailName.setFont(new Font("Open Sans", 13));
        thumbnailName.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        thumbnailName.setText("Select Thumbnail");
        hBox.getChildren().add(thumbnailName);
        thumbnailName.setPadding(new Insets(5, 5, 2,5 ));

        SquareButton delete = new SquareButton();
        layout.getChildren().add(delete);
        delete.setSize(0, 30d);
        delete.setTextAllignment(Pos.CENTER);
        delete.setBackgroundColor(CSSColorParser.parseColor("-background-dark-color"));
        delete.setText("Delete Thumbnail");
        delete.setBackgroundColor(CSSColorParser.parseColor("-accent-color"));
        delete.setBackgroundHoverColor(CSSColorParser.parseColor("-alert-overlay-color"));
        delete.setTextColor(Color.WHITE);
        delete.setFont(new Font("Open Sans", 12));
        delete.setPadding(new Insets(5, 0, 20,0));
        delete.setOnClicked(() -> {
            chosenThumbnailType = null;
            chosenThumbnailName = null;
            thumbnailName.setText("Select Thumbnail");
            if (thumbnail != null) {
                thumbnailHolder.getChildren().remove(thumbnail);
                thumbnail = null;
            }
        });
    }

    /**
     * Sets the thumbnail
     * @param type The type of thumbnail
     * @param name The name
     */
    private void setThumbnail(LoadoutThumbnailType type, String name) {
        if (thumbnail != null) {
            thumbnailHolder.getChildren().remove(thumbnail);
            thumbnail = null;
        }

        String url = "";
        if (type == LoadoutThumbnailType.ITEM) {
            Item item = Item.load(name);
            if (item == null)
                return;

            url = item.getWikiURLEnding();
        } else if (type == LoadoutThumbnailType.MONSTER) {
            Monster monster = Monster.load(name);
            if (monster == null)
                return;

            url = monster.getWikiURLEnding();
        }

        thumbnailName.setText(name);
        thumbnail = new LoadoutThumbnail(60, 60, type, name, url);
        thumbnail.setMouseTransparent(true);
        thumbnailHolder.getChildren().add(thumbnail);

        chosenThumbnailType = type;
        chosenThumbnailName = name;
    }
}
