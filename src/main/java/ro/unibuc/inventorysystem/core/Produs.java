package ro.unibuc.inventorysystem.core;

import java.util.Objects;

public class Produs {
    private final String nume;
    private final String categorie;
    private final double pretCumparare;
    private final double pretVanzare;

    public Produs() {
        this.nume = "";
        this.categorie = "";
        this.pretCumparare = this.pretVanzare = 0.0;
    }

    public Produs(String nume, String categorie, double pretCumparare, double pretVanzare) {
        this.nume = nume;
        this.categorie = categorie;
        this.pretCumparare = pretCumparare;
        this.pretVanzare = pretVanzare;
    }

    public String getNume() {
        return nume;
    }

    public String getCategorie() {
        return categorie;
    }

    public double getPretCumparare() {
        return pretCumparare;
    }

    public double getPretVanzare() {
        return pretVanzare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produs produs = (Produs) o;
        return Double.compare(produs.pretCumparare, pretCumparare) == 0 && Double.compare(produs.pretVanzare, pretVanzare) == 0 && Objects.equals(nume, produs.nume) && Objects.equals(categorie, produs.categorie);
    }

    @Override
    public String toString() {
        return "Produs " + nume + " (" + categorie + "), pret cumparare " + pretCumparare + ", pret vanzare " + pretVanzare;
    }
}
