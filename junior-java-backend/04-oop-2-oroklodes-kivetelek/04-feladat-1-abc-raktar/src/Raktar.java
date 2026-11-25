/**
 * Az ABC raktára: tetszőleges típusú termékeket tárol egy Termek típusú tömbben.
 * <p>
 * A polimorfizmus miatt a Termek tömbben Tejtermek és Pekaru objektum is lehet.
 */
public class Raktar {

    private final Termek[] termekek;
    private int darab;

    /**
     * @param kapacitas hány féle terméket tud tárolni a raktár
     */
    public Raktar(int kapacitas) {
        termekek = new Termek[kapacitas];
    }

    /**
     * Új termék felvétele.
     *
     * @param t a felveendő termék
     * @throws IllegalStateException ha a raktár megtelt
     * @throws IllegalArgumentException ha már van ilyen cikkszámú termék
     */
    public void felvesz(Termek t) {
        if (darab == termekek.length) {
            throw new IllegalStateException("A raktár megtelt, nem vehető fel: " + t.getMegnevezes());
        }
        if (keres(t.getCikkszam()) != null) {
            throw new IllegalArgumentException("Már létezik ilyen cikkszám: " + t.getCikkszam());
        }
        termekek[darab++] = t;
    }

    /**
     * @param cikkszam a keresett cikkszám
     * @return a termék, vagy null, ha nincs ilyen
     */
    public Termek keres(String cikkszam) {
        for (int i = 0; i < darab; i++) {
            if (termekek[i].getCikkszam().equals(cikkszam)) {
                return termekek[i];
            }
        }
        return null;
    }

    /** Kiírja a raktárkészletet. */
    public void listaz() {
        for (int i = 0; i < darab; i++) {
            System.out.println(termekek[i]);
        }
    }

    /** @return a teljes raktárkészlet értéke forintban */
    public long osszErtek() {
        long osszeg = 0;
        for (int i = 0; i < darab; i++) {
            osszeg += termekek[i].keszletErtek();
        }
        return osszeg;
    }
}
