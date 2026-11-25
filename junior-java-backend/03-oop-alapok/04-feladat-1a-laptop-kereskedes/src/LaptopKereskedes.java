/**
 * A laptopkereskedés készletét kezelő osztály.
 * <p>
 * Egységbezárás: a készletet tároló tömb és a rajta dolgozó műveletek
 * (felvétel, listázás, keresés, összérték) egy osztályban vannak, és a
 * tömböt kívülről senki nem éri el közvetlenül (adatrejtés).
 * A kollekciókat (ArrayList) majd az 5. lecke mutatja be, ezért itt még tömböt használunk.
 */
public class LaptopKereskedes {

    /** A készlet maximális mérete – osztályszintű állandó. */
    public static final int MAX_KESZLET = 10;

    private final String nev;
    private final Laptop[] keszlet = new Laptop[MAX_KESZLET];
    /** Hány elem foglalt a tömbben (a következő szabad index is egyben). */
    private int darab;

    /**
     * @param nev a kereskedés neve
     */
    public LaptopKereskedes(String nev) {
        this.nev = nev;
    }

    /** @return a kereskedés neve */
    public String getNev() {
        return nev;
    }

    /** @return a készleten lévő laptopok száma */
    public int getDarab() {
        return darab;
    }

    /**
     * Új laptop felvétele a készletbe.
     *
     * @param laptop a felveendő laptop
     * @return true, ha sikerült; false, ha tele van a készlet vagy a cikkszám már létezik
     */
    public boolean felvesz(Laptop laptop) {
        // 1. Betelt a tömb?
        if (darab == MAX_KESZLET) {
            return false;
        }
        // 2. A cikkszámnak egyedinek kell lennie.
        if (keres(laptop.getCikkszam()) != null) {
            return false;
        }
        // 3. Elhelyezés az első szabad helyre, majd a darabszám növelése.
        keszlet[darab] = laptop;
        darab++;
        return true;
    }

    /**
     * Cikkszám szerinti keresés (lineáris keresés).
     *
     * @param cikkszam a keresett cikkszám
     * @return a megtalált laptop, vagy null, ha nincs ilyen
     */
    public Laptop keres(String cikkszam) {
        for (int i = 0; i < darab; i++) {
            if (keszlet[i].getCikkszam().equalsIgnoreCase(cikkszam)) {
                return keszlet[i];
            }
        }
        return null;
    }

    /**
     * Kiírja a teljes készletet a konzolra.
     */
    public void listaz() {
        System.out.println("=== " + nev + " – készlet (" + darab + " db) ===");
        for (int i = 0; i < darab; i++) {
            System.out.println(keszlet[i]);
        }
    }

    /**
     * Összegzés tétele: a teljes készlet értéke.
     *
     * @return az árak összege forintban
     */
    public long osszErtek() {
        long osszeg = 0;
        for (int i = 0; i < darab; i++) {
            osszeg += keszlet[i].getAr();
        }
        return osszeg;
    }

    /**
     * Maximumkiválasztás tétele: a legdrágább laptop.
     *
     * @return a legdrágább laptop, vagy null, ha üres a készlet
     */
    public Laptop legdragabb() {
        if (darab == 0) {
            return null;
        }
        Laptop max = keszlet[0];
        for (int i = 1; i < darab; i++) {
            if (keszlet[i].getAr() > max.getAr()) {
                max = keszlet[i];
            }
        }
        return max;
    }
}
