package app.data;

/**
 * Enum: The possible results from the creation and loading of the data.
 * @author Brett Taylor
 */
public enum DataLoadResult {
    SUCCESS,
    DIRECTORY_FAILED_TO_CREATE,
    DATABASE_FILE_FAILED_TO_COPY,
    CONNECTION_FAILED_TO_CREATE
}
