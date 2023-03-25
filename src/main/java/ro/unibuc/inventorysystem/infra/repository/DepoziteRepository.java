package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.CrudRepository;
import ro.unibuc.inventorysystem.core.Depozit;

import java.util.Random;

public final class DepoziteRepository extends CrudRepository<Depozit> {
    private static final String TABLE_NAME = "TABEL_DEPOZITE";

    public DepoziteRepository() {
        super(TABLE_NAME);
    }

    @Override
    public boolean create(final Depozit object) {
        final int id = new Random().nextInt(); // vom inlocui cu id-ul pe care il obtinem in urma inserarii in BD
        objects.put(id, object);
        return true;
    }

    @Override
    public boolean update(int id, final Depozit object) {
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
