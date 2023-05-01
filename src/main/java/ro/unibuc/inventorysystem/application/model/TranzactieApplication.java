package ro.unibuc.inventorysystem.application.model;

import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.TipTranzactie;
import ro.unibuc.inventorysystem.core.Tranzactie;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TranzactieApplication {
    void adaugaIntrare(int idProdus, int idFurnizor, double cantitate, Date timestamp, int idDepozit);
    void adaugaIesire(int idProdus, int idClient, double cantitate, Date timestamp, int idDepozit);
    List<Tranzactie> getTranzactii();
    void stergeTranzactie(int id);
    List<Tranzactie> getTranzactiiPerioada(Date start, Date end);
    List<Tranzactie> getTranzactiiProdus(int idProdus);
    List<Tranzactie> getTranzactiiDepozit(int idDepozit);
    List<Tranzactie> getTranzactiiPartener(Companie partener);
    List<Tranzactie> getTranzactiiTip(TipTranzactie tip);
    Optional<Integer> searchTranzactie(Tranzactie tr);
    Optional<Tranzactie> searchTranzactie(int id);
    Map<Integer, Tranzactie> getTranzactiiTable();
}
