package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.CrudRepository;
import ro.unibuc.inventorysystem.core.Persoana;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class PersoanaRepository extends CrudRepository<Persoana> {

    private static final String TABLE_NAME = "persoane";

    public PersoanaRepository() {
        super(TABLE_NAME);
        fetchObjects();
    }

    @Override
    protected void fetchObjects() {
        final ResultSet rs = SQLRepository.INSTANCE.getObjects(getTable());
        try {
            while(rs.next()) {
                final Persoana p = new Persoana(rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getLong("cnp"),
                        rs.getString("phone"),
                        rs.getString("email"));
                objects.put(rs.getInt("id"), p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement getCreatePreparedStatement(Persoana object) throws SQLException {
        final String queryTemplate = "INSERT INTO %s (first_name, last_name, cnp, phone, email) VALUES (?, ?, ?, ?, ?)";
        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setString(1, object.getFirstName());
        ps.setString(2, object.getLastName());
        ps.setLong(3, object.getCnp());
        ps.setString(4, object.getPhone());
        ps.setString(5, object.getEmail());
        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePreparedStatement(int id, Persoana object) throws SQLException {
        final String queryTemplate = "UPDATE %s SET first_name = ?, last_name = ?, cnp = ?, phone = ?, email = ? WHERE id = ?";
        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setString(1, object.getFirstName());
        ps.setString(2, object.getLastName());
        ps.setLong(3, object.getCnp());
        ps.setString(4, object.getPhone());
        ps.setString(5, object.getEmail());
        ps.setInt(6, id);
        return ps;
    }
}
