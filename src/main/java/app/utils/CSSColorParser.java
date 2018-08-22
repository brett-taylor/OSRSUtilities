package app.utils;

import app.ui.OSRSUtilitiesWindow;
import app.ui.components.popups.DialogBox;
import javafx.css.CssParser;
import javafx.css.Declaration;
import javafx.css.Rule;
import javafx.css.Stylesheet;
import javafx.css.converter.ColorConverter;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Parses a colour found in css into a javafx object
 * https://stackoverflow.com/questions/42609756/how-to-parse-map-javafx-css-file-to-retrieve-its-properties-and-values
 * @author Brett Taylor
 */
public class CSSColorParser {
    /**
     * The root string of the css.
     */
    private static Rule root;

    /**
     * Parses a colour found in css into a javafx object
     * @param name The name of the colour in css
     * @return The colour if successful if not it will return white.
     */
    public static Color parseColor(String name) {
        if (root == null) {
            try {
                Stylesheet css = new CssParser().parse(CSSColorParser.class.getResource(OSRSUtilitiesWindow.CSS_LOCATION).toURI().toURL());
                root = css.getRules().get(0);
            } catch (IOException | URISyntaxException  e) {
                DialogBox.showError("CSS Parse Failed: \n" + e.getMessage());
            }
        }

        if (root != null) {
            for (Declaration d : root.getDeclarations()) {
                if (d.getProperty().equals(name)) {
                    return ColorConverter.getInstance().convert(d.getParsedValue(), null);
                }
            }
        }

        return Color.WHITE;
    }
}
