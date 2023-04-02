package ro.unibuc.inventorysystem.core;

public class Companie {
    private final String nume;
    private final long cui;
    private final String adresa;
    private Persoana persoanaContact;

    public Companie() {
        this.nume = "";
        this.cui = 0;
        this.adresa = "";
        this.persoanaContact = new Persoana();
    }

    public Companie(String nume, long cui, String adresa, Persoana persoanaContact) {
        this.nume = nume;
        this.cui = cui;
        this.adresa = adresa;
        this.persoanaContact = persoanaContact;
    }

    public void setPersoanaContact(Persoana persoanaContact) {
        this.persoanaContact = persoanaContact;
    }

    public String getNume() {
        return nume;
    }

    public long getCui() {
        return cui;
    }

    public String getAdresa() {
        return adresa;
    }

    public Persoana getPersoanaContact() {
        return persoanaContact;
    }

    @Override
    public String toString() {
        return "Companie " + nume +
                ", cui " + cui +
                ", adresa " + adresa +
                ", persoana de contact " + persoanaContact.toString();
    }
}
