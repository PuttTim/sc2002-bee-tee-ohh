package interfaces;

import java.util.List;

/**
 * <p>Interface for basic operations on a repository.</p>
 *
 * @param <T> The type of items in the repository.
 */
public interface IBaseRepository<T> {
    /**
     * <p>Get all the items.</p>
     *
     * @return A list of all items.
     */
    List<T> getAll();

    /**
     * <p>Save a list of items.</p>
     *
     * @param items The items to save.
     */
    void saveAll(List<T> items);

    /**
     * <p>Load the items into the repository.</p>
     */
    void load();
}
