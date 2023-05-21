package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.CrudRepository;
import ro.unibuc.inventorysystem.core.Persoana;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class AngajatRepository extends CrudRepository<Angajat> {
    private static final String TABLE_NAME = "angajati";
    private final PersoanaRepository persoanaRepository;

    public AngajatRepository(PersoanaRepository persoanaRepository) {
        super(TABLE_NAME);
        this.persoanaRepository = persoanaRepository;
        fetchObjects();
    }

    public List<Angajat> getTeamForAngajat(int id) {
        final Angajat a = objects.get(id);
        return objects.values()
                .stream()
                .filter(ang -> ang.getManager().isPresent() && ang.getManager().get().equals(a))
                .collect(Collectors.toList());
    }

    @Override
    protected void fetchObjects() {
        final ResultSet rs = SQLRepository.INSTANCE.getObjects(getTable());
        try {
            while(rs.next()) {
                final int id = rs.getInt("id");
                final int idManager = rs.getInt("manager_id");
                final Persoana p = persoanaRepository.getById(id).get();
                final Optional<Angajat> mgr = getById(idManager);
                final Angajat a = new Angajat(p.getFirstName(), p.getLastName(), p.getCnp(), p.getPhone(), p.getEmail(), mgr);
                objects.put(id, a);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean create(Angajat object) {
        return persoanaRepository.create(object) && super.create(object);
    }

    @Override
    public boolean update(int id, Angajat object) {
        return persoanaRepository.update(id, object) && super.update(id, object);
    }

    @Override
    protected PreparedStatement getCreatePreparedStatement(Angajat object) throws SQLException {
        final int id = persoanaRepository.findByObject(object).get();
        final int managerId = object.getManager().isPresent() ? findByObject(object.getManager().get()).orElse(-1) : -1;

        final String queryTemplate = "INSERT INTO %s (id, manager_id) VALUES (?, ?)";
        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setInt(1, id);
        if (managerId == -1) {
            ps.setNull(2, java.sql.Types.INTEGER);
        } else {
            ps.setInt(2, managerId);
        }

        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePreparedStatement(int id, Angajat object) throws SQLException {
        final int managerId = object.getManager().isPresent() ? findByObject(object.getManager().get()).orElse(-1) : -1;

        final String queryTemplate = "UPDATE %s SET manager_id = ? WHERE id = ?";
        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        if (managerId == -1) {
            ps.setNull(1, java.sql.Types.INTEGER);
        } else {
            ps.setInt(1, managerId);
        }
        ps.setInt(2, id);

        return ps;
    }

    @Override
    public boolean delete(int id) {
        return super.delete(id) && persoanaRepository.delete(id) ;
    }
}
