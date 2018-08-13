package app.data.models;

import app.data.DataManager;
import app.data.DatabaseSQLErrorType;
import app.data.DatabaseSQLExecuteResult;
import app.data.SQLStatement;
import app.data.tables.MonsterTable;
import app.ui.components.DialogBox;

/**
 * Represents an Monster from runescape.
 * @author Brett Taylor
 */
public class Monster {
    /**
     * Name of the monster.
     */
    private String name;

    /**
     * The name on the wiki used in the url of the article for this monster.
     */
    private String wikiURL;

    /**
     * The wiki id of the monster.
     */
    private Integer wikiID;

    /**
     * Creates a monster.
     * @param name Name of the monster.
     * @param wikiURL Wiki name of the monster.
     * @param wikiID The app.data.wiki id of the monster.
     */
    private Monster(String name, String wikiURL, Integer wikiID) {
        this.name = name;
        this.wikiURL = wikiURL;
        this.wikiID  = wikiID;
    }

    /**
     * @return The monster name
     */
    public String getName() {
        return name;
    }

    /**
     * @return The monster's wiki article id.
     */
    public int getArticleID() {
        return wikiID;
    }

    /**
     * @return The monster's wiki article url.
     */
    public String getWikiURLEnding() {
        return wikiURL;
    }

    /**
     * Creates an monsters with the information given and saves it to the data.
     * @param name Name of the monsters.
     * @param wikiName Wiki name of the monsters.
     * @param wikiID The app.data.wiki id of the monsters.
     */
    public static void create(String name, String wikiName, Integer wikiID) {
        if (wikiName.startsWith("/app/data/wiki/"))
            wikiName = wikiName.substring(6);

        SQLStatement sqlStatement = new SQLStatement("INSERT INTO tableName VALUES(monstersName, monstersWikiURL, monstersWikiID);");
        sqlStatement.bindParam("tableName", MonsterTable.TABLE_NAME);
        sqlStatement.bindStringParam("monstersName", name);
        sqlStatement.bindStringParam("monstersWikiURL", wikiName);
        sqlStatement.bindParam("monstersWikiID", "" + wikiID);

        DatabaseSQLExecuteResult execute = DataManager.execute(sqlStatement);
        if (!execute.isSuccessful()) {
            if (execute.getErrorType() != DatabaseSQLErrorType.UNIQUE_FAILED) {
                DialogBox.showError("Failed to create Monster: " + name + ". \n" + execute.getErrorMessage());
            }
        }
    }

    /**
     * Deletes all monsters that have the matching url.
     * @param url The url to match.
     */
    public static void deleteByMatchingWikiURL(String url) {
        SQLStatement sqlStatement = new SQLStatement("DELETE FROM tableName WHERE urlColumn LIKE urlGiven");
        sqlStatement.bindParam("tableName", MonsterTable.TABLE_NAME);
        sqlStatement.bindParam("urlColumn", MonsterTable.WIKI_URL_COLUMN);
        sqlStatement.bindStringParam("urlGiven", "%" + url + "%");

        DatabaseSQLExecuteResult execute = DataManager.execute(sqlStatement);
        if (!execute.isSuccessful()) {
            DialogBox.showError("Failed to delete Monsters with following url: " + url + ". \n" + execute.getErrorMessage());
        }
    }
}
