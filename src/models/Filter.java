package models;

import java.util.List;

public class Filter {
    private String key;
    private List<String> value;

    public Filter(String key, List<String> value) {
        this.key = key;
        this.value = value;
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
