package app.ui.components.popups.searchitem;

import app.OSRSUtilities;
import app.data.runescape.Item;
import app.ui.FXMLElement;
import app.ui.components.buttons.CircularButton;
import app.ui.components.buttons.SquareButton;
import app.utils.CSSColorParser;
import com.jfoenix.controls.JFXSpinner;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.*;

/**
 * Popup that allows the user to select a item.
 * TO:DO Rewrite to be inherit popup menu rather than be its own fxml element.
 * @author Brett Taylor
 */
public class SelectItemPopup extends FXMLElement {
    /**
     * The duration the hello and bye animations will last.
     */
    private static Duration HELLO_BYE_ANIMATION_TIME = Duration.millis(200);

    /**
     * The y position of where the dialog box will sit.
     */
    private static double FINAL_POSITION_Y = 25;

    /**
     * The maximum amount of items to show.
     */
    private static int TOTAL_AMOUNT_OF_ITEMS_TO_SHOW = 50;

    /**
     * The main background of the dialog box.
     */
    private HBox mainBackground;

    /**
     * The button that will hide the select item popup without doing anything.
     */
    private CircularButton byeButton;

    /**
     * The button that will choose the currently selected item.
     */
    private CircularButton submitButton;

    /**
     * Stores whether the submit button is activated or not.
     */
    private boolean canBeSubmitted = false;

    /**
     * The item searchitem result area.
     */
    private FlowPane itemGrid;

    /**
     * The thread that searching for items will execute on.
     */
    private Thread searchThread;

    /**
     * The ItemSearchResult that has currently been selected.
     */
    private ItemSearchResult currentItemSearchResultSelected;

    /**
     * The listener to the OnSelectItemConfirmed event.
     */
    private OnSelectItemConfirmed onSelectItemConfirmed;

    /**
     * The listener to the OnSelectItemCancelled event.
     */
    private OnSelectItemCancelled onSelectItemCancelled;

    /**
     * Constructor
     */
    private SelectItemPopup() {
        super("/fxml/components/SelectItemPopup.fxml");
        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setTopAnchor(this, 0d);
        toFront();

        mainBackground = (HBox) lookup("#mainBackground");
        Objects.requireNonNull(mainBackground);

        HBox buttonRow = (HBox) lookup("#buttonRow");
        Objects.requireNonNull(buttonRow);

        byeButton = CircularButton.failedButton();
        buttonRow.getChildren().add(byeButton);
        Runnable onBye = () -> {
            hide();
            if (onSelectItemCancelled != null) {
                onSelectItemCancelled.onCancelled();
            }
        };

        byeButton.setOnClicked(onBye);


        submitButton = CircularButton.successButton();
        buttonRow.getChildren().add(submitButton);
        correctSubmitButton();
        submitButton.setOnClicked(() -> {
            if (canBeSubmitted) {
                hide();
                if (onSelectItemConfirmed != null) {
                    onSelectItemConfirmed.onSuccess(currentItemSearchResultSelected.getItem());
                }
            }
        });

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for a Item");
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

        HBox searchBoxContainer = (HBox) lookup("#searchBoxContainer");
        Objects.requireNonNull(searchBoxContainer);
        searchBoxContainer.getChildren().add(searchBar);
        searchBoxContainer.getChildren().add(searchButton);

        ScrollPane scrollPane = (ScrollPane) lookup("#scrollPane");
        Objects.requireNonNull(scrollPane);
        itemGrid = (FlowPane) scrollPane.getContent();

        showCeneterLabel("Search for a item");
    }

    /**
     * Creates and starts a select item popup
     * @return The select item popup.
     */
    public static SelectItemPopup show() {
        SelectItemPopup sip = new SelectItemPopup();
        OSRSUtilities.getWindow().getMainLayout().getChildren().add(sip);
        OSRSUtilities.getWindow().setSideBarEnabledStatus(false);
        sip.startHelloAnimation();
        return sip;
    }

    /**
     * Starts the hello animation.
     */
    private void startHelloAnimation() {
        setVisible(true);
        mainBackground.setLayoutY(OSRSUtilities.getWindow().getPrimaryStage().getHeight() + 5);

        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(
                new KeyFrame(HELLO_BYE_ANIMATION_TIME, new KeyValue(mainBackground.layoutYProperty(), FINAL_POSITION_Y)
                ));
        timeline.play();
        timeline.setOnFinished(e ->  {
            timeline.stop();
            this.endHelloAnimation();
        });
    }

    /**
     * Called when the hello animation ends.
     */
    private void endHelloAnimation() {
    }

