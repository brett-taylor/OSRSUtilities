package app.data.wiki;

import app.data.DataManager;
import app.data.DatabaseSQLExecuteResult;
import app.data.SQLStatement;
import app.data.tables.BlacklistTable;
import app.ui.components.DialogBox;

import java.sql.SQLException;

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
}
