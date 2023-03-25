package ro.unibuc.inventorysystem.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class CrudRepository<T> implements Repository<T> {
    private String table;
    protected Map<Integer, T> objects;

    public CrudRepository(String table) {
        this.table = table;
        this.objects = new HashMap<>();
    }

    @Override
    public final List<T> retrieve() {
        return new ArrayList<>(objects.values());
    }

    protected String getTable() {
        return table;
    }

    @Override
    public Optional<T> getById(int id) {
        return Optional.ofNullable(objects.get(id));
    }

    @Override
    public Optional<Integer> findByObject(T object) {
        return objects
                .entrySet()
                .stream()
                .filter(entry -> object.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
