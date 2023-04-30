package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.CrudRepository;
import ro.unibuc.inventorysystem.core.Depozit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class DepoziteRepository extends CrudRepository<Depozit> {
    private static final String TABLE_NAME = "depozite";
    private final AngajatRepository angajatRepository;

    public DepoziteRepository(AngajatRepository angajatRepository) {
        super(TABLE_NAME);
        this.angajatRepository = angajatRepository;
        fetchObjects();
    }

    @Override
    protected void fetchObjects() {
        final ResultSet rs = SQLRepository.INSTANCE.getObjects(getTable());
        try {
            while(rs.next()) {
                final Angajat mgr = angajatRepository.getById(rs.getInt("manager")).get();
                final Depozit d = new Depozit(rs.getString("nume"), rs.getString("address"), mgr);
                objects.put(rs.getInt("id"), d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new RuntimeException("Angajatul nu exista!");
        }
    }

    @Override
    protected PreparedStatement getCreatePreparedStatement(Depozit object) throws SQLException {
        final String queryTemplate = "INSERT INTO %s (nume, address, manager) VALUES (?, ?, ?)";
        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setString(1, object.getNume());
        ps.setString(2, object.getAdresa());
        final Optional<Integer> idManager = angajatRepository.findByObject(object.getManager());
        if (idManager.isEmpty()) {
            throw new RuntimeException("Angajatul nu exista!");
        }
        ps.setInt(3, idManager.get());
        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePreparedStatement(int id, Depozit object) throws SQLException {
        final String queryTemplate = "UPDATE %s SET nume = ?, address = ?, manager = ? WHERE id = ?";
        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setString(1, object.getNume());
        ps.setString(2, object.getAdresa());
        final Optional<Integer> idManager = angajatRepository.findByObject(object.getManager());
        if (idManager.isEmpty()) {
            throw new RuntimeException("Angajatul nu exista!");
        }
        ps.setInt(3, idManager.get());
        ps.setInt(4, id);
        return ps;
    }
}
