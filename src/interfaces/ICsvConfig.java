package interfaces;

import java.util.List;

/**
 * Interface for configuring CSV file settings.
 */
public interface ICsvConfig {
    /**
     * Gets the headers for the CSV file.
     *
     * @return a list of headers for the CSV file.
     */
    List<String> getHeaders();

    /**
     * Gets the file path for the CSV file.
     *
     * @return the file path, as a string.
     */
    String getFilePath();
}
