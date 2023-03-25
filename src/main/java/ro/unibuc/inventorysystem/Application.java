package ro.unibuc.inventorysystem;

import ro.unibuc.inventorysystem.core.Angajat;
import ro.unibuc.inventorysystem.core.Companie;
import ro.unibuc.inventorysystem.core.Depozit;
import ro.unibuc.inventorysystem.core.Produs;
import ro.unibuc.inventorysystem.core.TipTranzactie;
import ro.unibuc.inventorysystem.core.Tranzactie;
import ro.unibuc.inventorysystem.infra.repository.AngajatRepository;
import ro.unibuc.inventorysystem.infra.repository.ClientiRepository;
import ro.unibuc.inventorysystem.infra.repository.DepoziteRepository;
import ro.unibuc.inventorysystem.infra.repository.FurnizoriRepository;
import ro.unibuc.inventorysystem.infra.repository.ProdusRepository;
import ro.unibuc.inventorysystem.infra.repository.TranzactieRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class Application {
    private final AngajatRepository angajatRepository;
    private final ClientiRepository clientiRepository;
    private final FurnizoriRepository furnizoriRepository;
    private final ProdusRepository produsRepository;
    private final TranzactieRepository tranzactieRepository;

    private final DepoziteRepository depoziteRepository;

    public Application() {
        angajatRepository = new AngajatRepository();
        clientiRepository = new ClientiRepository();
        furnizoriRepository = new FurnizoriRepository();
        produsRepository = new ProdusRepository();
        tranzactieRepository = new TranzactieRepository();
        depoziteRepository = new DepoziteRepository();
    }

    public void adaugaProdus(Produs p) {
        produsRepository.create(p);
    }

    public List<Produs> getProduse() {
        return produsRepository.retrieve();
    }

    public void stergeProdus(int id) {
        final long tr = tranzactieRepository
                .retrieve()
                .stream()
                .filter(tranzactie -> tranzactie
                        .getProdus()
                        .equals(produsRepository
                                .getById(id)
                                .orElseThrow()
                        )
                ).count();
        if (tr > 0) {
            throw new RuntimeException("Produsul nu poate fi sters deoarece exista tranzactii asociate");
        }
        produsRepository.delete(id);
    }

    public void actualizeazaProdus(int id, Produs p) {
        produsRepository.update(id, p);
    }

    public Optional<Integer> searchProdus(Produs p) {
        return produsRepository.findByObject(p);
    }

    public Optional<Produs> searchProdus(int id) {
        return produsRepository.getById(id);
    }

    public void adaugaAngajat(Angajat a) {
        angajatRepository.create(a);
    }

    public List<Angajat> getAngajati() {
        return angajatRepository.retrieve();
    }

    public void stergeAngajat(int id) {
        final Optional<Angajat> ang = searchAngajat(id);
        if (ang.isEmpty()) {
            throw new NoSuchElementException("Angajatul nu exista");
        }
        final List<Angajat> echipa = angajatRepository.getTeamForAngajat(id);
        if (echipa.size() > 0) {
            throw new RuntimeException("Angajatul nu poate fi sters deoarece are angajati subordonati");
        }
        final long depozite = depoziteRepository.retrieve().stream().filter(depozit -> depozit.getManager().equals(ang.get())).count();
        if (depozite > 0) {
            throw new RuntimeException("Angajatul nu poate fi sters deoarece este manager la " + depozite + " depozite");
        }
        angajatRepository.delete(id);
    }

    public Optional<Integer> searchAngajat(Angajat a) {
        return angajatRepository.findByObject(a);
    }

    public Optional<Angajat> searchAngajat(int id) {
        return angajatRepository.getById(id);
    }

    public void actualizeazaAngajat(int id, Angajat nou) {
        angajatRepository.update(id, nou);
    }

    private void adaugaTranzactie(int idProdus, Companie partener, double cantitate, Date timestamp, int idDepozit, TipTranzactie tipTranzactie) {
        final Depozit d = depoziteRepository.getById(idDepozit).orElseThrow(() -> new NoSuchElementException("Depozitul nu exista"));
        final Tranzactie tr = new Tranzactie(0,
                produsRepository.getById(idProdus).orElseThrow(() -> new NoSuchElementException("Produsul nu exista")),
                tipTranzactie,
                timestamp,
                cantitate,
                d,
                partener);
        d.executaTranzactie(tr);
        tranzactieRepository.create(tr);
    }

    public void adaugaIntrare(int idProdus, int idFurnizor, double cantitate, Date timestamp, int idDepozit) {
        adaugaTranzactie(idProdus,
                furnizoriRepository.getById(idFurnizor).orElseThrow(() -> new NoSuchElementException("Furnizorul nu exista")),
                cantitate,
                timestamp,
                idDepozit,
                TipTranzactie.IN);
    }

    public void adaugaIesire(int idProdus, int idClient, double cantitate, Date timestamp, int idDepozit) {
        adaugaTranzactie(idProdus,
                clientiRepository.getById(idClient).orElseThrow(() -> new NoSuchElementException("Clientul nu exista")),
                cantitate,
                timestamp,
                idDepozit,
                TipTranzactie.OUT);
    }

    public List<Tranzactie> getTranzactii() {
        return tranzactieRepository.retrieve();
    }

    public void anuleazaTranzactie(int id) {
        tranzactieRepository.delete(id);
    }

    public List<Tranzactie> getTranzactiiPerioada(Date start, Date end) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getTimestamp().after(start) && tranzactie.getTimestamp().before(end))
                .collect(Collectors.toList());
    }

    public List<Tranzactie> getTranzactiiPartener(Companie partener) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getPartener().equals(partener))
                .collect(Collectors.toList());
    }

    public List<Tranzactie> getTranzactiiProdus(int idProdus) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getProdus().equals(produsRepository
                                        .getById(idProdus)
                                        .orElseThrow(() -> new NoSuchElementException("Produsul nu exista"))))
                .collect(Collectors.toList());
    }

    public List<Tranzactie> getTranzactiiDepozit(int idDepozit) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getDepozit().equals(depoziteRepository
                                        .getById(idDepozit)
                                        .orElseThrow(() -> new NoSuchElementException("Depozitul nu exista"))))
                .collect(Collectors.toList());
    }

    public List<Tranzactie> getTranzactiiTip(TipTranzactie tip) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getTip().equals(tip))
                .collect(Collectors.toList());
    }

    public Optional<Integer> searchTranzactie(Tranzactie tr) {
        return tranzactieRepository.findByObject(tr);
    }

    public Optional<Tranzactie> searchTranzactie(int id) {
        return tranzactieRepository.getById(id);
    }

    public void adaugaDepozit(String nume, String adresa, int idAngajat) {
        final Optional<Angajat> ang = angajatRepository.getById(idAngajat);
        if (ang.isEmpty()) {
            throw new NoSuchElementException("Angajatul nu exista");
        }
        depoziteRepository.create(new Depozit(nume, adresa, ang.get()));
    }

    public List<Depozit> getDepozite() {
        return depoziteRepository.retrieve();
    }

    public Set<Depozit> getDepoziteOrdered() {
        return new HashSet<>(depoziteRepository.retrieve());
    }

    public List<Depozit> updateDepozit(int id, String nume, String adresa, int idAngajat) {
        final Optional<Angajat> ang = angajatRepository.getById(idAngajat);
        if (ang.isEmpty()) {
            throw new NoSuchElementException("Angajatul nu exista");
        }

        final Optional<Depozit> dep = depoziteRepository.getById(id);
        if (dep.isEmpty()) {
            throw new NoSuchElementException("Depozitul nu exista");
        }

        final Depozit depozitNou = new Depozit(nume, adresa, dep.get().getStoc(), ang.get());
        depoziteRepository.update(id, depozitNou);
        return depoziteRepository.retrieve();
    }

    public void stergeDepozit(int id) {
        if (getTranzactiiDepozit(id).size() > 0) {
            throw new NoSuchElementException("Depozitul nu poate fi sters deoarece are tranzactii asociate");
        }

        depoziteRepository.delete(id);
    }

    public Map<Produs, Double> getStocCantitativ(int idDepozit) {
        return depoziteRepository.getById(idDepozit).orElseThrow().getStoc();
    }

    public Map<Produs, Double> getStocValoric(int idDepozit) {
        return depoziteRepository
                .getById(idDepozit)
                .orElseThrow()
                .getStoc()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getKey().getPretVanzare() * entry.getValue()));
    }

    public void schimbaManagerDepozit(int idDepozit, int idAngajat) {
        depoziteRepository
                .getById(idDepozit)
                .orElseThrow()
                .setManager(angajatRepository.getById(idAngajat).orElseThrow());
    }

    public Optional<Integer> searchDepozit(Depozit depozit) {
        return depoziteRepository.findByObject(depozit);
    }

    public Optional<Depozit> searchDepozit(int id) {
        return depoziteRepository.getById(id);
    }

    public void adaugaFurnizor(Companie c) {
        furnizoriRepository.create(c);
    }

    public void adaugaClient(Companie c) {
        clientiRepository.create(c);
    }

    public List<Companie> getFurnizori() {
        return furnizoriRepository.retrieve();
    }

    public List<Companie> getClienti() {
        return clientiRepository.retrieve();
    }

    public void stergeFurnizor(int id) {
        if (getTranzactiiPartener(furnizoriRepository.getById(id).orElseThrow()).size() > 0) {
            throw new NoSuchElementException("Furnizorul nu poate fi sters deoarece are tranzactii asociate");
        }
        furnizoriRepository.delete(id);
    }

    public void stergeClient(int id) {
        if (getTranzactiiPartener(clientiRepository.getById(id).orElseThrow()).size() > 0) {
            throw new NoSuchElementException("Clientul nu poate fi sters deoarece are tranzactii asociate");
        }
        clientiRepository.delete(id);
    }

    public void actualizeazaFurnizor(int id, Companie c) {
        furnizoriRepository.update(id, c);
    }

    public void actualizeazaClient(int id, Companie c) {
        clientiRepository.update(id, c);
    }

    public Optional<Integer> searchFurnizor(Companie c) {
        return furnizoriRepository.findByObject(c);
    }

    public Optional<Integer> searchClient(Companie c) {
        return clientiRepository.findByObject(c);
    }

    public Optional<Companie> searchFurnizor(int id) {
        return furnizoriRepository.getById(id);
    }

    public Optional<Companie> searchClient(int id) {
        return clientiRepository.getById(id);
    }
}
