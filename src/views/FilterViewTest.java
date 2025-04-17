package views;

import java.util.Map;
import java.util.Set;

public class FilterViewTest {
    public static void main(String[] args) {
        FilterView fv = new FilterView();
        Map<String, Set<String>> filters = fv.selectProjectFilters();
        for (Map.Entry<String, Set<String>> entry : filters.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
