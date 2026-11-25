import java.util.Scanner;

/**
 * 5. lecke, házi feladat: zöldséges árlista kezelése menüből (Map).
 */
public class App {

    private static final Scanner SC = new Scanner(System.in);

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Arlista arlista = new Arlista();
        String valasztas;
        do {
            System.out.println();
            System.out.println("=== Zöldséges ===");
            System.out.println("1. Új termék/ár felvitel");
            System.out.println("2. Termék törlése");
            System.out.println("3. Árváltozás");
            System.out.println("4. Terméklista");
            System.out.println("5. Vásárlás");
            System.out.println("6. Kilépés");
            valasztas = sorBekeres("Választás: ");
            switch (valasztas) {
                case "1" -> {
                    String termek = sorBekeres("Termék neve: ");
                    int ar = arBekeres("Ár (Ft/kg): ");
                    System.out.println(arlista.felvesz(termek, ar) ? "Felvéve." : "Ilyen termék már van – használja az árváltozás menüpontot.");
                }
                case "2" -> System.out.println(arlista.torol(sorBekeres("Törlendő termék: ")) ? "Törölve." : "Nincs ilyen termék.");
                case "3" -> {
                    String termek = sorBekeres("Termék neve: ");
                    // Előbb ellenőrizzük a létezést, hogy ne kérjünk be fölöslegesen árat.
                    if (arlista.ar(termek) == null) {
                        System.out.println("Nincs ilyen termék.");
                    } else {
                        arlista.arValtozas(termek, arBekeres("Új ár (Ft/kg): "));
                        System.out.println("Ár módosítva.");
                    }
                }
                case "4" -> arlista.listaz();
                case "5" -> vasarlas(arlista);
                case "6" -> System.out.println("Viszlát!");
                default -> System.out.println("Nincs ilyen menüpont.");
            }
        } while (!valasztas.equals("6"));
    }

    /**
     * Vásárlás: termék és mennyiség bekérése, majd árkalkuláció.
     *
     * @param arlista az árlista
     */
    private static void vasarlas(Arlista arlista) {
        String termek = sorBekeres("Termék neve: ");
        Integer kiloAr = arlista.ar(termek);
        if (kiloAr == null) {
            System.out.println("Nincs ilyen termék.");
            return;
        }
        try {
            double kg = Double.parseDouble(sorBekeres("Mennyiség (kg): ").replace(',', '.'));
            if (kg <= 0) {
                System.out.println("A mennyiségnek pozitívnak kell lennie.");
                return;
            }
            // Az unboxing miatt az Integer kiloAr gond nélkül szorozható a double-lel.
            long fizetendo = Math.round(kiloAr * kg);
            System.out.printf("%s: %.2f kg × %d Ft/kg = %d Ft%n", termek, kg, kiloAr, fizetendo);
        } catch (NumberFormatException e) {
            System.out.println("Hibás mennyiség.");
        }
    }

    /**
     * Pozitív egész árat kér be, amíg érvényes értéket nem kap.
     *
     * @param uzenet a kiírandó kérdés
     * @return az ár
     */
    private static int arBekeres(String uzenet) {
        while (true) {
            try {
                int ar = Integer.parseInt(sorBekeres(uzenet));
                if (ar > 0) {
                    return ar;
                }
                System.out.println("Az árnak pozitívnak kell lennie!");
            } catch (NumberFormatException e) {
                System.out.println("Kérem, egész számot adjon meg!");
            }
        }
    }

    /**
     * Beolvas egy sort; ha elfogyott a bemenet, kilép a programból.
     *
     * @param uzenet a kiírandó kérdés
     * @return a beolvasott sor
     */
    private static String sorBekeres(String uzenet) {
        System.out.print(uzenet);
        if (!SC.hasNextLine()) {
            System.out.println();
            System.out.println("Nincs több bemenet, a program kilép.");
            System.exit(0);
        }
        return SC.nextLine().trim();
    }
}
