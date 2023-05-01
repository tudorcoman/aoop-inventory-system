package ro.unibuc.inventorysystem.application.model;

import ro.unibuc.inventorysystem.core.Companie;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CompanieApplication {
    void adaugaFurnizor(Companie c);
    void adaugaClient(Companie c);
    void stergeFurnizor(int id);
    void stergeClient(int id);
    void actualizeazaFurnizor(int id, Companie c);
    void actualizeazaClient(int id, Companie c);
    Optional<Integer> searchFurnizor(Companie c);
    Optional<Integer> searchClient(Companie c);
    Optional<Companie> searchFurnizor(int id);
    Optional<Companie> searchClient(int id);
    List<Companie> getFurnizori();
    List<Companie> getClienti();
    Map<Integer, Companie> getFurnizoriTable();
    Map<Integer, Companie> getClientiTable();

}
