import java.util.Scanner;

/**
 * 3. lecke, Feladat I.B: kerületszámító program, amelyben a kört
 * a Kor osztály képviseli a logikai paraméter helyett.
 */
public class App {

    /** A konzolos bemenet olvasója, a program egészében egy példány. */
    private static final Scanner SC = new Scanner(System.in);

    /**
     * Belépési pont: menüből választott síkidom kerületét számolja ki,
     * amíg a felhasználó ki nem lép.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        int valasztas;
        do {
            // 1. Menü kiírása és a választás bekérése.
            System.out.println();
            System.out.println("=== Kerületszámítás ===");
            System.out.println("1. Téglalap");
            System.out.println("2. Négyzet");
            System.out.println("3. Háromszög");
            System.out.println("4. Kör");
            System.out.println("0. Kilépés");
            valasztas = egeszBekeres("Választás: ");

            // 2. A választott síkidom adatainak bekérése és a megfelelő túltöltött metódus hívása.
            switch (valasztas) {
                case 1 -> {
                    double a = pozitivBekeres("a oldal: ");
                    double b = pozitivBekeres("b oldal: ");
                    kiir("téglalap", Kerulet.kerulet(a, b));
                }
                case 2 -> kiir("négyzet", Kerulet.kerulet(pozitivBekeres("a oldal: ")));
                case 3 -> {
                    double a = pozitivBekeres("a oldal: ");
                    double b = pozitivBekeres("b oldal: ");
                    double c = pozitivBekeres("c oldal: ");
                    // Háromszög-egyenlőtlenség: bármely két oldal összege nagyobb a harmadiknál.
                    if (a + b > c && a + c > b && b + c > a) {
                        kiir("háromszög", Kerulet.kerulet(a, b, c));
                    } else {
                        System.out.println("Ezekből az oldalakból nem szerkeszthető háromszög.");
                    }
                }
                case 4 -> {
                    // Itt jön elő az osztály haszna: Kor objektumot adunk át, nem double + boolean párost.
                    Kor kor = new Kor(pozitivBekeres("sugár: "));
                    kiir("kör", Kerulet.kerulet(kor));
                }
                case 0 -> System.out.println("Viszlát!");
                default -> System.out.println("Nincs ilyen menüpont.");
            }
        } while (valasztas != 0);
    }

    /**
     * Kiírja az eredményt két tizedesjegyre kerekítve.
     *
     * @param sikidom a síkidom neve
     * @param k       a kiszámolt kerület
     */
    private static void kiir(String sikidom, double k) {
        System.out.printf("A %s kerülete: %.2f%n", sikidom, k);
    }

    /**
     * Beolvas egy sort; ha elfogyott a bemenet (pl. pipe vége), kilép a programból.
     *
     * @param uzenet a kiírandó kérdés
     * @return a beolvasott, szóközöktől megtisztított sor
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
            String sor = sorBekeres(uzenet);
            // A try-catch a 4. lecke témája; itt csak az érvénytelen szám kiszűrésére használjuk.
            try {
                return Integer.parseInt(sor);
            } catch (NumberFormatException e) {
                System.out.println("Kérem, egész számot adjon meg!");
            }
        }
    }

    /**
     * Pozitív valós számot kér be (tizedesvessző és -pont is elfogadott).
     *
     * @param uzenet a kiírandó kérdés
     * @return a beolvasott pozitív szám
     */
    private static double pozitivBekeres(String uzenet) {
        while (true) {
            String sor = sorBekeres(uzenet).replace(',', '.');
            try {
                double ertek = Double.parseDouble(sor);
                if (ertek > 0) {
                    return ertek;
                }
                System.out.println("Az értéknek pozitívnak kell lennie!");
            } catch (NumberFormatException e) {
                System.out.println("Kérem, számot adjon meg!");
            }
        }
    }
}
