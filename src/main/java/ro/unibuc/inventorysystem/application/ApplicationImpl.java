package ro.unibuc.inventorysystem.application;

import ro.unibuc.inventorysystem.application.model.Application;
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
import ro.unibuc.inventorysystem.infra.repository.PersoanaRepository;
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

public final class ApplicationImpl implements Application {
    private final AngajatRepository angajatRepository;
    private final ClientiRepository clientiRepository;
    private final FurnizoriRepository furnizoriRepository;
    private final ProdusRepository produsRepository;
    private final TranzactieRepository tranzactieRepository;

    private final DepoziteRepository depoziteRepository;

    private final PersoanaRepository persoanaRepository;

    ApplicationImpl() {
        persoanaRepository = new PersoanaRepository();
        angajatRepository = new AngajatRepository(persoanaRepository);
        clientiRepository = new ClientiRepository(persoanaRepository);
        furnizoriRepository = new FurnizoriRepository(persoanaRepository);
        produsRepository = new ProdusRepository();
        depoziteRepository = new DepoziteRepository(angajatRepository);
        tranzactieRepository = new TranzactieRepository(produsRepository, furnizoriRepository, clientiRepository, depoziteRepository);
    }

    @Override
    public void adaugaProdus(Produs p) {
        produsRepository.create(p);
    }

    @Override
    public List<Produs> getProduse() {
        return produsRepository.retrieve();
    }

    @Override
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

    @Override
    public void actualizeazaProdus(int id, Produs p) {
        produsRepository.update(id, p);
    }

    @Override
    public Optional<Integer> searchProdus(Produs p) {
        return produsRepository.findByObject(p);
    }

    @Override
    public Optional<Produs> searchProdus(int id) {
        return produsRepository.getById(id);
    }

    @Override
    public void adaugaAngajat(Angajat a) {
        angajatRepository.create(a);
    }

    @Override
    public List<Angajat> getAngajati() {
        return angajatRepository.retrieve();
    }

    @Override
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

    @Override
    public Optional<Integer> searchAngajat(Angajat a) {
        return angajatRepository.findByObject(a);
    }

    @Override
    public Optional<Angajat> searchAngajat(int id) {
        return angajatRepository.getById(id);
    }

    @Override
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

    @Override
    public void adaugaIntrare(int idProdus, int idFurnizor, double cantitate, Date timestamp, int idDepozit) {
        adaugaTranzactie(idProdus,
                furnizoriRepository.getById(idFurnizor).orElseThrow(() -> new NoSuchElementException("Furnizorul nu exista")),
                cantitate,
                timestamp,
                idDepozit,
                TipTranzactie.IN);
    }

    @Override
    public void adaugaIesire(int idProdus, int idClient, double cantitate, Date timestamp, int idDepozit) {
        adaugaTranzactie(idProdus,
                clientiRepository.getById(idClient).orElseThrow(() -> new NoSuchElementException("Clientul nu exista")),
                cantitate,
                timestamp,
                idDepozit,
                TipTranzactie.OUT);
    }

    @Override
    public List<Tranzactie> getTranzactii() {
        return tranzactieRepository.retrieve();
    }

    @Override
    public void stergeTranzactie(int id) {
        tranzactieRepository.delete(id);
    }

