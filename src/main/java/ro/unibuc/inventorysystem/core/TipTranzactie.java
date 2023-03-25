package ro.unibuc.inventorysystem.core;

public enum TipTranzactie {
    IN,
    OUT;

    public static TipTranzactie getInverse(final TipTranzactie t) {
        return (t == IN) ? OUT : IN;
    }
}
