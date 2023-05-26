package ro.unibuc.inventorysystem.core;

import ro.unibuc.inventorysystem.infra.repository.SQLRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    protected abstract void fetchObjects();
    protected abstract PreparedStatement getCreatePreparedStatement(T object) throws SQLException;
    protected abstract PreparedStatement getUpdatePreparedStatement(int id, T object) throws SQLException;

    @Override
    public boolean create(final T object) {
        try {
            final PreparedStatement ps = getCreatePreparedStatement(object);
            ps.executeUpdate();
            final ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            final int id = rs.getInt(1);
            if (object instanceof Tranzactie) {
                final Tranzactie object2 = new Tranzactie(id, ((Tranzactie) object).getProdus(), ((Tranzactie) object).getTip(), ((Tranzactie) object).getTimestamp(), ((Tranzactie) object).getQuantity(), ((Tranzactie) object).getDepozit(), ((Tranzactie) object).getPartener());
                objects.put(id, (T) object2);
            } else {
                objects.put(id, object);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public final List<T> retrieve() {
        return new ArrayList<>(objects.values());
    }
    @Override
    public final Map<Integer, T> retrieveTable() {
        return new HashMap<>(objects);
    }

    @Override
    public boolean update(int id, final T object) {
        try {
            final PreparedStatement ps = getUpdatePreparedStatement(id, object);
            ps.executeUpdate();
            objects.put(id, object);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        final String queryTemplate = "DELETE FROM %s WHERE id = ?";
        final String query = String.format(queryTemplate, table);
        try {
            final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            objects.remove(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
