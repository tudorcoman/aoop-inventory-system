package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.CrudRepository;
import ro.unibuc.inventorysystem.core.Produs;

import java.util.Random;

public class ProdusRepository extends CrudRepository<Produs> {
    private static final String TABLE_NAME = "TABEL_PRODUSE";

    public ProdusRepository() {
        super(TABLE_NAME);
    }

    @Override
    public boolean create(final Produs object) {
        final int id = new Random().nextInt(); // vom inlocui cu id-ul pe care il obtinem in urma inserarii in BD
        objects.put(id, object);
        return true;
    }

    @Override
    public boolean update(int id, final Produs object) {
        // insert sql part
        objects.put(id, object);
        return true;
    }

    @Override
    public boolean delete(int id) {
        // insert sql part
        objects.remove(id);
        return true;
    }
}
