package app.ui.components.items;

import app.utils.CSSColorParser;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Recreates the stack size label from runescape.
 * When the given quantity is at (or above) 100,000 it will instead show 100k and be white.
 * When the given quantity is at (or above) 10,000,000 it will instead show 10M and be green.
 * @author Brett Taylor
 */
public class StackSizeLabel extends Label {
    /**
     * The color the label will be when the stack size is less than 100k.
     */
    private static final Color BELOW_100K_TEXT_COLOR = CSSColorParser.parseColor("-stack-below-100k-text-color");

    /**
     * The color the label will be when the stack size is above 100k.
     */
    private static final Color ABOVE_100K_TEXT_COLOR = CSSColorParser.parseColor("-stack-above-100k-text-color");

    /**
     * The color the label will be when the stack size is above 10m.
     */
    private static final Color ABOVE_10M_TEXT_COLOR = CSSColorParser.parseColor("-stack-above-10m-text-color");

    /**
     * The value that the text will end with a k and become white.
     */
    private static final int VALUE_FOR_100k = 100000;

    /**
     * The value that the text will end with a m and become green.
     */
    private static final int VALUE_FOR_10M = 10000000;

    /**
     * The stack size.
     */
    private int stackSize;

    /**
     * Creates a stack size label.
     */
    public StackSizeLabel() {
        setFont(new Font("Open Sans Bold", 9));
    }

    /**
     * Sets the stack size.
     * @param stackSize the new stack size.
     */
    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
        setCorrectColor();
        setCorrectText();
        setVisible(stackSize >= 2); // Set to show if the stack size is 2 or greater.
    }

    /**
     * Sets the correct color of the label.
     */
    private void setCorrectColor() {
        if (stackSize >= VALUE_FOR_10M) {
            setTextFill(ABOVE_10M_TEXT_COLOR);
        } else if (stackSize >= VALUE_FOR_100k) {
            setTextFill(ABOVE_100K_TEXT_COLOR);
        } else {
            setTextFill(BELOW_100K_TEXT_COLOR);
        }
    }

    /**
     * Sets the correct text string associated with the stack size..
     */
    private void setCorrectText() {
        if (stackSize >= VALUE_FOR_10M) {
            int importantPart = stackSize / 1000000;
            setText(importantPart + "M");
        } else if (stackSize >= VALUE_FOR_100k) {
            int importantPart = stackSize / 1000;
            setText(importantPart + "K");
        } else {
            setText(String.valueOf(stackSize));
        }
    }
}
