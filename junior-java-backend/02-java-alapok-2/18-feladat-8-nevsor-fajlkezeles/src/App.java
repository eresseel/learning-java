import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Feladat VIII.: egy osztály névsorának kezelése menüvel.
 * <p>
 * Menüpontok: 1. adatok bekérése konzolról, 2. adatok beolvasása fájlból,
 * 3. névsor kilistázása, 4. adatok kiírása fájlba, 5. kilépés. A menü minden
 * művelet után újra megjelenik, amíg a felhasználó a kilépést nem választja.
 * <p>
 * A neveket {@link ArrayList}-ben tároljuk: a tömbbel ellentétben ennek a
 * mérete futás közben nőhet, ami itt kell, hiszen előre nem tudjuk, hány név lesz.
 */
public class App {

    /** A beolvasandó mintafájl neve (a feladat mappájából futtatva). */
    static final String BEMENETI_FAJL = "nevsor.txt";

    /** A mentés célfájlja – külön fájl, hogy a mintaadat ne íródjon felül. */
    static final String KIMENETI_FAJL = "nevsor-mentes.txt";

    /** A bemenetet olvasó Scanner. */
    static final Scanner SC = new Scanner(System.in);

    /**
     * A program belépési pontja: a menü főciklusa.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        List<String> nevsor = new ArrayList<>();
        int valasztas;

        // Hátultesztelős ciklus: a menü legalább egyszer megjelenik, és addig
        // ismétlődik, amíg a választás nem 5 (kilépés).
        do {
            menuKiirasa();
            valasztas = menupontBekerese();
            switch (valasztas) {
                case 1 -> bekeresKonzolrol(nevsor);
                case 2 -> beolvasasFajlbol(nevsor, BEMENETI_FAJL);
                case 3 -> listazas(nevsor);
                case 4 -> kiirasFajlba(nevsor, KIMENETI_FAJL);
                case 5 -> System.out.println("Viszontlátásra!");
                default -> System.out.println("Nincs ilyen menüpont, 1 és 5 között válasszon.");
            }
        } while (valasztas != 5);

        SC.close();
    }

    /** Kiírja a menüpontokat. */
    static void menuKiirasa() {
        System.out.println();
        System.out.println("=== Osztálynévsor ===");
        System.out.println("1. Adatok bekérése konzolról");
        System.out.println("2. Adatok beolvasása fájlból");
        System.out.println("3. Névsor kilistázása");
        System.out.println("4. Adatok kiírása fájlba");
        System.out.println("5. Kilépés");
    }

    /**
     * Bekéri a választott menüpontot egy teljes sorként.
     * Ha a bemenet véget ért, kilépésnek (5) tekinti.
     *
     * @return a menüpont száma, vagy 0, ha nem szám érkezett
     */
    static int menupontBekerese() {
        System.out.print("Választás: ");
        if (!SC.hasNextLine()) {
            System.out.println();
            return 5; // EOF: a program szabályosan kilép
        }
        String sor = SC.nextLine().trim();
        try {
            return Integer.parseInt(sor);
        } catch (NumberFormatException e) {
            return 0; // a default ág kezeli
        }
    }

    /**
     * Neveket kér be a konzolról üres sorig, és hozzáfűzi őket a névsorhoz.
     *
     * @param nevsor a bővítendő névsor
     */
    static void bekeresKonzolrol(List<String> nevsor) {
        System.out.println("Adja meg a neveket soronként, a befejezéshez üres sort üssön:");
        int db = 0;
        while (SC.hasNextLine()) {
            String nev = SC.nextLine().trim();
            if (nev.isEmpty()) {
                break; // üres sor: vége a bekérésnek
            }
            nevsor.add(nev);
            db++;
        }
        System.out.println(db + " név felvéve, a névsorban összesen " + nevsor.size() + " név van.");
    }

    /**
     * Beolvassa a megadott fájl sorait (soronként egy név), és hozzáfűzi a névsorhoz.
     * <p>
     * A fájlkezelés lépései: adatfolyam megnyitása → olvasás, amíg van újabb sor →
     * adatfolyam bezárása. A try-with-resources szerkezet (a try utáni zárójel)
     * a blokk végén akkor is automatikusan bezárja a fájlt, ha hiba történt.
     *
     * @param nevsor   a bővítendő névsor
     * @param fajlnev  a beolvasandó fájl neve
     */
    static void beolvasasFajlbol(List<String> nevsor, String fajlnev) {
        int db = 0;
        try (BufferedReader olvaso = new BufferedReader(new FileReader(fajlnev, StandardCharsets.UTF_8))) {
            String sor;
            // A readLine() null-t ad vissza, ha elértük a fájl végét.
            while ((sor = olvaso.readLine()) != null) {
                String nev = sor.trim();
                if (!nev.isEmpty()) { // az üres sorokat kihagyjuk
                    nevsor.add(nev);
                    db++;
                }
            }
            System.out.println(db + " név beolvasva a(z) " + fajlnev + " fájlból.");
        } catch (IOException e) {
            // Pl. ha a fájl nem létezik: nem omlik össze a program, csak jelez.
            System.out.println("Hiba a(z) " + fajlnev + " olvasásakor: " + e.getMessage());
        }
    }

    /**
     * Ábécérendben, sorszámozva kilistázza a névsort. Az eredeti listát nem
     * módosítja, egy rendezett másolatot ír ki.
     *
     * @param nevsor a kiírandó névsor
     */
    static void listazas(List<String> nevsor) {
        if (nevsor.isEmpty()) {
            System.out.println("A névsor üres.");
            return;
        }
        List<String> rendezett = new ArrayList<>(nevsor);
        // Magyar ábécérend (ékezetes betűk helyesen) – lásd a VII.B feladatot.
        rendezett.sort(Collator.getInstance(Locale.of("hu", "HU")));
        System.out.println("Névsor (" + rendezett.size() + " fő):");
        for (int i = 0; i < rendezett.size(); i++) {
            System.out.printf("%3d. %s%n", i + 1, rendezett.get(i));
        }
    }

    /**
     * Kiírja a névsort a megadott fájlba (soronként egy név), felülírva annak
     * korábbi tartalmát.
     *
     * @param nevsor  a mentendő névsor
     * @param fajlnev a célfájl neve
     */
    static void kiirasFajlba(List<String> nevsor, String fajlnev) {
        try (BufferedWriter iro = new BufferedWriter(new FileWriter(fajlnev, StandardCharsets.UTF_8))) {
            for (String nev : nevsor) {
                iro.write(nev);
                iro.newLine();
            }
            // A bezárás (itt automatikus) üríti a puffert – enélkül adat veszhetne el.
            System.out.println(nevsor.size() + " név kiírva a(z) " + fajlnev + " fájlba.");
        } catch (IOException e) {
            System.out.println("Hiba a(z) " + fajlnev + " írásakor: " + e.getMessage());
        }
    }
}
