package app.data;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;

/**
 * Handles creating, loading, writing to and reading from the database
 * @author Brett Taylor
 */
public class DataManager {
    /**
     * The location of the default files that need to be copied to the local users file.
     */
    private final static String ORIGINAL_DEFAULTS_LOCATION = "/defaults";

    /**
     * Location of the data files. Should end with a slash.
     */
    public final static String DATA_LOCATION = System.getProperty("user.home") + "/.OSRSUtilities/";

    /**
     * Name of the data file
     */
    private final static String DATABASE_FILENAME = "OSRSUtilitiesDatabase.sqlite";

    /**
     * Full path of the SQLite data. Adds together the location and filename
     */
    private final static String DATABASE_FULL_LOCATION = DATA_LOCATION + DATABASE_FILENAME;

    /**
     * Full path of the SQLite data with the required sql prefix added.
     */
    private final static String DATABASE_SQL_LOCATION = "jdbc:sqlite:" + DATABASE_FULL_LOCATION;

    /**
     * The statement
     */
    private static Statement theStatement = null;

    /**
     * Handles setting up the data manager.
     */
    public static DataLoadResult start() {
        if (!doesDirectoryExist()) {
            if (!createDirectory()) {
                return DataLoadResult.DIRECTORY_FAILED_TO_CREATE;
            }
        }

        if (!doesDatabaseExist()) {
            if (!copyDatabase()) {
                return DataLoadResult.DATABASE_FILE_FAILED_TO_COPY;
            }
        }

        try {
            Connection theConnection = DriverManager.getConnection(DATABASE_SQL_LOCATION);
            theStatement = theConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return DataLoadResult.CONNECTION_FAILED_TO_CREATE;
        }

        return DataLoadResult.SUCCESS;
    }

    /**
     * @return True if the directory exists.
     */
    private static boolean doesDirectoryExist() {
        File directory = new File(DATA_LOCATION);
        return directory.exists() && directory.isDirectory();
    }

    /**
     * Creates the directory where the data will be stored..
     */
    private static boolean createDirectory() {
        return new File(DATA_LOCATION).mkdirs();
    }

    /**
     * @return True if the data file exist - indicating the data has already been created.
     */
    private static boolean doesDatabaseExist() {
        File file = new File(DATABASE_FULL_LOCATION);
        return file.exists() && file.isFile();
    }

    /**
     * Creates the data file.
     */
    private static boolean copyDatabase() {
        File source = new File(DataManager.class.getResource(ORIGINAL_DEFAULTS_LOCATION).getPath());
        File dest = new File(DATA_LOCATION);
        try {
            FileUtils.copyDirectory(source, dest);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executes a sql statement.
     * @param sqlStatement The sql statement to be executed.
     * @return Returns the result object of the executed sql.
     */
    public static DatabaseSQLExecuteResult execute(SQLStatement sqlStatement) {
        try {
            ResultSet resultSet = null;
            if (sqlStatement.willUseQuery()) {
                resultSet = theStatement.executeQuery(sqlStatement.getSQLString());
            }
            else {
                theStatement.executeUpdate(sqlStatement.getSQLString());
            }
            return DatabaseSQLExecuteResult.success(resultSet);
        } catch (SQLException e) {
            return DatabaseSQLExecuteResult.failed(e.getMessage());
        }
    }
}
