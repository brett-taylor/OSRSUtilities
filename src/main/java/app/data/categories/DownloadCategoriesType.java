package app.data.categories;

import app.data.runescape.Item;
import app.data.runescape.Monster;

/**
 * Stores what type of category it is.
 * @author Brett Taylor
 */
public enum DownloadCategoriesType {
    ITEM(Item::create),
    MONSTER(Monster::create);

    /**
     * The method that will saveObject the function.
     */
    private DownloadCategoriesSave saveObjectFunction;

    /**
     * Constructor.
     */
    DownloadCategoriesType(DownloadCategoriesSave saveObjectFunction) {
        this.saveObjectFunction = saveObjectFunction;
    }

    /**
     * Saves the object
     * @param wikiArticleName The wiki article name
     * @param wikiArticleURL The wiki article name
     * @param wikiArticleID The wiki article name
     */
    public void save(String wikiArticleName, String wikiArticleURL, Integer wikiArticleID) {
        if (saveObjectFunction != null) {
            saveObjectFunction.saveObject(wikiArticleName, wikiArticleURL, wikiArticleID);
        }
    }
}