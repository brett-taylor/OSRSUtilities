package app.data.events;

import app.data.DownloadCategories;
import app.data.DownloadCategoriesStatus;

/**
 * Category download event which will allow objects to listen to when a category has hit a milestone.
 * Such as receiving the api request or inserting a object into the database or when its status gets updated.
 * @author Brett Taylor
 */
public interface CategoryDownloadEvent {
    /**
     * Called when the status of a category has changed.
     * @param downloadCategory The category in question.
     * @param oldStatus The old download status.
     * @param newStatus The new download status.
     */
    void statusUpdate(DownloadCategories downloadCategory, DownloadCategoriesStatus oldStatus, DownloadCategoriesStatus newStatus);
}
