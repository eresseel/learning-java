/**
 * A felhasználói jogosultságok felsorolása (enum) a fájlban használt kódjukkal.
 * <p>
 * Az enum egy speciális osztály, amelynek csak a felsorolt, előre létrehozott
 * példányai léteznek – így érvénytelen jogosultság nem jöhet létre.
 */
public enum Jogosultsag {
    VENDEG(1, "Vendég"),
    FELHASZNALO(2, "Felhasználó"),
    ADMIN(3, "Admin");

    private final int kod;
    private final String megnevezes;

    /**
     * @param kod        a fájlban szereplő számkód
     * @param megnevezes a kiírt név
     */
    Jogosultsag(int kod, String megnevezes) {
        this.kod = kod;
        this.megnevezes = megnevezes;
    }

    /** @return a számkód */
    public int getKod() {
        return kod;
    }

    /** @return a megnevezés */
    public String getMegnevezes() {
        return megnevezes;
    }

    /**
     * Számkód alapján megkeresi a jogosultságot.
     *
     * @param kod a számkód (1–3)
     * @return a megfelelő jogosultság
     * @throws IllegalArgumentException ha nincs ilyen kódú jogosultság
     */
    public static Jogosultsag kodAlapjan(int kod) {
        for (Jogosultsag j : values()) {
            if (j.kod == kod) {
                return j;
            }
        }
        throw new IllegalArgumentException("Ismeretlen jogosultságkód: " + kod);
    }
}
