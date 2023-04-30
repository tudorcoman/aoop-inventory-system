package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.CrudRepository;
import ro.unibuc.inventorysystem.core.Depozit;
import ro.unibuc.inventorysystem.core.Produs;
import ro.unibuc.inventorysystem.core.TipTranzactie;
import ro.unibuc.inventorysystem.core.Tranzactie;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.Optional;

public class TranzactieRepository extends CrudRepository<Tranzactie> {
    private static final String TABLE_NAME = "tranzactii";
    private final ProdusRepository produsRepository;
    private final FurnizoriRepository furnizoriRepository;
    private final ClientiRepository clientiRepository;
    private final DepoziteRepository depozitRepository;

    public TranzactieRepository(ProdusRepository produsRepository, FurnizoriRepository furnizoriRepository, ClientiRepository clientiRepository, DepoziteRepository depozitRepository) {
        super(TABLE_NAME);
        this.produsRepository = produsRepository;
        this.furnizoriRepository = furnizoriRepository;
        this.clientiRepository = clientiRepository;
        this.depozitRepository = depozitRepository;
        fetchObjects();
    }

//    @Override
//    public boolean create(Tranzactie object) {
//        object.getDepozit().executaTranzactie(object);
//        return super.create(object);
//    }

    @Override
    public boolean update(int id, final Tranzactie object) {
        // tranzactiile sunt read-only
        return false;
    }

    @Override
    public boolean delete(int id) {
        // o tranzactie nu poate fi stearsa, dar in schimb puteam adauga o noua tranzactie "opusa" (care sa o anuleze pe cea existenta)

        final Optional<Tranzactie> opInitial = getById(id);
        if (opInitial.isEmpty()) {
            // nu s-a putut face update
            // TODO: add logging
            return false;
        }

        final Tranzactie initial = opInitial.get();
        final Tranzactie antagonic = new Tranzactie(id, initial.getProdus(), TipTranzactie.getInverse(initial.getTip()), initial.getTimestamp(), initial.getQuantity(), initial.getDepozit(), initial.getPartener());

        return create(antagonic);
    }

    @Override
    protected void fetchObjects() {
        final ResultSet rs = SQLRepository.INSTANCE.getObjects(getTable());
        try {
            while (rs.next()) {
                final int id = rs.getInt("id");
                final int idProdus = rs.getInt("product_id");
                final char x = rs.getString("partener_id").charAt(0);
                final int idPartener = Integer.valueOf(rs.getString("partener_id").substring(1));
                final int idDepozit = rs.getInt("depozit_id");
                final TipTranzactie tip = TipTranzactie.valueOf(rs.getString("tip"));
                final double quantity = rs.getDouble("quantity");
                final Timestamp timestamp = rs.getTimestamp("data_tranzactie");

                final Produs prod = produsRepository.getById(idProdus).get();
                Companie comp;
                if (x == 'F')
                    comp = furnizoriRepository.getById(idPartener).get();
                else
                    comp = clientiRepository.getById(idPartener).get();
                final Depozit dep = depozitRepository.getById(idDepozit).get();

                final Tranzactie tranzactie = new Tranzactie(id, prod, tip, timestamp, quantity, dep, comp);
                objects.put(id, tranzactie);
                dep.executaTranzactie(tranzactie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected PreparedStatement getCreatePreparedStatement(Tranzactie object) throws SQLException {
        final int idProdus = produsRepository.findByObject(object.getProdus()).get();
        String idPartener = "";
        if (furnizoriRepository.findByObject(object.getPartener()).isPresent()) {
            idPartener = "F" + String.valueOf(furnizoriRepository.findByObject(object.getPartener()).get());
        } else {
            idPartener = "C" + String.valueOf(clientiRepository.findByObject(object.getPartener()).get());
        }

        final int idDepozit = depozitRepository.findByObject(object.getDepozit()).get();

        final String queryTemplate = "INSERT INTO %s (product_id, partener_id, depozit_id, tip, quantity, data_tranzactie) VALUES (?, ?, ?, ?, ?, ?)";
        final String query = String.format(queryTemplate, getTable());
        final PreparedStatement ps = SQLRepository.INSTANCE.createPreparedStatement(query);
        ps.setInt(1, idProdus);
        ps.setString(2, idPartener);
        ps.setInt(3, idDepozit);
        ps.setObject(4, object.getTip().toString(), Types.OTHER);
        ps.setDouble(5, object.getQuantity());
        ps.setTimestamp(6, new java.sql.Timestamp(object.getTimestamp().getTime()));

        return ps;
    }

    @Override
    protected PreparedStatement getUpdatePreparedStatement(int id, Tranzactie object) throws SQLException {
        // tranzactiile sunt read-only
        return null;
    }
}
