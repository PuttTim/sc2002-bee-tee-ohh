package models;

import java.util.List;

/**
 * Represents a filter, with a key and a list of values.
 */
public class Filter {
    private String key;
    private List<String> value;

    /**
     * Adds a value to the filter's list of values.
     *
     * @param input the value to be added.
     */
    public void addValue(String input) {
        value.add(input);
    }

    /**
     * Gets the key of the filter.
     *
     * @return the key of the filter.
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the list of values of the filter.
     *
     * @return the list of values.
     */
    public List<String> getValue() {
        return value;
    }

    /**
     * Sets the key for the filter.
     *
     * @param key the key to set for the filter.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Sets the list of values for the filter.
     *
     * @param value the list of values to set.
     */
    public void setValue(List<String> value) {
        this.value = value;
    }
}
