import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;

/**
 * Bemutató program a lecke elméleti diáihoz: tömbök (vektor, mátrix, eltérő
 * hosszú sorok), karakter-, sztring-, matematikai és dátumfüggvények,
 * véletlenszám-generátor, metódusok és metódus-túltöltés.
 * <p>
 * Nem kér be adatot; a véletlenszámos rész minden futáskor mást ír ki.
 */
public class App {

    /**
     * A program belépési pontja: sorban meghívja a témakörönkénti bemutatókat.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        tombok();
        karakterFuggvenyek();
        sztringFuggvenyek();
        matematikaiFuggvenyek();
        datumFuggvenyek();
        veletlenszamok();
        metodusok();
    }

    /**
     * Tömbök deklarálása, inicializálása és definiálása, a dián szereplő összes formában.
     */
    static void tombok() {
        System.out.println("=== Tömbök ===");
        // 1. Külön lépésekben: deklarálás, inicializálás (helyfoglalás), definiálás.
        int[] szamok;              // deklarálás: még nincs tömb, csak egy referencia
        szamok = new int[3];       // inicializálás: 3 elem, mind 0 kezdőértékkel
        szamok[0] = 5;             // definiálás index alapján (az index 0-tól indul!)
        System.out.println("new int[3], majd szamok[0] = 5: " + Arrays.toString(szamok));

        // 2. Egyidejű deklarálás, inicializálás és definiálás – két írásmód.
        int[] masik = new int[]{5, 17, 4};
        int[] rovid = {5, 17, 4};
        System.out.println("{5, 17, 4}: " + Arrays.toString(masik) + ", elemszám (length): " + rovid.length);

        // 3. Kétdimenziós tömb (mátrix): 3 sor, 2 oszlop.
        int[][] szamMatrix = new int[3][2];
        szamMatrix[2][1] = 9;
        System.out.println("int[3][2] mátrix: " + Arrays.deepToString(szamMatrix));

        // 4. Eltérő hosszú sorok ("csipkés" tömb): a sorokat külön hozzuk létre.
        int[][] csipkes = new int[3][];
        csipkes[0] = new int[3];
        csipkes[1] = new int[4];
        csipkes[2] = new int[2];
        for (int i = 0; i < csipkes.length; i++) {
            System.out.println("  " + i + ". sor hossza: " + csipkes[i].length);
        }
        System.out.println();
    }

    /**
     * A Character osztály vizsgáló és átalakító metódusai.
     */
    static void karakterFuggvenyek() {
        System.out.println("=== Karakterfüggvények ===");
        char[] karakterek = {'a', 'Á', '7', ' '};
        for (char c : karakterek) {
            System.out.printf("'%c': betű=%b, kisbetű=%b, nagybetű=%b, számjegy=%b, nagybetűsen='%c'%n",
                    c, Character.isLetter(c), Character.isLowerCase(c), Character.isUpperCase(c),
                    Character.isDigit(c), Character.toUpperCase(c));
        }
        // compare: negatív, ha az első kisebb; 0, ha egyenlők; pozitív, ha nagyobb
        System.out.println("Character.compare('a', 'b') = " + Character.compare('a', 'b'));
        System.out.println();
    }

    /**
     * A String osztály leggyakoribb metódusai.
     */
    static void sztringFuggvenyek() {
        System.out.println("=== Sztringfüggvények ===");
        String s = "  Java alapok II.  ";
        String t = s.trim(); // szóközök levágása az elejéről és a végéről
        System.out.println("trim: '" + t + "', length: " + t.length());
        // A diákon toUpper()/toLower() szerepel, a valódi metódusnevek:
        System.out.println("toUpperCase: " + t.toUpperCase() + ", toLowerCase: " + t.toLowerCase());
        System.out.println("substring(0, 4): " + t.substring(0, 4)); // a 4. index már nem része
        System.out.println("replace: " + t.replace("II.", "2"));
        String[] darabok = t.split(" "); // feldarabolás szóközöknél -> String[]
        System.out.println("split: " + Arrays.toString(darabok));
        System.out.println("contains(\"alap\"): " + t.contains("alap")
                + ", startsWith(\"Java\"): " + t.startsWith("Java")
                + ", endsWith(\".\"): " + t.endsWith("."));
        System.out.println("indexOf(\"a\"): " + t.indexOf("a") + ", lastIndexOf(\"a\"): " + t.lastIndexOf("a"));
        // Szövegek összehasonlítása MINDIG equals-szal, nem == operátorral!
        System.out.println("equals: " + t.equals("Java alapok II."));
        System.out.println();
    }

