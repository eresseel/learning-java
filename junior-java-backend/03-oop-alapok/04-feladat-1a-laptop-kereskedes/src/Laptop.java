/**
 * Egy laptop a kereskedés kínálatában: a valós világ laptopjának a kereskedés
 * szempontjából lényeges adatai (gyártó, modell, cikkszám, ár).
 */
public class Laptop {

    private final String gyarto;
    private final String modell;
    private final String cikkszam;
    private int ar;

    /**
     * Létrehoz egy új laptopot.
     *
     * @param gyarto   a gyártó neve
     * @param modell   a modell neve
     * @param cikkszam a kereskedés cikkszáma (egyedi azonosító)
     * @param ar       az ár forintban
     */
    public Laptop(String gyarto, String modell, String cikkszam, int ar) {
        this.gyarto = gyarto;
        this.modell = modell;
        this.cikkszam = cikkszam;
        this.ar = ar;
    }

    /** @return a gyártó neve */
    public String getGyarto() {
        return gyarto;
    }

    /** @return a modell neve */
    public String getModell() {
        return modell;
    }

    /** @return a cikkszám */
    public String getCikkszam() {
        return cikkszam;
    }

    /** @return az ár forintban */
    public int getAr() {
        return ar;
    }

    /**
     * Módosítja az árat; negatív értéket nem fogad el.
     *
     * @param ar az új ár forintban
     * @return true, ha a módosítás megtörtént
     */
    public boolean setAr(int ar) {
        if (ar < 0) {
            return false;
        }
        this.ar = ar;
        return true;
    }

    /**
     * Árengedmény alkalmazása százalékban.
     *
     * @param szazalek az engedmény mértéke (0–100)
     */
    public void akcio(int szazalek) {
        // Egész számokkal számolunk: előbb szorzunk, aztán osztunk, hogy ne vesszen el pontosság.
        if (szazalek > 0 && szazalek <= 100) {
            ar = ar * (100 - szazalek) / 100;
        }
    }

    /** @return a laptop adatai táblázatos sorként */
    @Override
    public String toString() {
        return String.format("%-8s %-10s %-14s %,10d Ft", cikkszam, gyarto, modell, ar);
    }
}
