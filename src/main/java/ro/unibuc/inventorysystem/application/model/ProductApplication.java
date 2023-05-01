package ro.unibuc.inventorysystem.application.model;

import ro.unibuc.inventorysystem.core.Produs;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductApplication {
    void adaugaProdus(Produs p);
    List<Produs> getProduse();
    void stergeProdus(int id);
    void actualizeazaProdus(int id, Produs p);
    Optional<Integer> searchProdus(Produs p);
    Optional<Produs> searchProdus(int id);
    Map<Integer, Produs> getProduseTable();
}