    @Override
    public List<Tranzactie> getTranzactiiPerioada(Date start, Date end) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getTimestamp().after(start) && tranzactie.getTimestamp().before(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tranzactie> getTranzactiiPartener(Companie partener) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getPartener().equals(partener))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tranzactie> getTranzactiiProdus(int idProdus) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getProdus().equals(produsRepository
                        .getById(idProdus)
                        .orElseThrow(() -> new NoSuchElementException("Produsul nu exista"))))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tranzactie> getTranzactiiDepozit(int idDepozit) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getDepozit().equals(depoziteRepository
                        .getById(idDepozit)
                        .orElseThrow(() -> new NoSuchElementException("Depozitul nu exista"))))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tranzactie> getTranzactiiTip(TipTranzactie tip) {
        return tranzactieRepository.retrieve().stream()
                .filter(tranzactie -> tranzactie.getTip().equals(tip))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Integer> searchTranzactie(Tranzactie tr) {
        return tranzactieRepository.findByObject(tr);
    }

    @Override
    public Optional<Tranzactie> searchTranzactie(int id) {
        return tranzactieRepository.getById(id);
    }

    @Override
    public void adaugaDepozit(String nume, String adresa, int idAngajat) {
        final Optional<Angajat> ang = angajatRepository.getById(idAngajat);
        if (ang.isEmpty()) {
            throw new NoSuchElementException("Angajatul nu exista");
        }
        depoziteRepository.create(new Depozit(nume, adresa, ang.get()));
    }

    @Override
    public List<Depozit> getDepozite() {
        return depoziteRepository.retrieve();
    }

    @Override
    public Set<Depozit> getDepoziteOrdered() {
        return new HashSet<>(depoziteRepository.retrieve());
    }

    @Override
    public void updateDepozit(int id, String nume, String adresa, int idAngajat) {
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
    }

    @Override
    public void stergeDepozit(int id) {
        if (getTranzactiiDepozit(id).size() > 0) {
            throw new NoSuchElementException("Depozitul nu poate fi sters deoarece are tranzactii asociate");
        }

        depoziteRepository.delete(id);
    }

    @Override
    public Map<Produs, Double> getStocCantitativ(int idDepozit) {
        return depoziteRepository.getById(idDepozit).orElseThrow().getStoc();
    }

    @Override
    public Map<Produs, Double> getStocValoric(int idDepozit) {
        return depoziteRepository
                .getById(idDepozit)
                .orElseThrow()
                .getStoc()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getKey().getPretVanzare() * entry.getValue()));
    }

    @Override
    public void schimbaManagerDepozit(int idDepozit, int idAngajat) {
        depoziteRepository
                .getById(idDepozit)
                .orElseThrow()
                .setManager(angajatRepository.getById(idAngajat).orElseThrow());
    }

    @Override
    public Optional<Integer> searchDepozit(Depozit depozit) {
        return depoziteRepository.findByObject(depozit);
    }

    @Override
    public Optional<Depozit> searchDepozit(int id) {
        return depoziteRepository.getById(id);
    }

    @Override
    public void adaugaFurnizor(Companie c) {
        furnizoriRepository.create(c);
    }

    @Override
    public void adaugaClient(Companie c) {
        clientiRepository.create(c);
    }

    @Override
    public List<Companie> getFurnizori() {
        return furnizoriRepository.retrieve();
    }

    @Override
    public List<Companie> getClienti() {
        return clientiRepository.retrieve();
    }

    @Override
    public void stergeFurnizor(int id) {
        if (getTranzactiiPartener(furnizoriRepository.getById(id).orElseThrow()).size() > 0) {
            throw new NoSuchElementException("Furnizorul nu poate fi sters deoarece are tranzactii asociate");
        }
        furnizoriRepository.delete(id);
    }

    @Override
    public void stergeClient(int id) {
        if (getTranzactiiPartener(clientiRepository.getById(id).orElseThrow()).size() > 0) {
            throw new NoSuchElementException("Clientul nu poate fi sters deoarece are tranzactii asociate");
        }
        clientiRepository.delete(id);
    }

    @Override
    public void actualizeazaFurnizor(int id, Companie c) {
        furnizoriRepository.update(id, c);
    }

    @Override
    public void actualizeazaClient(int id, Companie c) {
        clientiRepository.update(id, c);
    }

    @Override
    public Optional<Integer> searchFurnizor(Companie c) {
        return furnizoriRepository.findByObject(c);
    }

    @Override
    public Optional<Integer> searchClient(Companie c) {
        return clientiRepository.findByObject(c);
    }

    @Override
    public Optional<Companie> searchFurnizor(int id) {
        return furnizoriRepository.getById(id);
    }

    @Override
    public Optional<Companie> searchClient(int id) {
        return clientiRepository.getById(id);
    }

    @Override
    public Map<Integer, Angajat> getAngajatiTable() {
        return angajatRepository.retrieveTable();
    }

    @Override
    public Map<Integer, Companie> getFurnizoriTable() {
        return furnizoriRepository.retrieveTable();
    }

    @Override
    public Map<Integer, Companie> getClientiTable() {
        return clientiRepository.retrieveTable();
    }

    @Override
    public Map<Integer, Depozit> getDepoziteTable() {
        return depoziteRepository.retrieveTable();
    }

    @Override
    public Map<Integer, Tranzactie> getTranzactiiTable() {
        return tranzactieRepository.retrieveTable();
    }

    @Override
    public Map<Integer, Produs> getProduseTable() {
        return produsRepository.retrieveTable();
    }
}
