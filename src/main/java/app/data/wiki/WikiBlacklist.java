package app.data.wiki;

import app.data.DataManager;
import app.data.DatabaseSQLErrorType;
import app.data.DatabaseSQLExecuteResult;
import app.data.SQLStatement;
import app.data.models.Item;
import app.data.models.Monster;
import app.data.tables.BlacklistTable;
import app.ui.components.DialogBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The blacklist for urls from the wiki.
 * Urls are saved in the database.
 * @author Brett Taylor
 */
public class WikiBlacklist {
    /**
     * Checks if the given url exists in the blacklisted table in the database.
     * @param url The url to check if it is blacklisted.
     * @return True if the url exists in the blacklist table.
     */
    public static boolean isURLBlacklisted(String url) {
        if (url.contains(":")) {
            url = url.split(":")[0];
        }

        SQLStatement sqlStatement = new SQLStatement("SELECT urlColumn FROM tableName WHERE urlColumn LIKE urlBlacklisted");
        sqlStatement.bindParam("tableName", BlacklistTable.TABLE_NAME);
        sqlStatement.bindParam("urlColumn", BlacklistTable.URL);
        sqlStatement.bindStringParam("urlBlacklisted", "%" + url + "%");

        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        if (!result.isSuccessful()) {
            DialogBox.showError("Failed to check a url blacklist: " + url + " \n" + result.getErrorMessage());
            return false;
        }

        try {
            return result.getResultSet().next();
        } catch (SQLException e) {
            DialogBox.showError("Failed to check a url blacklist: " + url + " \n" + result.getErrorMessage());
            return false;
        }
    }

    /**
     * Returns all of the urls that are in the blacklisted table in the database that match the given phrase.
     * @param phrase The phrase that should be in the url.
     * @return A list of the blacklisted urls that matched the criteria.
     */
    public static List<String> getAllURLSBlacklisted(String phrase) {
        SQLStatement sqlStatement = new SQLStatement("SELECT urlColumn FROM tableName WHERE urlColumn LIKE phrase;");
        sqlStatement.bindParam("tableName", BlacklistTable.TABLE_NAME);
        sqlStatement.bindParam("urlColumn", BlacklistTable.URL);
        sqlStatement.bindStringParam("phrase", "%" + phrase + "%");

        List<String> list = new ArrayList<>();
        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        if (!result.isSuccessful()) {
            DialogBox.showError("Failed to grab blacklisted urls.\n" + result.getErrorMessage());
            return list;
        }

        try {
            while (result.getResultSet().next()) {
                list.add(result.getResultSet().getString(BlacklistTable.URL));
            }
        } catch (SQLException e) {
            DialogBox.showError("Failed to grab blacklisted urls.\n" + result.getErrorMessage());
        }

        return list;
    }

    /**
     * Deletes the given blacklisted url.
     * @param url The blacklisted url to delete.
     */
    public static void delete(String url) {
        SQLStatement sqlStatement = new SQLStatement("DELETE FROM tableName WHERE urlColumn = blacklistedURL;");
        sqlStatement.bindParam("tableName", BlacklistTable.TABLE_NAME);
        sqlStatement.bindParam("urlColumn", BlacklistTable.URL);
        sqlStatement.bindStringParam("blacklistedURL", url);

        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        if (!result.isSuccessful()) {
            DialogBox.showError("Failed to delete blacklisted url: " + url +".\n" + result.getErrorMessage());
        }
    }

    /**
     * Adds the given blacklisted url.
     * @param url The blacklisted url to add.
     */
    public static void add(String url) {
        SQLStatement sqlStatement = new SQLStatement("INSERT INTO tableName VALUES(blacklistedURL);");
        sqlStatement.bindParam("tableName", BlacklistTable.TABLE_NAME);
        sqlStatement.bindStringParam("blacklistedURL", url);

        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        if (!result.isSuccessful()) {
            if (result.getErrorType() != DatabaseSQLErrorType.UNIQUE_FAILED) {
                DialogBox.showError("Failed to add blacklisted url: " + url +".\n" + result.getErrorMessage());
            }
            return;
        }

        Monster.deleteByMatchingWikiURL(url);
        Item.deleteByMatchingWikiURL(url);
    }
}
