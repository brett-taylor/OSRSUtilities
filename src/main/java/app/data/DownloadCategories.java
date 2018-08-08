package app.utils;

import app.data.DataManager;
import app.data.DatabaseSQLExecuteResult;
import app.data.SQLStatement;
import app.data.tables.CategoryTable;
import app.ui.components.DialogBox;

import java.sql.SQLException;

/**
 * All the different categories on the app.wiki the api allows us to access.
 * @author Brett Taylor
 */
public enum DownloadCategories {
    HELMETS("Head_slot_items", "Helmets", "Dragon_full_helm_detail"),
    AMULETS("Amulet", "Amulets", "Amulet_of_fury_detail"),
    BRACLETS("Bracelet", "Braclets", "Zenyte_bracelet_detail");

    /**
     * The category the enum represents on the app.wiki.
     */
    private String wikiCategoryName;

    /**
     * The name of the category.
     */
    private String niceName;

    /**
     * The image name of the category in the default images folder.
     */
    private String imageName;

    /**
     * The download status.
     */
    private DownloadCategoriesStatus downloadStatus;
    
    /**
     * Constructor.
     * @param wikiCategoryName The category the enum represents on the app.wiki.
     * @param niceName The name of the category.
     * @param imageName The image name of the category in the default images folder.
     */
    DownloadCategories(String wikiCategoryName, String niceName, String imageName) {
        this.wikiCategoryName = wikiCategoryName;
        this.niceName = niceName;
        this.imageName = imageName;
    }

    /**
     * @return The category the enum represents on the app.wiki.
     */
    public String getWikiCategoryName() {
        return wikiCategoryName;
    }

    /**
     * @return The nice name
     */
    public String getNiceName() {
        return niceName;
    }

    /**
     * @return The image name
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @return the current download state.
     */
    public DownloadCategoriesStatus getDownloadStatus() {
        if (downloadStatus == null) {
            downloadStatus = getDownloadStatusFromDatabase();

        }

        return downloadStatus;
    }

    /**
     * Sets the download status
     * @param newDownloadStatus The new status.
     */
    public void setDownloadStatus(DownloadCategoriesStatus newDownloadStatus) {
        this.downloadStatus = newDownloadStatus;
    }

    /**
     * @return the current download state.
     */
    private DownloadCategoriesStatus getDownloadStatusFromDatabase() {
        SQLStatement sqlStatement = new SQLStatement("SELECT downloadColumn FROM tableName WHERE nameColumn = wikiCategory;");
        sqlStatement.bindParam("tableName", CategoryTable.TABLE_NAME);
        sqlStatement.bindParam("downloadColumn", CategoryTable.DOWNLOADED_COLUMN);
        sqlStatement.bindParam("nameColumn", CategoryTable.NAME_COLUMN_PK);
        sqlStatement.bindStringParam("wikiCategory", this.toString());

        DownloadCategoriesStatus status = DownloadCategoriesStatus.NOT_DOWNLOADED;
        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        try {
            if (result.getResultSet().next()) {
                if (result.getResultSet().getInt(CategoryTable.DOWNLOADED_COLUMN) == 1)
                    status = DownloadCategoriesStatus.DOWNLOADED;
            } else {
                createCategoryRowInDatabase();
            }
        } catch (SQLException e) {
            DialogBox.showError("Failed to get download status for " + getNiceName() + ". \n" + e.getMessage());
        }

        return status;
    }

    /**
     * Creates the category row in the database.
     */
    private void createCategoryRowInDatabase() {
        SQLStatement sqlStatement = new SQLStatement("INSERT INTO tableName VALUES(name, downloadStatus);");
        sqlStatement.bindParam("tableName", CategoryTable.TABLE_NAME);
        sqlStatement.bindParam("downloadStatus", "0");
        sqlStatement.bindStringParam("name", this.toString());

        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        if (!result.isSuccessful()) {
            DialogBox.showError("Failed to get create download status for " + getNiceName() + ". \n" + result.getErrorMessage());
        }
    }

    /**
     * Saves the new download status
     */
    public void saveDownloadStatus() {
        if (downloadStatus == null) {
            return;
        }

        int newDownloadStatus = 0;
        if (downloadStatus == DownloadCategoriesStatus.DOWNLOADED)
            newDownloadStatus = 1;

        SQLStatement sqlStatement = new SQLStatement("UPDATE tableName SET downloadColumn = downloadStatus WHERE nameColumn = categoryName;");
        sqlStatement.bindParam("tableName", CategoryTable.TABLE_NAME);
        sqlStatement.bindParam("downloadColumn", CategoryTable.DOWNLOADED_COLUMN);
        sqlStatement.bindParam("nameColumn", CategoryTable.NAME_COLUMN_PK);
        sqlStatement.bindParam("downloadStatus", "" + newDownloadStatus);
        sqlStatement.bindStringParam("categoryName", this.toString());

        DatabaseSQLExecuteResult result = DataManager.execute(sqlStatement);
        if (!result.isSuccessful()) {
            DialogBox.showError("Failed to get update download status for " + getNiceName() + ". \n" + result.getErrorMessage());
        }
    }
}
