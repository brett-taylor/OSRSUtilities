package app.ui.components.popups;

import app.ui.components.buttons.CircularButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The dialog box that can appear on screen.
 * @author Brett Taylor
 */
public class DialogBox extends PopupMenu {
    /**
     * Stores the dialog box that is currently showing on screen. Only one dialog box at a time can show.
     */
    private static DialogBox dialogBox = null;

    /**
     * Creates a dialogbox.
     */
    private DialogBox() {
    }

    /**
     * Shows the dialog box.
     * @param title what the heading of the dialog box will show.
     */
    public static void showDialogBox(String title) {
        if (dialogBox != null) {
            return;
        }

        dialogBox = new DialogBox();
        Platform.runLater(() -> {
            dialogBox.setHeading(title);
        });

        Platform.runLater(() -> dialogBox.startHelloAnimation());
    }

    /**
     * Creates a dialog box set up to handle errors.
     * @param body The error message.
     */
    public static void showError(String body) {
        showError(body, false);
    }

    /**
     * Creates a dialog box set up to handle errors.
     * @param body The error message.
     * @param seriousError If true the program will close when the dialog box is dismissed. If false it will continue.
     */
    public static void showError(String body, boolean seriousError) {
        if (dialogBox != null) {
            return;
        }

        DialogBox.showDialogBox("An error occured.");
        DialogBox.showBodyLabel(body);
        CircularButton button = CircularButton.regularButton();
        button.setOnClicked(() -> {
            if (seriousError)
                System.exit(0);
            else
                DialogBox.close();
        });
        DialogBox.addToButtonRow(button);
    }

    /**
     * Closes a dialog box.
     */
    public static void close() {
        if (dialogBox != null) {
            dialogBox.startByeAnimation();
            dialogBox.updateTimer.stop();
        }
    }

    /**
     * Adds a node to the buttom row of the dialog box (the button row).
     * @param node The node to be added.
     */
    public static void addToButtonRow(Node node) {
        Platform.runLater(() -> {
            if (dialogBox != null) {
                dialogBox.buttonRow.getChildren().add(node);
            }
        });
    }

    /**
     * Creates the body to be just a label.
     * @param text The contents.
     */
    public static void showBodyLabel(String text) {
        if (dialogBox == null)
            return;

        Label label = new Label();
        label.setFont(new Font("Open Sans Light", 14));
        label.setTextFill(Color.WHITE);
        label.setText(text);
        label.setAlignment(Pos.TOP_LEFT);
        label.setWrapText(true);
        dialogBox.mainBody.getChildren().add(label);
        AnchorPane.setTopAnchor(label, 10d);
        AnchorPane.setLeftAnchor(label, 10d);
        AnchorPane.setBottomAnchor(label, 10d);
        AnchorPane.setRightAnchor(label, 10d);
    }

    /**
     * Creates the body to be a spinner.
     */
    public static void showingLoadingSpinner() {
        if (dialogBox == null)
            return;

        double radius = 30d;
        JFXSpinner spinner = new JFXSpinner();
        spinner.setRadius(radius);
        spinner.setPrefSize(radius, radius);
        spinner.setMinSize(radius, radius);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(spinner);

        dialogBox.mainBody.getChildren().add(borderPane);
        AnchorPane.setTopAnchor(borderPane, 10d);
        AnchorPane.setLeftAnchor(borderPane, 10d);
        AnchorPane.setBottomAnchor(borderPane, 10d);
        AnchorPane.setRightAnchor(borderPane, 10d);
    }

    /**
     * Shows a menu dialog box.
     * @param nodes The nodes that will be added to the menus.
     */
    public static void showMenu(Node... nodes) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5d);
        vbox.setFillWidth(true);
        dialogBox.mainBody.getChildren().add(vbox);
        AnchorPane.setTopAnchor(vbox, 5d);
        AnchorPane.setLeftAnchor(vbox, 5d);
        AnchorPane.setBottomAnchor(vbox, 5d);
        AnchorPane.setRightAnchor(vbox, 5d);
        vbox.getChildren().addAll(nodes);
    }

    /**
     * Places a node in the top right of the dialog box.
     * @param node The node to place.
     */
    public static void placeInTopRight(Node node) {
        dialogBox.headingBody.getChildren().add(node);
        AnchorPane.setTopAnchor(node, 5d);
        AnchorPane.setRightAnchor(node, 5d);
    }

    /**
     * Clears the body of the dialog box.
     */
    public static void clearBody() {
        dialogBox.mainBody.getChildren().clear();
    }


    /**
     * Called when the bye animation ends.
     */
    @Override
    protected void endByeAnimation() {
        super.endByeAnimation();
        dialogBox = null;
    }

    /**
     * Sets the contents of the heading label.
     * @param text The contents.
     */
    private void setHeading(String text) {
        heading.setText(text);
    }

    /**
     * Sets a bit of code to be executed when the shortcut enter is pressed.
     * @param runnable The code to be executed.
     */
    public static void setShortcutSuccess(Runnable runnable) {
        dialogBox.setOnShortcutSuccess(runnable);
    }

    /**
     * Sets a bit of code to be executed when the shortcut escape is pressed.
     * @param runnable The code to be executed.
     */
    public static void setShortcutFailed(Runnable runnable) {
        dialogBox.setOnShortcutFailed(runnable);
    }

    /**
     * Sets a bit of code to be executed when the popup menu has shown itself.
     * @param runnable The code to be executed.
     */
    public static void setPopupShowed(Runnable runnable) {
        dialogBox.setOnPopupShowed(runnable);
    }
}
