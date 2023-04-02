package ro.unibuc.inventorysystem;

import ro.unibuc.inventorysystem.application.Application;
import ro.unibuc.inventorysystem.application.Meniu;
import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.Depozit;
import ro.unibuc.inventorysystem.core.Persoana;
import ro.unibuc.inventorysystem.core.Produs;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Meniu.INSTANCE.runMeniu();
    }
}
