package ro.unibuc.inventorysystem.infra.repository;

import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.CrudRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public final class AngajatRepository extends CrudRepository<Angajat> {
    private static final String TABLE_NAME = "TABEL_ANGAJATI";

    public AngajatRepository() {
        super(TABLE_NAME);
    }

    @Override
    public boolean create(final Angajat object) {
        final int id = new Random().nextInt(); // vom inlocui cu id-ul pe care il obtinem in urma inserarii in BD
        objects.put(id, object);
        return true;
    }

    @Override
    public boolean update(int id, final Angajat object) {
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

    public List<Angajat> getTeamForAngajat(int id) {
        final Angajat a = objects.get(id);
        return objects.values()
                .stream()
                .filter(ang -> ang.getManager().isPresent() && ang.getManager().get().equals(a))
                .collect(Collectors.toList());
    }
}
