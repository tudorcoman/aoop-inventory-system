package ro.unibuc.inventorysystem.application.model;

import ro.unibuc.inventorysystem.core.Depozit;
import ro.unibuc.inventorysystem.core.Produs;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DepozitApplication {
    void adaugaDepozit(String nume, String adresa, int idAngajat);
    void stergeDepozit(int id);
    void updateDepozit(int id, String nume, String adresa, int idAngajat);
    List<Depozit> getDepozite();
    Set<Depozit> getDepoziteOrdered();
    Map<Produs, Double> getStocCantitativ(int idDepozit);
    Map<Produs, Double> getStocValoric(int idDepozit);
    void schimbaManagerDepozit(int idDepozit, int idAngajat);
    Optional<Integer> searchDepozit(Depozit depozit);
    Optional<Depozit> searchDepozit(int id);
    Map<Integer, Depozit> getDepoziteTable();
}
