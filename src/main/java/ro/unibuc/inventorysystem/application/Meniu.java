package ro.unibuc.inventorysystem.application;

import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.Depozit;
import ro.unibuc.inventorysystem.core.Persoana;
import ro.unibuc.inventorysystem.core.Produs;
import ro.unibuc.inventorysystem.core.Tranzactie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public enum Meniu {
    INSTANCE;

    private Application a;

    Meniu() {
        this.a = new Application();
    }

    public void runMeniu() {
        while(true) {
            start();
        }
    }

    void start() {
        Scanner in = new Scanner(System.in).useDelimiter("\\n");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        System.out.println("Alege o operatie: ");

        System.out.println("[1]. Adauga");
        System.out.println("[2]. Sterge");
        System.out.println("[3]. Actualizeaza");
        System.out.println("[4]. Cauta");
        System.out.println("[5]. Dump");

        int op = in.nextInt();

        if (op == 1) {
            System.out.println("Alege ce vrei sa adaugi: ");
            System.out.println("[1]. Angajat");
            System.out.println("[2]. Depozit");
            System.out.println("[3]. Furnizor");
            System.out.println("[4]. Client");
            System.out.println("[5]. Tranzactie");
            System.out.println("[6]. Produs");
            System.out.println("[7]. Inapoi");

            int op1 = in.nextInt();
            if (op1 == 1) {
                a.adaugaAngajat(inputAngajat(in));
                System.out.println("Angajat adaugat cu succes!");
            } else if (op1 == 2) {
                System.out.println("Introduceti numele depozitului: ");
                String nume = in.next();
                System.out.println("Introduceti adresa depozitului: ");
                String adresa = in.next();
                System.out.println("Introduceti id-ul managerului depozitului: ");
                int idAngajat = in.nextInt();
                a.adaugaDepozit(nume, adresa, idAngajat);
                System.out.println("Depozit adaugat cu succes!");
            } else if (op1 == 3 || op1 == 4) {
                final Companie c = inputCompanie(in);
                if (op1 == 3) {
                    a.adaugaFurnizor(c);
                    System.out.println("Furnizor adaugat cu succes!");
                } else {
                    a.adaugaClient(c);
                    System.out.println("Client adaugat cu succes!");
                }
            } else if (op1 == 5) {
                System.out.println("Introduceti id-ul produsului: ");
                int idProdus = in.nextInt();
                System.out.println("Introduceti cantitatea produsului: ");
                double cantitate = in.nextDouble();
                System.out.println("Introduceti id-ul depozitului: ");
                int idDepozit = in.nextInt();
                System.out.println("Introduceti data tranzactiei (format yyyy-MM-dd hh:mm): ");
                Date data;
                try {
                    data = dateFormat.parse(in.next());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Introduceti tipul tranzactiei (intrare/iesire): ");
                String tip = in.next();

                if ("intrare".equals(tip)) {
                    System.out.println("Introduceti id-ul furnizorului: ");
                    int idFurnizor = in.nextInt();
                    a.adaugaIntrare(idProdus, idFurnizor, cantitate, data, idDepozit);
                } else if ("iesire".equals(tip)) {
                    System.out.println("Introduceti id-ul clientului: ");
                    int idClient = in.nextInt();
                    a.adaugaIesire(idProdus, idClient, cantitate, data, idDepozit);
                } else {
                    throw new RuntimeException("Tipul tranzactiei nu este valid! (intrare/iesire)");
                }
                System.out.println("Tranzactie adaugata cu succes!");
            } else if (op1 == 6) {
                a.adaugaProdus(inputProdus(in));
                System.out.println("Produs adaugat cu succes!");
            } else if (op1 == 7) {
                start();
            }
        } else if (op == 2) {
            System.out.println("Alege ce vrei sa stergi: ");
            System.out.println("[1]. Angajat");
            System.out.println("[2]. Depozit");
            System.out.println("[3]. Furnizor");
            System.out.println("[4]. Client");
            System.out.println("[5]. Produs");
            System.out.println("[6]. Inapoi");

            int op1 = in.nextInt();
            if (op1 == 1) {
                System.out.println("Introduceti id-ul angajatului: ");
                int id = in.nextInt();
                a.stergeAngajat(id);
                System.out.println("Angajat sters cu succes!");
            } else if (op1 == 2) {
                System.out.println("Introduceti id-ul depozitului: ");
                int id = in.nextInt();
                a.stergeDepozit(id);
                System.out.println("Depozit sters cu succes!");
            } else if (op1 == 3) {
                System.out.println("Introduceti id-ul furnizorului: ");
                int id = in.nextInt();
                a.stergeFurnizor(id);
                System.out.println("Furnizor sters cu succes!");
            } else if (op1 == 4) {
                System.out.println("Introduceti id-ul clientului: ");
                int id = in.nextInt();
                a.stergeClient(id);
                System.out.println("Client sters cu succes!");
            } else if (op1 == 5) {
                System.out.println("Introduceti id-ul produsului: ");
                int id = in.nextInt();
                a.stergeProdus(id);
                System.out.println("Produs sters cu succes!");
            } else if (op1 == 6) {
                start();
            }
        } else if (op == 3) {
            System.out.println("Alege ce vrei sa modifici: ");
            System.out.println("[1]. Angajat");
            System.out.println("[2]. Depozit");
            System.out.println("[3]. Furnizor");
            System.out.println("[4]. Client");
            System.out.println("[5]. Produs");
            System.out.println("[6]. Inapoi");

            int op1 = in.nextInt();
            if (op1 == 1) {
                System.out.println("Introduceti id-ul angajatului: ");
                int id = in.nextInt();
                Angajat angajat = inputAngajat(in);
                a.actualizeazaAngajat(id, angajat);
            } else if (op1 == 2) {
                System.out.println("Introduceti id-ul depozitului: ");
                int id = in.nextInt();
                System.out.println("Introduceti numele depozitului: ");
                String nume = in.next();
                System.out.println("Introduceti adresa depozitului: ");
                String adresa = in.next();
                System.out.println("Introduceti id-ul managerului depozitului: ");
                int idAngajat = in.nextInt();

                a.updateDepozit(id, nume, adresa, idAngajat);
            } else if (op1 == 3 || op1 == 4) {
                System.out.println("Introduceti id-ul companiei: ");
                int id = in.nextInt();
                final Companie c = inputCompanie(in);
                if (op1 == 3) {
                    a.actualizeazaFurnizor(id, c);
                } else {
                    a.actualizeazaClient(id, c);
                }
            } else if (op1 == 5) {
                System.out.println("Introduceti id-ul produsului: ");
                int id = in.nextInt();
                final Produs p = inputProdus(in);
                a.actualizeazaProdus(id, p);
            }
        } else if (op == 4) {
            System.out.println("Alege ce vrei sa cauti: ");
            System.out.println("[1]. Angajat");
            System.out.println("[2]. Depozit");
            System.out.println("[3]. Furnizor");
            System.out.println("[4]. Client");
            System.out.println("[5]. Tranzactie");
            System.out.println("[6]. Produs");
            System.out.println("[7]. Inapoi");

            int op1 = in.nextInt();
            if (op1 == 1) {
                System.out.println("Introduceti id-ul angajatului: ");
                int id = in.nextInt();
                final Optional<Angajat> ang = a.searchAngajat(id);
                if (ang.isPresent()) {
                    System.out.println(ang.get());
                } else {
                    System.out.println("Angajatul nu a fost gasit!");
                }
            } else if (op1 == 2) {
                System.out.println("Introduceti id-ul depozitului: ");
                int id = in.nextInt();
                final Optional<Depozit> d = a.searchDepozit(id);
                if (d.isPresent()) {
                    System.out.println(d.get());
                } else {
                    System.out.println("Depozitul nu a fost gasit!");
                }
            } else if (op1 == 3) {
                System.out.println("Introduceti id-ul furnizorului: ");
                int id = in.nextInt();
                final Optional<Companie> f = a.searchFurnizor(id);
                if (f.isPresent()) {
                    System.out.println(f.get());
                } else {
                    System.out.println("Furnizorul nu a fost gasit!");
                }
            } else if (op1 == 4) {
                System.out.println("Introduceti id-ul clientului: ");
                int id = in.nextInt();
                final Optional<Companie> c = a.searchClient(id);
                if (c.isPresent()) {
                    System.out.println(c.get());
                } else {
                    System.out.println("Clientul nu a fost gasit!");
                }
            } else if (op1 == 5) {
                System.out.println("Introduceti id-ul tranzactiei: ");
                int id = in.nextInt();
                final Optional<Tranzactie> t = a.searchTranzactie(id);
                if (t.isPresent()) {
                    System.out.println(t.get());
                } else {
                    System.out.println("Tranzactia nu a fost gasita!");
                }
            } else if (op1 == 6) {
                System.out.println("Introduceti id-ul produsului: ");
                int id = in.nextInt();
                final Optional<Produs> p = a.searchProdus(id);
                if (p.isPresent()) {
                    System.out.println(p.get());
                } else {
                    System.out.println("Produsul nu a fost gasit!");
                }
            } else if (op1 == 7) {
                start();
            }
        } else if (op == 5) {
            System.out.println("Alege ce vrei sa vezi: ");
            System.out.println("[1]. Angajat");
            System.out.println("[2]. Depozit");
            System.out.println("[3]. Furnizor");
            System.out.println("[4]. Client");
            System.out.println("[5]. Tranzactie");
            System.out.println("[6]. Produs");
            System.out.println("[7]. Inapoi");

            int op1 = in.nextInt();
            if (op1 == 1) {
                for (Map.Entry<Integer, Angajat> entry : a.getAngajatiTable().entrySet()) {
                    System.out.println(entry.getKey() + ". " + entry.getValue());
                }
            } else if (op1 == 2) {
                for (Map.Entry<Integer, Depozit> entry : a.getDepoziteTable().entrySet()) {
                    System.out.println(entry.getKey() + ". " + entry.getValue());
                }
            } else if (op1 == 3) {
                for (Map.Entry<Integer, Companie> entry : a.getFurnizoriTable().entrySet()) {
                    System.out.println(entry.getKey() + ". " + entry.getValue());
                }
            } else if (op1 == 4) {
                for (Map.Entry<Integer, Companie> entry : a.getClientiTable().entrySet()) {
                    System.out.println(entry.getKey() + ". " + entry.getValue());
                }
            } else if (op1 == 5) {
                for (Map.Entry<Integer, Tranzactie> entry : a.getTranzactiiTable().entrySet()) {
                    System.out.println(entry.getKey() + ". " + entry.getValue());
                }
            } else if (op1 == 6) {
                for (Map.Entry<Integer, Produs> entry : a.getProduseTable().entrySet()) {
                    System.out.println(entry.getKey() + ". " + entry.getValue());
                }
            } else if (op1 == 7) {
                start();
            }
        }
    }

    private Angajat inputAngajat(Scanner in) {
        System.out.println("Introduceti numele angajatului: ");
        String nume = in.next();
        System.out.println("Introduceti prenumele angajatului: ");
        String prenume = in.next();
        System.out.println("Introduceti CNP-ul angajatului: ");
        long cnp = in.nextLong();
        System.out.println("Introduceti telefonul angajatului: ");
        String telefon = in.next();
        System.out.println("Introduceti adresa de email a angajatului: ");
        String email = in.next();
        System.out.println("Introduceti ID manager (-1 daca nu are manager): ");
        int idManager = in.nextInt();
        return new Angajat(nume, prenume, cnp, telefon, email, a.searchAngajat(idManager));
    }

    private Produs inputProdus(Scanner in) {
        System.out.println("Introduceti numele produsului: ");
        String nume = in.next();
        System.out.println("Introduceti categoria produsului: ");
        String categorie = in.next();
        System.out.println("Introduceti pretul de cumparare al produsului: ");
        double pretCumparare = in.nextDouble();
        System.out.println("Introduceti pretul de vanzare al produsului: ");
        double pretVanzare = in.nextDouble();
        return new Produs(nume, categorie, pretCumparare, pretVanzare);
    }

    private Companie inputCompanie(Scanner in) {
        System.out.println("Introduceti numele companiei: ");
        String numeCompanie = in.next();
        System.out.println("Introduceti CUI-ul companiei: ");
        long cui = in.nextLong();
        System.out.println("Introduceti adresa companiei: ");
        String adresaCompanie = in.next();

        System.out.println("Introduceti numele persoanei de contact: ");
        String nume = in.next();
        System.out.println("Introduceti prenumele persoanei de contact: ");
        String prenume = in.next();
        System.out.println("Introduceti CNP-ul persoanei de contact: ");
        long cnp = in.nextLong();
        System.out.println("Introduceti telefonul persoanei de contact: ");
        String telefon = in.next();
        System.out.println("Introduceti adresa de email a persoanei de contact: ");
        String email = in.next();

        return new Companie(numeCompanie, cui, adresaCompanie, new Persoana(nume, prenume, cnp, telefon, email));
    }
}
