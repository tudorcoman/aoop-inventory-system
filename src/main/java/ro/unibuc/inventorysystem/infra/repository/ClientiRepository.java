package ro.unibuc.inventorysystem.infra.repository;

public final class ClientiRepository extends CompanieRepository {
    private static final String TABLE_NAME = "clienti";

    public ClientiRepository(PersoanaRepository persoanaRepository) {
        super(TABLE_NAME, persoanaRepository);
    }
}
