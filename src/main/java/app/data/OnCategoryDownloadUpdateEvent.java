package app.data;

import app.data.categories.DownloadCategories;
import app.data.categories.DownloadCategoriesStatus;

/**
 * Category download event which will allow objects to listen to when a category has hit a milestone.
 * Such as receiving the api request or inserting a object into the database or when its status gets updated.
 * @author Brett Taylor
 */
public interface OnCategoryDownloadUpdateEvent {
    /**
     * Called when the status of a category has changed.
     * @param downloadCategory The category in question.
     * @param oldStatus The old download status.
     * @param newStatus The new download status.
     */
    void statusUpdate(DownloadCategories downloadCategory, DownloadCategoriesStatus oldStatus, DownloadCategoriesStatus newStatus);

    /**
     * Called when the category stars downloading.
     * @param downloadCategory The category in question.
     */
    void requestToStartDownload(DownloadCategories downloadCategory);

    /**
     * Called when the category has received the wiki api.
     * @param downloadCategory The category in question.
     */
    void receivedWikiAPI(DownloadCategories downloadCategory);

    /**
     * Called when the category is inserting objects into the database.
     * @param downloadCategory The category in question.
     * @param numberInstalled The number it is through.
     * @param amountToBeInstalled The total amount to be installed.
     */
    void installObject(DownloadCategories downloadCategory, int numberInstalled, int amountToBeInstalled);

    /**
     * Called when the category is done downloading.
     * @param downloadCategory The category in question.
     */
    void doneDownload(DownloadCategories downloadCategory);
}
