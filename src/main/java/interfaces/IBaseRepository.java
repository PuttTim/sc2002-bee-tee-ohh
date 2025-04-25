package interfaces;

import java.util.List;

public interface IBaseRepository<T> {
    List<T> getAll();
    void saveAll(List<T> items);
    void load();
}
