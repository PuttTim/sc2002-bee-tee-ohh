package models;

import java.util.List;

/**
 * Represents a filter used to search or query a collection of data based on a key-value pair.
 * <p>
 * This class allows adding and setting filter values, which can be used for filtering lists of data
 * based on a specific key.
 * </p>
 */
public class Filter {
    private String key;
    private List<String> value;

    public Filter(String key, List<String> value) {
        this.key = key;
        this.value = value;
    }
    
    /**
     * Adds a value to the list of filter values.
     *
     * @param input the value to add to the filter's value list
     */
    public void addValue(String input) {
        value.add(input);
    }

    /**
     * Gets the key used for filtering.
     *
     * @return the key used for filtering
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the list of filter values.
     *
     * @return the list of values used for filtering
     */
    public List<String> getValue() {
        return value;
    }

    /**
     * Sets the key used for filtering.
     *
     * @param key the key to use for filtering
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Sets the list of values used for filtering.
     *
     * @param value the list of values to use for filtering
     */
    public void setValue(List<String> value) {
        this.value = value;
    }
}
