package models;

import java.util.List;

public class Filter {
    private String key;
    private List<String> value;

    public void addValue(String input) {
        value.add(input);
    }

    public String getKey() {
        return key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
