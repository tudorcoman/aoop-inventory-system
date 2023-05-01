package ro.unibuc.inventorysystem.application.model;

import ro.unibuc.inventorysystem.core.Angajat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AngajatApplication {
    void adaugaAngajat(Angajat a);
    List<Angajat> getAngajati();
    void stergeAngajat(int id);
    void actualizeazaAngajat(int id, Angajat a);
    Optional<Integer> searchAngajat(Angajat a);
    Optional<Angajat> searchAngajat(int id);
    Map<Integer, Angajat> getAngajatiTable();
}
