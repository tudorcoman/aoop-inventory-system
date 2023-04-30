package ro.unibuc.inventorysystem.infra.repository;

public class FurnizoriRepository extends CompanieRepository {
    private static final String TABLE_NAME = "furnizori";

    public FurnizoriRepository(PersoanaRepository persoanaRepository) {
        super(TABLE_NAME, persoanaRepository);
    }

}
