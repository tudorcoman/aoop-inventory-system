package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.CrudRepository;
import ro.unibuc.inventorysystem.core.Produs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class ProdusRepository extends CrudRepository<Produs> {
    private static final String TABLE_NAME = "produse";

    public ProdusRepository() {
        super(TABLE_NAME);
        fetchObjects();
    }

    @Override
    protected void fetchObjects() {
        final ResultSet rs = SQLRepository.INSTANCE.getObjects(getTable());
        try {
            while(rs.next()) {
                final Produs p = new Produs(rs.getString("name"), rs.getString("category"), rs.getDouble("buying_price"), rs.getDouble("selling_price"));
                objects.put(rs.getInt("id"), p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement getCreatePreparedStatement(Produs object) throws SQLException {
        final String queryTemplate = "INSERT INTO %s (name, category, buying_price, selling_price) VALUES (?, ?, ?, ?)";
        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setString(1, object.getNume());
        ps.setString(2, object.getCategorie());
        ps.setDouble(3, object.getPretCumparare());
        ps.setDouble(4, object.getPretVanzare());
        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePreparedStatement(int id, Produs object) throws SQLException {
        final String queryTemplate = "UPDATE %s SET name = ?, category = ?, buying_price = ?, selling_price = ? WHERE id = ?";
        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setString(1, object.getNume());
        ps.setString(2, object.getCategorie());
        ps.setDouble(3, object.getPretCumparare());
        ps.setDouble(4, object.getPretVanzare());
        ps.setInt(5, id);
        return ps;
    }
}
