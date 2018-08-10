package app.data.categories;

/**
 * The download saveObject function that passes the wiki article name, url and id.
 * Used when grabbing data objects (e.g. Item or Monster).
 * Data objects are assigned a DownloadCategoriesType which will have a DownloadCategoriesSave that
 * DownloadCategoriesType overrides to save that data object correctly.
 * @author Brett Taylor
 */
@FunctionalInterface
public interface DownloadCategoriesSave {
    /**
     * @param wikiArticleName The wiki article name
     * @param wikiArticleURL The wiki article name
     * @param wikiArticleID The wiki article name
     */
    void saveObject(String wikiArticleName, String wikiArticleURL, Integer wikiArticleID);
}