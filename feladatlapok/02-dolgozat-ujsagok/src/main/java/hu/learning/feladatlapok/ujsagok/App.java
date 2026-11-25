package hu.learning.feladatlapok.ujsagok;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * A Dolgozat II. belépési pontja: beolvassa az ujsagok.csv fájlt, megoldja a
 * IV–VIII. feladatokat, majd felajánlja a gyakorló IX–X. feladatokat
 * (ellenőrzött bekérés és hozzáfűzés a fájlhoz).
 */
public class App {

    /**
     * A program indítása.
     *
     * @param args opcionálisan a CSV fájl útvonala (alapértelmezés: {@code ujsagok.csv})
     * @throws IOException ha a fájl nem olvasható vagy írható
     */
    public static void main(String[] args) throws IOException {
        // 1. Fájl kiválasztása: argumentumból, vagy az alapértelmezett relatív útvonal.
        Path fajl = Path.of(args.length > 0 ? args[0] : "ujsagok.csv");

        // 2. Feladat II.: beolvasás tömbbe.
        Ujsag[] ujsagok = UjsagFajlKezelo.beolvas(fajl);
        System.out.println("Beolvasott sajtótermékek száma: " + ujsagok.length);

        // 3. Feladat IV–VIII.
        hetilapokListazasa(ujsagok);
        surunMegjelenoNapilapok(ujsagok);
        altalanosUjsagokSzama(ujsagok);
        legdragabb(ujsagok);
        elsoNapilap(ujsagok);

        // 4. Gyakorló feladatok (IX–X.): csak ha a felhasználó kéri.
        gyakorloResz(fajl);
    }

    /**
     * Feladat IV.: az összes hetilap kiírása az összes ismert adatával.
     *
     * @param ujsagok a beolvasott tömb
     */
    static void hetilapokListazasa(Ujsag[] ujsagok) {
        System.out.println("\nIV. Hetilapok:");
        for (Ujsag u : ujsagok) {
            // instanceof: csak a Hetilap típusú elemeket írjuk ki;
            // a toString() polimorf, így a hetilap saját adatai is megjelennek.
            if (u instanceof Hetilap) {
                System.out.println("  " + u);
            }
        }
    }

    /**
     * Feladat V.: azon napilapok, amelyek legalább hatszor jelennek meg egy héten.
     *
     * @param ujsagok a beolvasott tömb
     */
    static void surunMegjelenoNapilapok(Ujsag[] ujsagok) {
        System.out.println("\nV. Legalább hetente hatszor megjelenő napilapok:");
        for (Ujsag u : ujsagok) {
            // Mintaillesztéses instanceof: a típusellenőrzés után rögtön Napilap-ként érjük el.
            if (u instanceof Napilap n && n.getHetiMegjelenes() >= 6) {
                System.out.println("  " + n);
            }
        }
    }

    /**
     * Feladat VI.: az általános (se nem napilap, se nem hetilap) újságok darabszáma.
     *
     * @param ujsagok a beolvasott tömb
     */
    static void altalanosUjsagokSzama(Ujsag[] ujsagok) {
        int db = 0;
        for (Ujsag u : ujsagok) {
            // Itt NEM jó az instanceof Ujsag, mert minden napilap és hetilap is Ujsag!
            // A pontos osztályt kell összehasonlítani.
            if (u.getClass() == Ujsag.class) {
                db++;
            }
        }
        System.out.println("\nVI. Általános újságok száma: " + db + " db");
    }

    /**
     * Feladat VII.: a legdrágább sajtótermék megnevezése és áfatartalma
     * (maximumkiválasztás; egyenlő ár esetén az első).
     *
     * @param ujsagok a beolvasott tömb
     */
    static void legdragabb(Ujsag[] ujsagok) {
        if (ujsagok.length == 0) {
            System.out.println("\nVII. Nincs adat.");
            return;
        }
        // Maximumkiválasztás: az első elemből indulunk, és ha drágábbat találunk, lecseréljük.
        Ujsag max = ujsagok[0];
        for (Ujsag u : ujsagok) {
            if (u.getAr() > max.getAr()) {
                max = u;
            }
        }
        System.out.printf("%nVII. A legdrágább sajtótermék: %s, áfatartalma: %.2f Ft%n",
                max.getMegnevezes(), max.afaTartalomSzamit());
    }

    /**
     * Feladat VIII.: a fájlban található első napilap megnevezése és heti költsége.
     *
     * @param ujsagok a beolvasott tömb
     */
    static void elsoNapilap(Ujsag[] ujsagok) {
        // Lineáris keresés: az első találatnál kilépünk a ciklusból (return).
        for (Ujsag u : ujsagok) {
            if (u instanceof Napilap) {
                System.out.println("\nVIII. Az első napilap: " + u.getMegnevezes()
                        + ", heti költsége: " + u.hetiArkalkulacio() + " Ft");
                return;
            }
        }
        System.out.println("\nVIII. Nincs napilap a fájlban.");
    }

    /**
     * Gyakorló feladatok: IX. ellenőrzött bekérés, X. hozzáfűzés a CSV-hez.
     * Üres vagy elfogyott bemenet esetén csendben kilép.
     *
     * @param fajl a CSV fájl, amelyhez hozzáfűzünk
     * @throws IOException ha az írás nem sikerül
     */
    static void gyakorloResz(Path fajl) throws IOException {
        Scanner scanner = new Scanner(System.in);
        UjsagBekero bekero = new UjsagBekero(scanner);
        System.out.println();
        while (true) {
            // 1. Megkérdezzük, akar-e új adatot felvenni (EOF vagy nem „i” → vége).
            String valasz = bekero.sortBeker("IX–X. Szeretne új sajtóterméket felvenni? (i/n): ");
            if (valasz == null || !valasz.equalsIgnoreCase("i")) {
                System.out.println("Viszlát!");
                return;
            }
            // 2. IX.: ellenőrzött bekérés.
            Ujsag uj = bekero.ujsagotBeker();
            if (uj == null) {
                System.out.println("A bemenet elfogyott, a félbehagyott adatot nem mentjük.");
                return;
            }
            // 3. X.: hozzáfűzés a CSV fájlhoz.
            UjsagFajlKezelo.hozzafuz(fajl, uj);
            System.out.println("Hozzáfűzve a(z) " + fajl + " fájlhoz: " + UjsagFajlKezelo.csvSor(uj));
        }
    }
}
