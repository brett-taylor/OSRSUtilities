package app.data;

import app.runescape.Item;
import app.runescape.ItemTable;
import app.wiki.WikiCategories;
import app.wiki.WikiList;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

/**
 * Handles creating, loading, writing to and reading from the data.
 * @author Brett Taylor
 */
public class DatabaseManager {
    /**
     * Location of the SQLite data file.
     * Should end with a slash.
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
     * Holds all of the  tables that need to be created or upgraded.
     * Will be empty after the constructor has ran.
     */
    private static DatabaseTable[] tables = {
            new ItemTable()
    };

    /**
     * Stores whether this is the first run of the new data or not.
     */
    private static boolean firstRunWithDatabase = false;

    /**
     * Stores the last macro message to due with populating the data.
     */
    private static String currentMacroStepMessage = "";

    /**
     * Stores the last micro message to due with populating the data.
     */
    private static String currentMicroStepMessage = "";

    /**
     * Constructor of the data manager.
     */
    public static DatabaseLoadResult start() {
        if (!doesDatabaseExist() || !doesDatabaseExist())
            firstRunWithDatabase = true;

        if (!doesDirectoryExist()) {
            if (!createDirectory()) {
                return DatabaseLoadResult.DIRECTORY_FAILED_TO_CREATE;
            }
        }

        if (!doesDatabaseExist()) {
            if (!createDatabase()) {
                return DatabaseLoadResult.DATABASE_FILE_FAILED_TO_CREATE;
            }
        }

        if (!createTables()) {
            return DatabaseLoadResult.TABLES_FAILED_TO_CREATE;
        }

        if (firstRunWithDatabase) {
            new Thread(DatabaseManager::populateDatabase).start();
        } else {
            currentMacroStepMessage = "done";
        }

        return DatabaseLoadResult.SUCCESS;
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
    private static boolean createDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_SQL_LOCATION)) {
            return conn != null;
        } catch (SQLException ignored) {
        }

        return false;
    }

    /**
     * Creates (if needed) the tables.
     * @return true if it was successful.
     */
    private static boolean createTables() {
        for (DatabaseTable table : tables) {
            if (table != null) {
                table.createAndUpdate();
            }
        }

        tables = null;
        return true;
    }

    /**
     * Executes a sql statement.
     * @param sql The sql to be executed.
     * @return Returns the result object of the executed sql.
     */
    public static DatabaseSQLExecuteResult execute(String sql) {
        Connection connection;
        Statement statement;

        try {
            connection = DriverManager.getConnection(DATABASE_SQL_LOCATION);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        try {
            statement = connection.createStatement();
            ResultSet resultSet = null;
            if (sql.startsWith("SELECT"))
                resultSet = statement.executeQuery(sql);
            else
                statement.executeUpdate(sql);
            return DatabaseSQLExecuteResult.success(resultSet, sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks to see if a table exists
     * @param tableName the table to check if it exists
     * @return true if the table exists, false otherwise.
     */
    public static boolean doesTableExist(String tableName) {
        try (Connection connection = DriverManager.getConnection(DATABASE_SQL_LOCATION)) {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            return tables.next();
        } catch (SQLException ignored) {
        }

        return false;
    }

    /**
     * Checks to see if a given table has a column.
     * @param tableName the table to check.
     * @param columnName the column name
     * @return true if the given table has the column specified.
     */
    public static boolean doesTableHaveColumn(String tableName, String columnName) {
        try (Connection connection = DriverManager.getConnection(DATABASE_SQL_LOCATION)) {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet columns = dbm.getColumns(null, null, tableName, columnName);
            return columns.next();
        } catch (SQLException ignored) {
        }

        return false;
    }

    /**
     * Will populate the data.
     */
    public static void populateDatabase() {
        populateDatabaseWithCategory(WikiCategories.HELMETS);
        populateDatabaseWithCategory(WikiCategories.CAPES);
        populateDatabaseWithCategory(WikiCategories.NECKLACE);
        populateDatabaseWithCategory(WikiCategories.QUIVER);
        populateDatabaseWithCategory(WikiCategories.WEAPON);
        populateDatabaseWithCategory(WikiCategories.TORSO);
        populateDatabaseWithCategory(WikiCategories.SHIELD);
        populateDatabaseWithCategory(WikiCategories.LEGS);
        populateDatabaseWithCategory(WikiCategories.GLOVES);
        populateDatabaseWithCategory(WikiCategories.BOOTS);
        populateDatabaseWithCategory(WikiCategories.RING);
        populateDatabaseWithCategory(WikiCategories.TWO_HANDED_WEAPONS);
        populateDatabaseWithCategory(WikiCategories.FOOD);
        populateDatabaseWithCategory(WikiCategories.POTIONS);
        populateDatabaseWithCategory(WikiCategories.STORAGE_IETMS);
        populateDatabaseWithCategory(WikiCategories.STACKABLE_IETMS);
        currentMacroStepMessage = "done";
    }

    /**
     * Creates a data insert for each object in that list in its respective table.
     * @param category the category to get the objects from.
     */
    private static void populateDatabaseWithCategory(WikiCategories category) {
        currentMacroStepMessage = "Grabbing " + category.toString();
        WikiList list = WikiList.all(category);
        if (category.isItem()) {
            for (HashMap<String, Object> pair : list.getObjects().values()) {
                Item.create((String) pair.get("title"), (String) pair.get("url"), category.typeOfItem(), (Integer) pair.get("id"));
                currentMicroStepMessage = "Grabbing: " + pair.get("title");
            }
        }
    }

    /**
     * @return The the latest macro message.
     */
    public static String getCurrentMacroStep() {
        return currentMacroStepMessage;
    }

    /**
     * @return The the latest micro message.
     */
    public static String getCurrentMicroStep() {
        return currentMicroStepMessage;
    }
}
