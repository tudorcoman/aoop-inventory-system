package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.CrudRepository;

import java.util.Random;

class CompanieRepository extends CrudRepository<Companie> {
    CompanieRepository(String table) {
        super(table);
    }

    @Override
    public boolean create(final Companie object) {
        final int id = new Random().nextInt(); // vom inlocui cu id-ul pe care il obtinem in urma inserarii in BD
        objects.put(id, object);
        return true;
    }

    @Override
    public boolean update(int id, final Companie object) {
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
