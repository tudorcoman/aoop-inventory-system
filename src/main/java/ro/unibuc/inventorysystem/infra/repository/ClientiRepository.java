package ro.unibuc.inventorysystem.infra.repository;

public final class ClientiRepository extends CompanieRepository {
    private static final String TABLE_NAME = "TABEL_CLIENTI";

    public ClientiRepository() {
        super(TABLE_NAME);
    }

}
