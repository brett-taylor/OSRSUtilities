package app.ui.components.popups.createeditloadout;

import app.data.loadouts.LoadoutThumbnailType;
import app.data.runescape.Item;
import app.data.runescape.Monster;
import app.ui.components.buttons.CircularButton;
import app.ui.components.buttons.SquareButton;
import app.ui.components.popups.PopupMenu;
import app.ui.components.popups.searchitem.ItemSearchResult;
import app.utils.CSSColorParser;
import com.jfoenix.controls.JFXSpinner;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Popup where a thumbnail for the loadout can be selected.
 */
public class SelectLoadoutThumbnailPopup extends PopupMenu {
    /**
     * Layout container.
     */
    private VBox layout;

    /**
     * The submit button.
     */
    private CircularButton submitButton;

    /**
     * Boolean checking if the button can be submitted or not
     */
    private boolean submitStatus = false;

    /**
     * Runnable that is executed when the selection was cancelled.
     */
    private Runnable onCancelled;

    /**
     * Runnable that is executed when the selection was successful.
     */
    private OnSelectLoadoutThumbnailSuccess onSuccess;

    /**
     * The maximum amount of objects to show.
     */
    private static int TOTAL_AMOUNT_OF_OBJECTS_TO_SHOW = 50;

    /**
     * Grid view.
     */
    private FlowPane grid;

    /**
     * The thread that the search will happen on.
     */
    private Thread searchThread;

    /**
     * The thumbnail search result that is currently selected.
     */
    private ThumbnailSearchResult currentThumbnailSearchResultSelected;

    /**
     * Creates a popup that allows the user to create or edit a loadout.
     */
    public SelectLoadoutThumbnailPopup() {
        heading.setText("Select a loadout thumbnail");
        setSize(700, 500);

        CircularButton byeButton = CircularButton.failedButton();
        buttonRow.getChildren().add(byeButton);
        byeButton.setOnClicked(() -> {
            startByeAnimation();
            if (onCancelled != null) {
                onCancelled.run();
            }
        });

        submitButton = CircularButton.successButton();
        buttonRow.getChildren().add(submitButton);
        submitButton.setOnClicked(() -> {
            if (submitStatus) {
                startByeAnimation();
                if (onSuccess != null && currentThumbnailSearchResultSelected != null) {
                    onSuccess.onSuccess(currentThumbnailSearchResultSelected.getType(), currentThumbnailSearchResultSelected.getName());
                }
            }
        });

        layout = new VBox();
        mainBody.getChildren().add(layout);
        AnchorPane.setTopAnchor(layout, 0d);
        AnchorPane.setRightAnchor(layout, 0d);
        AnchorPane.setBottomAnchor(layout, 0d);
        AnchorPane.setLeftAnchor(layout, 0d);
        layout.setFillWidth(true);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(5, 5, 5,5 ));

        enableSubmitButton(false);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for a Item, Monster or Skill");
        searchBar.getStyleClass().add("searchbox");
        searchBar.setId("dark-searchbox");
        searchBar.setPrefWidth(Integer.MAX_VALUE);
        searchBar.setPrefHeight(40d);

        SquareButton searchButton = SquareButton.defaultButton();
        searchButton.setSize(40d, 40d);
        searchButton.setText("");
        searchButton.setGlyph(FontAwesomeIcon.SEARCH, Color.WHITE);
        searchButton.setGlyphSize(18);
        searchButton.setBackgroundColor(CSSColorParser.parseColor("-good-color"));
        searchButton.setBackgroundHoverColor(CSSColorParser.parseColor("-good-secondary-color"));
        searchButton.setOnClicked(() -> search(searchBar.getText().trim()));

        HBox searchBoxContainer = new HBox();
        Objects.requireNonNull(searchBoxContainer);
        layout.getChildren().add(searchBoxContainer);
        searchBoxContainer.getChildren().add(searchBar);
        searchBoxContainer.getChildren().add(searchButton);

        ScrollPane scrollPane = new ScrollPane();
        layout.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.getStyleClass().add("edge-to-edge");
        scrollPane.getStyleClass().add("invis-scrollpane");

