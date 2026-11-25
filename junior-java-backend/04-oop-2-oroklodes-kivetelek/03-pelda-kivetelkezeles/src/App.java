import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Kivételkezelés bemutatása: try-catch-finally, több catch ág, a kivétel terjedése
 * a hívási láncon, ellenőrzött és futási idejű kivételek, saját kivétel dobása.
 */
public class App {

    /**
     * Belépési pont: egymás után lefuttatja a bemutató részeket.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        tobbCatchAg();
        terjedes();
        ellenorzottKivetel();
        sajatKivetel();
        System.out.println();
        System.out.println("A program a kivételek ellenére a végéig lefutott.");
    }

    /**
     * Futási idejű kivételek elkapása specifikus és általános catch ágakkal, finally blokkal.
     */
    private static void tobbCatchAg() {
        System.out.println("=== 1. try – catch – finally ===");
        String[] bemenetek = {"5", "0", "abc", "9"};
        int[] tomb = {10, 20, 30};
        for (String bemenet : bemenetek) {
            try {
                // Kivételkezelt kód: több helyen is hibázhat.
                int oszto = Integer.parseInt(bemenet);   // NumberFormatException
                int hanyados = 100 / oszto;              // ArithmeticException (0-val osztás)
                int elem = tomb[oszto];                  // ArrayIndexOutOfBoundsException
                System.out.println(bemenet + " -> 100/" + oszto + " = " + hanyados + ", tomb[" + oszto + "] = " + elem);
            } catch (NumberFormatException e) {
                // Specifikus kivétel: csak a hibás számformátumra.
                System.out.println(bemenet + " -> nem szám: " + e.getMessage());
            } catch (ArithmeticException e) {
                System.out.println(bemenet + " -> aritmetikai hiba: " + e.getMessage());
            } catch (Exception e) {
                // Minden más, nem specifikusan kezelt kivétel. A sorrend fontos:
                // az általánosabb típus (Exception) mindig a specifikusabbak után jön!
                System.out.println(bemenet + " -> egyéb hiba: " + e.getClass().getSimpleName() + " – " + e.getMessage());
            } finally {
                // Mindig lefut, akár volt kivétel, akár nem (pl. erőforrás lezárására).
                System.out.println("   (finally: " + bemenet + " feldolgozva)");
            }
        }
    }

    /**
     * A kivétel terjedése: a hívási láncon (main → kulso → belso) visszafelé halad,
     * amíg valaki el nem kapja.
     */
    private static void terjedes() {
        System.out.println();
        System.out.println("=== 2. A kivétel terjedése a hívó felé ===");
        try {
            kulso();
        } catch (IllegalStateException e) {
            System.out.println("A terjedes() kapta el: " + e.getMessage());
        }
    }

    /** Középső szint: nem kezeli a kivételt, így az továbbmegy a hívója felé. */
    private static void kulso() {
        System.out.println("kulso() hívja a belso()-t");
        belso();
        System.out.println("ez a sor nem fut le, mert a belso() kivételt dobott");
    }

    /** Legbelső szint: itt keletkezik a kivétel. */
    private static void belso() {
        throw new IllegalStateException("hiba a belso() metódusban");
    }

    /**
     * Ellenőrzött kivétel: a fájlművelet IOException-t dobhat, ezt a fordító
     * nem engedi kezeletlenül hagyni.
     */
    private static void ellenorzottKivetel() {
        System.out.println();
        System.out.println("=== 3. Ellenőrzött kivétel (I/O) ===");
        try {
            String tartalom = Files.readString(Path.of("nem-letezo-fajl.txt"));
            System.out.println(tartalom);
        } catch (IOException e) {
            // A NoSuchFileException az IOException leszármazottja – a polimorfizmus miatt
            // az ős típussal is elkapható.
            System.out.println("Nem sikerült a fájl olvasása: " + e.getClass().getSimpleName() + " – " + e.getMessage());
        }
    }

    /**
     * Saját kivétel dobása és elkapása az objektum adatellenőrzésénél.
     */
    private static void sajatKivetel() {
        System.out.println();
        System.out.println("=== 4. Saját kivétel (throw new ...) ===");
        Object[][] adatok = {{"Kovács Ádám", 34}, {"Szabó Éva", -5}, {"", 20}, {"Tóth Ilona", 131}};
        for (Object[] sor : adatok) {
            try {
                Szemely sz = new Szemely((String) sor[0], (Integer) sor[1]);
                System.out.println("Létrejött: " + sz);
            } catch (ErvenytelenEletkorException e) {
                // A saját kivétel extra adatot is hordozhat (getEletkor()).
                System.out.println("Elutasítva: " + e.getMessage() + " [érték: " + e.getEletkor() + "]");
            } catch (IllegalArgumentException e) {
                System.out.println("Elutasítva: " + e.getMessage());
            }
        }
    }
}
