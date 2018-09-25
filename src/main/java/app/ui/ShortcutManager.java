package app.ui;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Handles shortcuts. Allows objects to listen and stop listening to certain keys.
 * Key events are never consumed and are always passed on by this.
 *
 * Currently will execute shortcuts when typing in a text field. As this is only being used for enter and escape
 * currently there is no need to change this. But it should be fixed in a future release.
 *
 * @author Brett Taylor
 */
public class ShortcutManager {
    /**
     * Shortcuts stored here.
     */
    private static HashMap<KeyCode, List<Runnable>> shortcuts = new HashMap<>();

    /**
     * Registers a shortcut to a certain key.
     * @param keycode The key that will trigger the code.
     * @param runnable The code that will be executed when the key is pressed.
     */
    public static void addShortcut(KeyCode keycode, Runnable runnable) {
        if (!shortcuts.containsKey(keycode)) {
            shortcuts.put(keycode, new ArrayList<>());
        }

        shortcuts.get(keycode).add(runnable);
    }

    /**
     * Removes a shortcut.
     * @param keycode The shortcut to be removed.
     */
    public static void clearShortcut(KeyCode keycode) {
        shortcuts.remove(keycode);
    }

    /**
     * Activates a certain keycode.
     * @param keyCode
     */
    public static void activateShortcut(KeyCode keyCode) {
        if (shortcuts.containsKey(keyCode)) {
            List<Runnable> list = shortcuts.get(keyCode);
            for (Runnable run : list) {
                run.run();
            }
        }
    }
}