    /**
     * A Math osztály metódusai és konstansai.
     */
    static void matematikaiFuggvenyek() {
        System.out.println("=== Matematikai függvények ===");
        System.out.println("abs(-5) = " + Math.abs(-5) + ", min(3, 8) = " + Math.min(3, 8)
                + ", max(3, 8) = " + Math.max(3, 8));
        System.out.println("sqrt(16) = " + Math.sqrt(16) + ", pow(2, 10) = " + Math.pow(2, 10));
        System.out.println("round(2.5) = " + Math.round(2.5) + ", floor(2.7) = " + Math.floor(2.7)
                + ", ceil(2.1) = " + Math.ceil(2.1));
        System.out.println("PI = " + Math.PI + ", E = " + Math.E);
        System.out.println();
    }

    /**
     * Dátum és idő a java.time csomag osztályaival (a modern, ajánlott megoldás).
     */
    static void datumFuggvenyek() {
        System.out.println("=== Dátumfüggvények ===");
        LocalDate ma = LocalDate.now();
        System.out.println("Mai dátum: " + ma + ", egy hét múlva: " + ma.plusDays(7));
        System.out.println("Most (idő): " + LocalTime.now().withNano(0));
        System.out.println("Most (dátum és idő): " + LocalDateTime.now().withNano(0));
        System.out.println("Szökőév-e idén: " + ma.isLeapYear());
        System.out.println();
    }

    /**
     * A Random osztály használata.
     */
    static void veletlenszamok() {
        System.out.println("=== Véletlenszámok ===");
        Random rnd = new Random();
        // nextInt(max): 0 és max-1 között (a felső határ NEM része)
        System.out.println("nextInt(10): " + rnd.nextInt(10));
        // nextInt(min, max) Java 17 óta: min és max-1 között
        System.out.println("nextInt(1, 51): " + rnd.nextInt(1, 51));
        // nextDouble(): 0.0 (benne) és 1.0 (nincs benne) közötti valós szám
        System.out.printf("nextDouble(): %.4f%n", rnd.nextDouble());
        System.out.println();
    }

    /**
     * Visszatérési érték nélküli és visszatérési értékkel rendelkező metódusok
     * hívása, valamint a túltöltött udvozlo metódusok.
     */
    static void metodusok() {
        System.out.println("=== Metódusok ===");
        osszeadasKiir(2, 3);                         // hívás paraméterekkel
        int osszeg = osszeadas(2, 3);                // visszatérési érték változóba
        System.out.println("osszeadas(2, 3) = " + osszeg);
        System.out.println("osszeadas(osszeadas(1, 2), 3) = " + osszeadas(osszeadas(1, 2), 3));
        udvozlo();                                   // túltöltés: paraméter nélküli változat
        udvozlo("Anna");                             // túltöltés: String paraméteres változat
    }

    /**
     * Kiírja két egész szám összegét (visszatérési érték nélküli metódus).
     *
     * @param a az első összeadandó
     * @param b a második összeadandó
     */
    static void osszeadasKiir(int a, int b) {
        System.out.println("osszeadasKiir(" + a + ", " + b + "): " + (a + b));
    }

    /**
     * Visszaadja két egész szám összegét (visszatérési értékkel rendelkező metódus).
     *
     * @param a az első összeadandó
     * @param b a második összeadandó
     * @return a két szám összege
     */
    static int osszeadas(int a, int b) {
        return a + b;
    }

    /** Általános üdvözlés (paraméter nélküli túltöltött változat). */
    static void udvozlo() {
        System.out.println("Üdvözlöm!");
    }

    /**
     * Névre szóló üdvözlés (a túltöltött változat: azonos név, eltérő paraméterlista).
     *
     * @param nev az üdvözlendő személy neve
     */
    static void udvozlo(String nev) {
        System.out.println("Üdvözlöm! " + nev);
    }
}
