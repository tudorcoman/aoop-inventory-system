package ro.unibuc.inventorysystem.infra.repository;

public class FurnizoriRepository extends CompanieRepository {
    private static final String TABLE_NAME = "TABEL_FURNIZORI";

    public FurnizoriRepository() {
        super(TABLE_NAME);
    }
}
