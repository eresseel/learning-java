package hu.learning.junior.autokereskedes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Feladat III.A – az {@code autokereskedes.xml} beolvasása adatszerkezetbe,
 * egy új autó hozzáadása, majd az eredmény kiírása új XML fájlba.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args opcionálisan: [bemeneti xml] [kimeneti xml]
     *             (alapértelmezés: {@code autokereskedes.xml}, {@code autokereskedes_uj.xml})
     */
    public static void main(String[] args) {
        Path be = Path.of(args.length > 0 ? args[0] : "autokereskedes.xml");
        Path ki = Path.of(args.length > 1 ? args[1] : "autokereskedes_uj.xml");
        try {
            // 1. Beolvasás.
            Autokereskedes kereskedes = AutoXml.beolvas(be);
            System.out.println(kereskedes.nev() + " – beolvasott autók:");
            kereskedes.autok().forEach(a -> System.out.println("  " + a));

            // 2. Új autó hozzáadása: az azonosító a legnagyobb meglévő + 1.
            //    A beolvasott listát új ArrayList-be másoljuk, hogy bővíthető legyen.
            List<Auto> autok = new ArrayList<>(kereskedes.autok());
            int ujId = autok.stream().mapToInt(Auto::id).max().orElse(0) + 1;
            Auto uj = new Auto(ujId, "Dacia", "Duster", 2022, 6990000, "zöld");
            autok.add(uj);
            System.out.println("Hozzáadva: " + uj);

            // 3. Kiírás új fájlba.
            AutoXml.kiir(new Autokereskedes(kereskedes.nev(), autok), ki);
            System.out.println(autok.size() + " autó kiírva ide: " + ki.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Hiba: " + e.getMessage());
            System.exit(1);
        }
    }
}