        grid = new FlowPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5d);
        grid.setVgap(5d);

        showCeneterLabel("Search for a Item, Monster or Skill");
    }

    /**
     * Enables or disables the submit button.
     * @param submitStatus True if the submit button should be enabled.
     */
    private void enableSubmitButton(boolean submitStatus) {
        this.submitStatus = submitStatus;
        if (this.submitStatus) {
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
     * Sets A runnable to be executed when the selection is cancelled.
     * @param onCancelled The runnable.
     */
    public void setOnCancelled(Runnable onCancelled) {
        this.onCancelled = onCancelled;
    }

    /**
     * Sets a runnable to be executed when the selection is successful.
     * @param onSuccess The runnable.
     */
    public void setOnSuccess(OnSelectLoadoutThumbnailSuccess onSuccess) {
        this.onSuccess = onSuccess;
    }

    /**
     * Shows a center label.
     * @param message the message to show
     */
    private void showCeneterLabel(String message) {
        grid.getChildren().clear();
        grid.setAlignment(Pos.CENTER);

        Label label = new Label();
        label.setText(message);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Open Sans", 13));
        label.setWrapText(true);
        label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        grid.getChildren().add(label);
    }

    /**
     * Shows a loading spinner.
     */
    private void showLoadingSpinner() {
        grid.getChildren().clear();
        grid.setAlignment(Pos.CENTER);

        JFXSpinner spinner = new JFXSpinner();
        grid.getChildren().add(spinner);
    }

    /**
     * Searches for items, monsters and skills that name contains the given phrase.
     * @param phrase the phrase
     */
    private void search(String phrase) {
        resetSelectedThumbnailSearchResult();

        if (searchThread != null) {
            searchThread.interrupt();
            searchThread = null;
        }

        if (phrase.isEmpty()) {
            showCeneterLabel("Search for a Item, Monster or Skill");
            return;
        } else {
            showLoadingSpinner();
        }

        searchThread = new Thread(() -> {
            List<Item> items = Item.loadAllItemsThatContainInName(phrase);
            List<Monster> monsters = Monster.loadAllMonstersThatContainInName(phrase);
            if (items == null) {
                Platform.runLater(() -> showCeneterLabel("A error occurred."));
                return;
            }

            List<Node> searchResults = new ArrayList<>();
            int currentAmountOfItems = 0;

            for (Item item : items) {
                if (currentAmountOfItems > TOTAL_AMOUNT_OF_OBJECTS_TO_SHOW) {
                    break;
                }

                searchResults.add(new ThumbnailSearchResult(this, LoadoutThumbnailType.ITEM, item.getName(), item.getWikiURLEnding()));
                currentAmountOfItems++;
            }

            for (Monster monster : monsters) {
                if (currentAmountOfItems > TOTAL_AMOUNT_OF_OBJECTS_TO_SHOW) {
                    break;
                }

                searchResults.add(new ThumbnailSearchResult(this, LoadoutThumbnailType.MONSTER, monster.getName(), monster.getWikiURLEnding()));
                currentAmountOfItems++;
            }

            if (currentAmountOfItems > TOTAL_AMOUNT_OF_OBJECTS_TO_SHOW) {
                Label maxObjectsShowingLabel = new Label();
                maxObjectsShowingLabel.setText("Showing maximum amount of objects - please research with a more specific term.");
                maxObjectsShowingLabel.setAlignment(Pos.CENTER);
                maxObjectsShowingLabel.setFont(new Font("Open Sans", 13));
                maxObjectsShowingLabel.setWrapText(true);
                maxObjectsShowingLabel.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
                searchResults.add(maxObjectsShowingLabel);
            }

            Platform.runLater(() -> {
                grid.getChildren().clear();
                grid.setAlignment(Pos.CENTER);
                grid.getChildren().addAll(searchResults);
            });
        });
        searchThread.start();
    }

    /**
     * Resets the currently selected thumbnail search result.
     */
    private void resetSelectedThumbnailSearchResult() {
        if (currentThumbnailSearchResultSelected != null) {
            currentThumbnailSearchResultSelected.setNormalBorder();
        }

        currentThumbnailSearchResultSelected = null;
        enableSubmitButton(false);
    }

    /**
     * Activates the given thumbnail search result.
     * @param result The thumbnail search result.
     */
    public void thumbnailSearchResultSelected(ThumbnailSearchResult result) {
        if (currentThumbnailSearchResultSelected != null) {
            resetSelectedThumbnailSearchResult();
        }

        currentThumbnailSearchResultSelected = result;
        currentThumbnailSearchResultSelected.setSelectedBorder();
        enableSubmitButton(true);
    }
}
