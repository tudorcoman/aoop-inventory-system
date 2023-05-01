package ro.unibuc.inventorysystem.application;

import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.Depozit;
import ro.unibuc.inventorysystem.core.Persoana;
import ro.unibuc.inventorysystem.core.Produs;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Testing {
    void testing() {
        final ApplicationImpl a = new ApplicationImpl();

        final Angajat tudor = new Angajat("Tudor", "Coman", 1234567890124L, "0798765432", "tudor@gmail.com", Optional.empty());
        a.adaugaAngajat(tudor);
        final Optional<Integer> idT = a.searchAngajat(tudor);

        final Angajat ion = new Angajat("Ion", "Popescu", 1234567890123L, "0712345678", "ion.popescu@info.ro", Optional.of(tudor));
        a.adaugaAngajat(ion);
        final Optional<Integer> idI = a.searchAngajat(ion);

        a.getAngajati().forEach(System.out::println);

        final Produs laptop = new Produs("Laptop", "Laptop Lenovo ThinkPad T480", 1000, 1500);
        final Produs mouse = new Produs("Mouse", "Mouse Logitech M185", 50, 100);

        a.adaugaProdus(laptop);
        a.adaugaProdus(mouse);
        a.getProduse().stream().map(Object::toString).forEach(System.out::println);

        final Persoana contactLenovo = new Persoana("John", "Doe", 1234567890123L, "1234567890", "johndoe@lenovo.com");
        final Persoana contactLogitech = new Persoana("John", "Doe", 1234567890123L, "1234567890", "johndoe@logitech.com");

        final Companie lenovo = new Companie("Lenovo", 12345, "Lenovo House, 100 New Bridge Street, London, EC4V 6JA, United Kingdom", contactLenovo);
        final Companie logitech = new Companie("Logitech", 12345, "Logitech House, 100 New Bridge Street, London, EC4V 6JA, United Kingdom", contactLogitech);

        a.adaugaFurnizor(lenovo);
        a.adaugaFurnizor(logitech);

        a.adaugaDepozit("Bucuresti", "Bucuresti, Strada Ion Creanga, Nr. 1", idI.get());
        final Depozit depozitBucuresti = a.getDepozite().get(0);
        final int idDepozitBucuresti = a.searchDepozit(depozitBucuresti).get();

        final int idLaptop = a.searchProdus(laptop).get();
        final int idMouse = a.searchProdus(mouse).get();

        a.adaugaIntrare(idLaptop, a.searchFurnizor(lenovo).get(), 1000, new Date(), idDepozitBucuresti);
        a.adaugaIntrare(idMouse, a.searchFurnizor(logitech).get(), 50, new Date(), idDepozitBucuresti);

        a.getStocCantitativ(idDepozitBucuresti).forEach((produs, cantitate) -> System.out.println(produs.getNume() + ": " + cantitate));

        final Companie client = new Companie("Client", 12345, "Client House, 100 New Bridge Street, London, EC4V 6JA, United Kingdom", new Persoana("X", "Y", 1234567890123L, "1234567890", "x@y.com"));
        a.adaugaClient(client);

        a.adaugaIesire(idLaptop, a.searchClient(client).get(), 100, new Date(), idDepozitBucuresti);
        a.getStocCantitativ(idDepozitBucuresti).forEach((produs, cantitate) -> System.out.println(produs.getNume() + ": " + cantitate));

        a.adaugaDepozit("Cluj", "Cluj, Strada Ion Creanga, Nr. 1", idT.get());

        final List<Depozit> listaDepozite = a.getDepozite();
        listaDepozite.forEach(System.out::println);

        //a.stergeAngajat(idT.get());
        final Set<Depozit> setDepozite = a.getDepoziteOrdered();
        setDepozite.forEach(System.out::println);
    }
}
