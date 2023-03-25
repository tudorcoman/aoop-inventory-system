package ro.unibuc.inventorysystem.core;

import java.util.HashMap;
import java.util.Map;

public class Depozit implements Comparable<Depozit> {
    private final String nume;
    private final String adresa;
    private final Map<Produs, Double> stoc;
    private Angajat manager;

    public Depozit(String nume, String adresa, Angajat manager) {
        this.nume = nume;
        this.adresa = adresa;
        this.manager = manager;
        this.stoc = new HashMap<>();
    }

    public Depozit(String nume, String adresa, Map<Produs, Double> stoc, Angajat manager) {
        this.nume = nume;
        this.adresa = adresa;
        this.stoc = stoc;
        this.manager = manager;
    }

    public void executaTranzactie(final Tranzactie t) {
        final int op = (t.getTip() == TipTranzactie.IN) ? 1 : -1;
        final double oldQuantity = this.stoc.getOrDefault(t.getProdus(), 0.0);
        this.stoc.put(t.getProdus(), oldQuantity + t.getQuantity() * op);
    }

    public String getNume() {
        return nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public Map<Produs, Double> getStoc() {
        return stoc;
    }

    public Angajat getManager() {
        return manager;
    }

    public void setManager(Angajat manager) {
        this.manager = manager;
    }

    @Override
    public String toString() {
        return "Depozitul " + nume + "\n"
                + "Adresa: " + adresa + "\n"
                + "Administrat de: " + manager.toString();
    }

    private Double totalBucati() {
        return stoc.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    @Override
    public int compareTo(Depozit o) {
        return Double.compare(totalBucati(), o.totalBucati());
    }
}
