package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.CrudRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class CompanieRepository extends CrudRepository<Companie> {
    private final PersoanaRepository persoanaRepository;
    CompanieRepository(String table, PersoanaRepository persoanaRepository) {
        super(table);
        this.persoanaRepository = persoanaRepository;
        fetchObjects();
    }

    @Override
    protected void fetchObjects() {
        final ResultSet rs = SQLRepository.INSTANCE.getObjects(getTable());
        try {
            while(rs.next()) {
                final Companie c = new Companie(rs.getString("nume"),
                        rs.getLong("cui"),
                        rs.getString("adresa"),
                        persoanaRepository.getById(rs.getInt("id_persoana_contact")).get());
                objects.put(rs.getInt("id"), c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean create(Companie object) {
        return persoanaRepository.create(object.getPersoanaContact()) && super.create(object);
    }

    @Override
    public boolean update(int id, Companie object) {
        final int idPersoanaContact = persoanaRepository.findByObject(object.getPersoanaContact()).get();
        return persoanaRepository.update(idPersoanaContact, object.getPersoanaContact()) && super.update(id, object);
    }


    @Override
    protected PreparedStatement getCreatePreparedStatement(Companie object) throws SQLException {
        final String queryTemplate = "INSERT INTO %s (nume, cui, adresa, id_persoana_contact) VALUES (?, ?, ?, ?)";
        final int idPersoanaContact = persoanaRepository.findByObject(object.getPersoanaContact()).get();

        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setString(1, object.getNume());
        ps.setLong(2, object.getCui());
        ps.setString(3, object.getAdresa());
        ps.setInt(4, idPersoanaContact);
        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePreparedStatement(int id, Companie object) throws SQLException {
        final String queryTemplate = "UPDATE %s SET nume = ?, cui = ?, adresa = ?, id_persoana_contact = ? WHERE id = ?";
        final int idPersoanaContact = persoanaRepository.findByObject(object.getPersoanaContact()).get();

        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setString(1, object.getNume());
        ps.setLong(2, object.getCui());
        ps.setString(3, object.getAdresa());
        ps.setInt(4, idPersoanaContact);
        ps.setInt(5, id);
        return ps;
    }
}
