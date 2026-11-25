/**
 * Általános termék a vegyeskereskedés (ABC) raktárában.
 * <p>
 * Minden termékről tudjuk: megnevezés, cikkszám, ár, darabszám.
 * Ez az osztály az ős a Tejtermek és a Pekaru számára, de önmagában is
 * példányosítható: így tároljuk azokat a termékeket, amelyek egyik
 * kategóriába sem tartoznak (általános termék).
 * <p>
 * Érvénytelen adat esetén IllegalArgumentException-t dob – a hibát
 * az objektum ismeri fel, a kezelését a hívóra bízza.
 */
public class Termek {

    private final String megnevezes;
    private final String cikkszam;
    private int ar;
    private int darabszam;

    /**
     * @param megnevezes a termék neve (nem lehet üres)
     * @param cikkszam   a cikkszám (nem lehet üres)
     * @param ar         az egységár forintban (pozitív)
     * @param darabszam  a raktáron lévő mennyiség (nem negatív)
     * @throws IllegalArgumentException ha bármelyik adat érvénytelen
     */
    public Termek(String megnevezes, String cikkszam, int ar, int darabszam) {
        // 1. Szöveges adatok ellenőrzése.
        if (megnevezes == null || megnevezes.isBlank()) {
            throw new IllegalArgumentException("A megnevezés nem lehet üres!");
        }
        if (cikkszam == null || cikkszam.isBlank()) {
            throw new IllegalArgumentException("A cikkszám nem lehet üres!");
        }
        this.megnevezes = megnevezes;
        this.cikkszam = cikkszam;
        // 2. A számértékeket a setterek ellenőrzik, így a szabály egy helyen van.
        setAr(ar);
        setDarabszam(darabszam);
    }

    /** @return a megnevezés */
    public String getMegnevezes() {
        return megnevezes;
    }

    /** @return a cikkszám */
    public String getCikkszam() {
        return cikkszam;
    }

    /** @return az egységár forintban */
    public int getAr() {
        return ar;
    }

    /** @return a raktáron lévő darabszám */
    public int getDarabszam() {
        return darabszam;
    }

    /**
     * @param ar az új egységár forintban
     * @throws IllegalArgumentException ha az ár nem pozitív
     */
    public final void setAr(int ar) {
        if (ar <= 0) {
            throw new IllegalArgumentException("Az árnak pozitívnak kell lennie (kapott: " + ar + ")!");
        }
        this.ar = ar;
    }

    /**
     * @param darabszam az új darabszám
     * @throws IllegalArgumentException ha a darabszám negatív
     */
    public final void setDarabszam(int darabszam) {
        if (darabszam < 0) {
            throw new IllegalArgumentException("A darabszám nem lehet negatív (kapott: " + darabszam + ")!");
        }
        this.darabszam = darabszam;
    }

    /**
     * Kiadás a raktárból (eladás).
     *
     * @param mennyiseg a kiadott mennyiség
     * @throws IllegalArgumentException ha a mennyiség nem pozitív, vagy több, mint a készlet
     */
    public void kiad(int mennyiseg) {
        if (mennyiseg <= 0) {
            throw new IllegalArgumentException("A kiadott mennyiségnek pozitívnak kell lennie!");
        }
        if (mennyiseg > darabszam) {
            throw new IllegalArgumentException("Nincs elég készlet: " + megnevezes
                    + " (kért: " + mennyiseg + ", raktáron: " + darabszam + ")");
        }
        darabszam -= mennyiseg;
    }

    /** @return a készlet értéke (ár × darabszám) */
    public long keszletErtek() {
        return (long) ar * darabszam;
    }

    /**
     * A termék kategóriájának neve – a leszármazottak felülírják.
     *
     * @return "Általános"
     */
    public String kategoria() {
        return "Általános";
    }

    /**
     * Kiegészítő információ a termékről – a leszármazottak felülírják.
     *
     * @return üres szöveg az általános terméknél
     */
    protected String extraInfo() {
        return "";
    }

    /** @return a termék adatai egy sorban */
    @Override
    public String toString() {
        // Az extraInfo() polimorf hívás: a tényleges osztály változata fut le.
        return String.format("%-10s %-8s %-22s %6d Ft %5d db %s",
                kategoria(), cikkszam, megnevezes, ar, darabszam, extraInfo());
    }
}
