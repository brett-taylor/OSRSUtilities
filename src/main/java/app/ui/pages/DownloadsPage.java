package app.ui.pages;

import app.data.categories.DownloadCategoriesStatus;
import app.ui.components.DialogBox;
import app.ui.components.buttons.CircularButton;
import app.ui.components.CategoryDownloadButton;
import app.ui.components.buttons.SquareButton;
import app.utils.CSSColorParser;
import app.data.categories.DownloadCategories;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

/**
 * Test page - no function.
 * @author Brett Taylor
 */
public class DownloadsPage extends BasePage {
    /**
     * The grid view.
     */
    private FlowPane gridView;

    /**
     * Constructor
     */
    public DownloadsPage() {
        super("/fxml/pages/DatabaseSettings.fxml", true);

        CircularButton downloadAll = new CircularButton();
        downloadAll.setBackgroundColor(CSSColorParser.parseColor("-background-dark-color"));
        downloadAll.setBackgroundHoverColor(CSSColorParser.parseColor("-background-color"));
        downloadAll.setGlyph(FontAwesomeIcon.ELLIPSIS_V, Color.WHITE);
        downloadAll.setSize(25d);
        downloadAll.setGlyphSize("27");
        downloadAll.setOnClicked(this::showMenu);

        Pane downloadButtonHolder = (Pane) lookup("#downloadButtonHolder");
        Objects.requireNonNull(downloadButtonHolder);
        downloadButtonHolder.getChildren().add(downloadAll);

        ScrollPane scrollPane = (ScrollPane) lookup("#scrollPane");
        Objects.requireNonNull(scrollPane);

        gridView = (FlowPane) scrollPane.getContent();
        Objects.requireNonNull(gridView);

        new Thread(() -> {
            for (DownloadCategories category : DownloadCategories.values()) {
                CategoryDownloadButton button = new CategoryDownloadButton(category);
                Platform.runLater(() -> gridView.getChildren().add(button));
            }

            Platform.runLater(() -> gridView.requestLayout());
        }).start();
    }

    @Override
    public void onLoaded() {
    }

    @Override
    public void onRemoved() {
        for (DownloadCategories category : DownloadCategories.values()) {
            category.clearAllListenersToOnCategoryDownloadUpdateEvent();
        }
    }

    /**
     * Show the menu
     */
    private void showMenu() {
        DialogBox.showDialogBox("Pick the action you wish to complete.");

        CircularButton close = CircularButton.regularButton();
        close.setOnClicked(DialogBox::close);
        close.setGlyph(FontAwesomeIcon.TIMES, null);

        Label helpText = new Label("By downloading all of the categories you will have access to all of the information. " +
                "Alternatively, you can resync the current categories you have downloaded this will update these categories to match the latest Runescape information."
        );
        helpText.setFont(new Font("Open Sans", 12));
        helpText.setTextFill(CSSColorParser.parseColor("-text-muted-color"));
        helpText.setAlignment(Pos.CENTER_LEFT);
        helpText.setWrapText(true);

        Pane spacer = new Pane();
        spacer.setPrefHeight(10d);

        SquareButton downloadAll = SquareButton.defaultButton();
        downloadAll.setSize(0, 30d);
        downloadAll.setTextAllignment(Pos.CENTER);
        downloadAll.setBackgroundColor(CSSColorParser.parseColor("-background-dark-color"));
        downloadAll.setText("Download All Categories.");
        downloadAll.setOnClicked(() -> {
            DialogBox.close();
            downloadAll();
        });

        SquareButton refreshAll = SquareButton.defaultButton();
        refreshAll.setSize(0, 30d);
        refreshAll.setTextAllignment(Pos.CENTER);
        refreshAll.setBackgroundColor(CSSColorParser.parseColor("-background-dark-color"));
        refreshAll.setText("Refresh All Downloaded Categories.");
        refreshAll.setOnClicked(() -> {
            DialogBox.close();
            refershAll();
        });

        DialogBox.placeInTopRight(close);
        DialogBox.showMenu(downloadAll, refreshAll, spacer, helpText);
    }

    /**
     * Starts downloading all of the categories.
     */
    private void downloadAll() {
        new Thread(() -> {
            for (DownloadCategories category : DownloadCategories.values()) {
                if (category.getDownloadStatus() == DownloadCategoriesStatus.NOT_DOWNLOADED) {
                    category.setDownloadStatus(DownloadCategoriesStatus.QUEUED);
                }
            }

            for (DownloadCategories category : DownloadCategories.values()) {
                if (category.getDownloadStatus() == DownloadCategoriesStatus.QUEUED) {
                    category.download();
                }
            }
        }).start();
    }

    /**
     * Refershes all the categories that are downloade.
     */
    private void refershAll() {
        new Thread(() -> {
            for (DownloadCategories category : DownloadCategories.values()) {
                if (category.getDownloadStatus() == DownloadCategoriesStatus.DOWNLOADED) {
                    category.setDownloadStatus(DownloadCategoriesStatus.QUEUED);
                }
            }

            for (DownloadCategories category : DownloadCategories.values()) {
                if (category.getDownloadStatus() == DownloadCategoriesStatus.QUEUED) {
                    category.download();
                }
            }
        }).start();
    }
}
