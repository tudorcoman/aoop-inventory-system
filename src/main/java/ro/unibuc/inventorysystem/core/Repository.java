package ro.unibuc.inventorysystem.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Repository<T> {
    boolean create(final T object);
    List<T> retrieve();
    Map<Integer, T> retrieveTable();
    Optional<T> getById(final int id);
    Optional<Integer> findByObject(final T object);

    boolean update(final int id, final T object);
    boolean delete(final int id);
}
