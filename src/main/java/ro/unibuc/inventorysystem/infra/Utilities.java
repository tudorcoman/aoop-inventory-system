package ro.unibuc.inventorysystem.infra;

public final class Utilities {
    private Utilities() { }

    public static boolean containsNonAlphaChars(final String str) {
        return !str.chars().allMatch(Character::isLetter);
    }
}
