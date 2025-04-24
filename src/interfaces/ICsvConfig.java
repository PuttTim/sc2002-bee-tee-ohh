package interfaces;

import java.util.List;

/**
 * <p>Interface for handling CSV file configuration.</p>
 */
public interface ICsvConfig {
    /**
     * <p>Gets the headers for the CSV file.</p>
     *
     * @return A list of headers.
     */
    List<String> getHeaders();

    /**
     * <p>Gets the file path of the CSV file.</p>
     *
     * @return The file path as a string.
     */
    String getFilePath();
}