    /**
     * Starts the bye animation.
     */
    public void hide() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(
                new KeyFrame(HELLO_BYE_ANIMATION_TIME, new KeyValue(mainBackground.layoutYProperty(), OSRSUtilities.getWindow().getPrimaryStage().getHeight() + 5)
                ));
        timeline.play();
        timeline.setOnFinished(e ->  {
            timeline.stop();
            this.endByeAnimation();
        });
    }

    /**
     * Called when the bye animation ends.
     */
    private void endByeAnimation() {
        OSRSUtilities.getWindow().getMainLayout().getChildren().remove(this);
        OSRSUtilities.getWindow().setSideBarEnabledStatus(true);
    }

    /**
     * Corrects the submit button colors depending on the canBeSubmitted boolean.
     */
    private void correctSubmitButton() {
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
     * Shows a center label.
     * @param message the message to show
     */
    private void showCeneterLabel(String message) {
        itemGrid.getChildren().clear();
        itemGrid.setAlignment(Pos.CENTER);
        resetSelectedItemSearchResult();

        Label label = new Label();
        label.setText(message);
        label.setAlignment(Pos.CENTER);
        label.setFont(new Font("Open Sans", 13));
        label.setWrapText(true);
        label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        itemGrid.getChildren().add(label);
    }

    /**
     * Shows a loading spinner.
     */
    private void showLoadingSpinner() {
        itemGrid.getChildren().clear();
        itemGrid.setAlignment(Pos.CENTER);
        resetSelectedItemSearchResult();

        JFXSpinner spinner = new JFXSpinner();
        itemGrid.getChildren().add(spinner);
    }

    /**
     * Called when the text changes on the searchitem box.
     * @param phrase The phrase to searchitem for.
     */
    private void search(String phrase) {
        if (searchThread != null) {
            searchThread.interrupt();
            searchThread = null;
        }

        if (phrase.isEmpty()) {
            showCeneterLabel("Search for a item");
            return;
        } else {
            showLoadingSpinner();
        }

        searchThread = new Thread(() -> {
            List<Item> items = Item.loadAllItemsThatContainInName(phrase);
            if (items == null) {
                Platform.runLater(() -> showCeneterLabel("A error occurred."));
                return;
            }

            int currentAmountOfItems = 0;
            List<Node> searchResults = new ArrayList<>();
            for (Item item : items) {
                ItemSearchResult itemSearchResult = new ItemSearchResult(item);
                itemSearchResult.setOnItemSearchResultClicked(this::onItemSearchResultClicked);
                searchResults.add(itemSearchResult);

                currentAmountOfItems++;
                if (currentAmountOfItems > TOTAL_AMOUNT_OF_ITEMS_TO_SHOW) {
                    Label label = new Label();
                    label.setText("Showing maximum amount of items - please research with a more specific term.");
                    label.setAlignment(Pos.CENTER);
                    label.setFont(new Font("Open Sans", 13));
                    label.setWrapText(true);
                    label.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
                    searchResults.add(label);
                    break;
                }
            }

            Platform.runLater(() -> {
                itemGrid.getChildren().clear();
                itemGrid.setAlignment(Pos.CENTER);
                itemGrid.getChildren().addAll(searchResults);
            });
        });
        searchThread.start();
    }

    /**
     * Resets the currently selected item searchitem result.
     */
    private void resetSelectedItemSearchResult() {
        if (currentItemSearchResultSelected != null) {
            currentItemSearchResultSelected.setNormalBorder();
        }

        currentItemSearchResultSelected = null;
        canBeSubmitted = false;
        correctSubmitButton();
    }

    /**
     * Called when a item searchitem result when it was clicked.
     * @param self The item searchitem result that was clicked.
     * @param item The item the item searchitem was representing.
     */
    private void onItemSearchResultClicked(ItemSearchResult self, Item item) {
        if (currentItemSearchResultSelected == self) {
            resetSelectedItemSearchResult();
            return;
        }

        if (currentItemSearchResultSelected != null) {
            currentItemSearchResultSelected.setNormalBorder();
        }
        self.setSelectedBorder();
        currentItemSearchResultSelected = self;
        canBeSubmitted = true;
        correctSubmitButton();
    }

    /**
     * Sets the listener to the OnSelectItemConfirmed event.
     * @param onSelectItemConfirmed the listener.
     */
    public void setOnSelectItemConfirmed(OnSelectItemConfirmed onSelectItemConfirmed) {
        this.onSelectItemConfirmed = onSelectItemConfirmed;
    }

    /**
     * Sets the listener to the OnSelectItemCancelled event.
     * @param onSelectItemCancelled the listener.
     */
    public void setOnSelectItemCancelled(OnSelectItemCancelled onSelectItemCancelled) {
        this.onSelectItemCancelled = onSelectItemCancelled;
    }
}
