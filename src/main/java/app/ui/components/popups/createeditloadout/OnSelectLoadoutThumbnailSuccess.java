package app.ui.components.popups.createeditloadout;

import app.data.loadouts.LoadoutThumbnailType;

/**
 * Called when a select loadout thumbnail popup
 * @author Brett Taylor
 */
@FunctionalInterface
public interface OnSelectLoadoutThumbnailSuccess {
    /**
     * @param type The type of thumbnail.
     * @param name The name of the thumbnail.
     */
    void onSuccess(LoadoutThumbnailType type, String name);
}
