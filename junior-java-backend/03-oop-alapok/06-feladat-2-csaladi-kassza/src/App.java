import java.util.Scanner;

/**
 * 3. lecke, Feladat II: családi kassza menüvezérelt kezelése.
 */
public class App {

    private static final Scanner SC = new Scanner(System.in);

    /**
     * Belépési pont: menü megjelenítése, amíg a felhasználó a Kilépést nem választja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. A kassza objektum létrehozása – a kezdő egyenleget a konstruktor állítja be.
        Kassa kassa = new Kassa();
        int valasztas;
        do {
            System.out.println();
            System.out.println("=== Családi kassza ===");
            System.out.println("1. Bevétel rögzítése");
            System.out.println("2. Kiadás könyvelése");
            System.out.println("3. Egyenleg lekérdezése");
            System.out.println("4. Kilépés");
            valasztas = egeszBekeres("Választás: ");

            // 2. A kiválasztott feladatot a Kassa objektum végzi el, a menü csak közvetít.
            switch (valasztas) {
                case 1 -> {
                    int osszeg = egeszBekeres("Bevétel összege (Ft): ");
                    if (kassa.bevetel(osszeg)) {
                        System.out.println("Bevétel rögzítve.");
                    } else {
                        System.out.println("Hiba: az összegnek pozitívnak kell lennie.");
                    }
                }
                case 2 -> {
                    int osszeg = egeszBekeres("Kiadás összege (Ft): ");
                    if (kassa.kiadas(osszeg)) {
                        System.out.println("Kiadás könyvelve.");
                    } else {
                        System.out.println("Hiba: érvénytelen összeg vagy nincs rá fedezet.");
                    }
                }
                case 3 -> System.out.printf("Aktuális egyenleg: %,d Ft%n", kassa.getEgyenleg());
                case 4 -> System.out.println("Viszlát!");
                default -> System.out.println("Nincs ilyen menüpont.");
            }
        } while (valasztas != 4);
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

    /**
     * Egész számot kér be, amíg érvényes értéket nem kap.
     *
     * @param uzenet a kiírandó kérdés
     * @return a beolvasott egész szám
     */
    private static int egeszBekeres(String uzenet) {
        while (true) {
            try {
                return Integer.parseInt(sorBekeres(uzenet));
            } catch (NumberFormatException e) {
                System.out.println("Kérem, egész számot adjon meg!");
            }
        }
    }
}
