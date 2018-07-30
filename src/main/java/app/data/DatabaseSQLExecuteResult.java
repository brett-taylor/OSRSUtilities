package app.data;

import java.sql.ResultSet;

/**
 * Handles the data that is needed to be known from an attempted sql execute.
 * Handles both successful and failed sql executes.
 * @author Brett Taylor
 */
public class DatabaseSQLExecuteResult {
    /**
     * The sql execution result string.
     */
    private static final String SUCCESS_MESSAGE = "Success";

    /**
     * The error message
     */
    private String errorMessage;

    /**
     * The result set
     */
    private ResultSet resultSet;

    /**
     * The sql statement that was used.
     */
    private String sqlStatement;

    /**
     * Constructor for a sql execute.
     * @param message The return message from the attempted execute of sql.
     * @param resultSet the results from the executed sql.
     * @param sqlStatement the sql statement that was executed.
     */
    private DatabaseSQLExecuteResult(String message, ResultSet resultSet, String sqlStatement) {
        this.errorMessage = message;
        this.resultSet = resultSet;
        this.sqlStatement = sqlStatement;
    }

    /**
     * Creates a successful sql execute result.
     * @param resultSet the results from the successful sql execute.
     * @param sqlStatement the sql statement that was executed.
     * @return a successful sql execute result.
     */
    public static DatabaseSQLExecuteResult success(ResultSet resultSet, String sqlStatement) {
        return new DatabaseSQLExecuteResult(SUCCESS_MESSAGE, resultSet, sqlStatement);
    }

    /**
     * Creates a failed sql execute result.
     * @param message why the sql result failed.
     * @param sqlStatement the sql statement that was executed.
     * @return a failed sql execute result with the message why.
     */
    public static DatabaseSQLExecuteResult failed(String message, String sqlStatement) {
        return new DatabaseSQLExecuteResult(message, null, sqlStatement);
    }

    /**
     * @return True if the sql execution was successful.
     */
    public boolean isSuccessful() {
        return errorMessage.equals(SUCCESS_MESSAGE);
    }

    /**
     * @return The error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return If it was successful returns the resultSet from the executed sql otherwise null.
     */
    public ResultSet getResultSet() {
        return isSuccessful() ? resultSet : null;
    }

    /**
     * @return The sql statement that was executed.
     */
    public String getSqlStatementUsed() {
        return sqlStatement;
    }
}
