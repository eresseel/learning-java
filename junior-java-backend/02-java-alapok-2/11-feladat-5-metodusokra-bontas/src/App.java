import java.util.Random;

/**
 * Feladat V.: a Feladat IV. (két véletlen tömb statisztikái) metódusokra bontva.
 * <p>
 * A {@code main} csak deklarációkat és metódushívásokat tartalmaz; minden
 * metódus egyetlen, a nevéből kiolvasható feladatot végez, így a két tömbre
 * ugyanaz a kód fut le, nincs ismétlődés.
 */
public class App {

    /** Közös véletlenszám-generátor az egész osztálynak (osztályszintű változó). */
    static final Random RND = new Random();

    /** A tömbök legkisebb és legnagyobb elemszáma. */
    static final int MIN_ELEMSZAM = 5;
    static final int MAX_ELEMSZAM = 10;

    /** A tömbelemek legkisebb és legnagyobb értéke. */
    static final int MIN_ERTEK = 1;
    static final int MAX_ERTEK = 50;

    /** Ezzel az értékkel jelezzük, hogy nincs négyzetszám a tömbben. */
    static final int NINCS = -1;

    /**
     * A program belépési pontja: csak deklarációk és metódushívások.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        int[] elsoTomb = veletlenTomb();
        int[] masodikTomb = veletlenTomb();
        statisztikaKiirasa("Első tömb", elsoTomb);
        statisztikaKiirasa("Második tömb", masodikTomb);
    }

    /**
     * Létrehoz egy véletlen elemszámú tömböt, és feltölti véletlenszámokkal.
     *
     * @return a feltöltött tömb
     */
    static int[] veletlenTomb() {
        // A felső határ +1, mert a nextInt(min, max) a max-ot már nem adja vissza.
        int[] tomb = new int[RND.nextInt(MIN_ELEMSZAM, MAX_ELEMSZAM + 1)];
        for (int i = 0; i < tomb.length; i++) {
            tomb[i] = RND.nextInt(MIN_ERTEK, MAX_ERTEK + 1);
        }
        return tomb; // a tömb referenciáját adjuk vissza a hívónak
    }

    /**
     * Kiírja egy tömb összes adatát a feladat által kért sorrendben.
     *
     * @param nev  a tömb megnevezése a fejléchez
     * @param tomb a vizsgált tömb
     */
    static void statisztikaKiirasa(String nev, int[] tomb) {
        System.out.println("=== " + nev + " (" + tomb.length + " elem) ===");
        System.out.println("Elemek: " + elemekSzovegkent(tomb));
        System.out.println("Összeg: " + osszeg(tomb));
        System.out.println("Páros számok darabszáma: " + parosDarab(tomb));
        System.out.println("Legnagyobb elem: " + maximum(tomb));
        int negyzetszam = elsoNegyzetszam(tomb);
        System.out.println("Első négyzetszám: " + (negyzetszam == NINCS ? "nincs a tömbben" : negyzetszam));
    }

    /**
     * Szóközzel elválasztott szöveggé alakítja a tömb elemeit.
     *
     * @param tomb a tömb
     * @return az elemek szövegként
     */
    static String elemekSzovegkent(int[] tomb) {
        String eredmeny = "";
        for (int szam : tomb) {
            eredmeny += szam + " ";
        }
        return eredmeny.trim();
    }

    /**
     * Összegzés tétele.
     *
     * @param tomb a tömb
     * @return az elemek összege
     */
    static int osszeg(int[] tomb) {
        int osszeg = 0;
        for (int szam : tomb) {
            osszeg += szam;
        }
        return osszeg;
    }

    /**
     * Megszámlálás tétele: a páros elemek száma.
     *
     * @param tomb a tömb
     * @return a páros elemek darabszáma
     */
    static int parosDarab(int[] tomb) {
        int darab = 0;
        for (int szam : tomb) {
            if (szam % 2 == 0) {
                darab++;
            }
        }
        return darab;
    }

    /**
     * Maximumkiválasztás tétele.
     *
     * @param tomb a tömb (legalább egy elemű)
     * @return a legnagyobb elem
     */
    static int maximum(int[] tomb) {
        int max = tomb[0];
        for (int szam : tomb) {
            if (szam > max) {
                max = szam;
            }
        }
        return max;
    }

    /**
     * Keresés tétele: az első négyzetszám megkeresése.
     *
     * @param tomb a tömb
     * @return az első négyzetszám, vagy {@link #NINCS}, ha nincs ilyen
     */
    static int elsoNegyzetszam(int[] tomb) {
        for (int szam : tomb) {
            if (negyzetszamE(szam)) {
                return szam; // az első találatnál azonnal visszatérünk
            }
        }
        return NINCS;
    }

    /**
     * Eldönti, hogy egy nemnegatív egész szám négyzetszám-e.
     *
     * @param szam a vizsgált szám
     * @return {@code true}, ha a szám egy egész szám négyzete
     */
    static boolean negyzetszamE(int szam) {
        int gyok = (int) Math.sqrt(szam);
        return gyok * gyok == szam;
    }
}
