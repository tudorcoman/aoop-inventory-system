package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.CrudRepository;
import ro.unibuc.inventorysystem.core.TipTranzactie;
import ro.unibuc.inventorysystem.core.Tranzactie;

import java.util.Optional;
import java.util.Random;

public class TranzactieRepository extends CrudRepository<Tranzactie> {
    private static final String TABLE_NAME = "TABEL_TRANZACTII";

    public TranzactieRepository() {
        super(TABLE_NAME);
    }

    @Override
    public boolean create(final Tranzactie object) {
        final int id = new Random().nextInt(); // vom inlocui cu id-ul pe care il obtinem in urma inserarii in BD
        objects.put(id, object);
        return true;
    }

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
}
