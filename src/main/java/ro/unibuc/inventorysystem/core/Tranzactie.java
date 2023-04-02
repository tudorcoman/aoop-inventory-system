package ro.unibuc.inventorysystem.core;

import java.util.Date;
import java.util.Objects;

public class Tranzactie {
    private final int id;
    private final Produs produs;
    private final TipTranzactie tip;
    private final Date timestamp;

    private final double quantity;

    private final Depozit depozit;

    private final Companie partener;

    public Tranzactie(int id, Produs produs, TipTranzactie tip, Date timestamp, double quantity, Depozit depozit, Companie partener) {
        this.id = id;
        this.produs = produs;
        this.tip = tip;
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.depozit = depozit;
        this.partener = partener;
    }

    public int getId() {
        return id;
    }

    public Produs getProdus() {
        return produs;
    }

    public TipTranzactie getTip() {
        return tip;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getQuantity() {
        return quantity;
    }

    public Depozit getDepozit() {
        return depozit;
    }

    public Companie getPartener() {
        return partener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tranzactie that = (Tranzactie) o;
        return id == that.id && Objects.equals(produs, that.produs) && tip == that.tip
                && Objects.equals(timestamp, that.timestamp) && quantity == that.quantity
                && Objects.equals(depozit, that.depozit) && Objects.equals(partener, that.partener);
    }

    @Override
    public String toString() {
        return "Tranzactie " + tip + "#" + id + "din data de " + timestamp + "\n" +
                produs.toString() + "\n" +
                "cantitate: " + quantity +
                " " + depozit.toString() +
                "\n" + partener.toString();
    }
}
