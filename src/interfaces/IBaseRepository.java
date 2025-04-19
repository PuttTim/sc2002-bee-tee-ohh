package interfaces;

import java.util.List;

/**
 * Interface for a repository that manages a collection of items.
 *
 * @param <T> the type of items in the repository.
 */
public interface IBaseRepository<T> {

    /**
     * Retrieves all items in repository.
     *
     * @return a list of all items.
     */
    List<T> getAll();

    /**
     * Saves a list of items to repository.
     *
     * @param items the list of items to save.
     */
    void saveAll(List<T> items);

    /**
     * Loads data into the repository.
     */
    void load();
}
