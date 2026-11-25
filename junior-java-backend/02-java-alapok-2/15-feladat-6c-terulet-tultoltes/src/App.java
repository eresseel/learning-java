import java.util.Scanner;

/**
 * Feladat VI.C: a kerületszámító program bővítése túltöltött területszámító
 * metódusokkal. Minden alakzatnál a kerületet és a területet is kiírja.
 * <p>
 * A túltöltött metódusok és szignatúráik:
 * <pre>
 *   kerulet(int a)                 négyzet     4 * a
 *   kerulet(int a, int b)          téglalap    2 * (a + b)
 *   kerulet(int a, int b, int c)   háromszög   a + b + c
 *   kerulet(double r)              kör         2 * r * π
 *
 *   terulet(int a)                 négyzet     a^2
 *   terulet(int a, int b)          téglalap    a * b
 *   terulet(double a, double ma)   háromszög   (a * ma) / 2
 *   terulet(double r)              kör         r^2 * π
 * </pre>
 * A háromszög területe és a téglalap területe is kétparaméteres: a fordító
 * csak a paraméterek TÍPUSA (int vagy double) alapján tud különbséget tenni.
 */
public class App {

    /** A bemenetet olvasó Scanner. */
    static final Scanner SC = new Scanner(System.in);

    /**
     * A program belépési pontja: menüből választott alakzat kerületét és területét számolja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        System.out.println("Kerület- és területszámítás – 1: téglalap, 2: négyzet, 3: háromszög, 4: kör");
        int valasztas = egeszBekerese("Választás: ");

        switch (valasztas) {
            case 1 -> {
                int a = egeszBekerese("a oldal (cm): ");
                int b = egeszBekerese("b oldal (cm): ");
                System.out.println("A téglalap kerülete: " + kerulet(a, b) + " cm");
                System.out.println("A téglalap területe: " + terulet(a, b) + " cm²");
            }
            case 2 -> {
                int a = egeszBekerese("a oldal (cm): ");
                System.out.println("A négyzet kerülete: " + kerulet(a) + " cm");
                System.out.println("A négyzet területe: " + terulet(a) + " cm²");
            }
            case 3 -> {
                int a = egeszBekerese("a oldal (cm): ");
                int b = egeszBekerese("b oldal (cm): ");
                int c = egeszBekerese("c oldal (cm): ");
                double ma = valosBekerese("az a oldalhoz tartozó magasság (cm): ");
                System.out.println("A háromszög kerülete: " + kerulet(a, b, c) + " cm");
                // FONTOS: az "a" int, ezért double-lé alakítjuk. Ha két int-tel
                // hívnánk (terulet(a, (int) ma)), a fordító a TÉGLALAP területét
                // számoló terulet(int, int) metódust választaná – rossz eredménnyel!
                // Itt az ma már double, így a fordító int -> double bővítéssel a
                // terulet(double, double) változatot választja; a kasztolás csak
                // a szándékot teszi egyértelművé.
                System.out.printf("A háromszög területe: %.2f cm²%n", terulet((double) a, ma));
            }
            case 4 -> {
                double r = valosBekerese("sugár (cm): ");
                System.out.printf("A kör kerülete: %.2f cm%n", kerulet(r));
                System.out.printf("A kör területe: %.2f cm²%n", terulet(r));
            }
            default -> System.out.println("Nincs ilyen menüpont.");
        }
    }

    // ===================== Kerület (túltöltött metódusok) =====================

    /**
     * Négyzet kerülete.
     *
     * @param a az oldal hossza
     * @return 4 * a
     */
    static int kerulet(int a) {
        return 4 * a;
    }

    /**
     * Téglalap kerülete.
     *
     * @param a az egyik oldal hossza
     * @param b a másik oldal hossza
     * @return 2 * (a + b)
     */
    static int kerulet(int a, int b) {
        return 2 * (a + b);
    }

    /**
     * Háromszög kerülete.
     *
     * @param a az első oldal hossza
     * @param b a második oldal hossza
     * @param c a harmadik oldal hossza
     * @return a + b + c
     */
    static int kerulet(int a, int b, int c) {
        return a + b + c;
    }

    /**
     * Kör kerülete.
     *
     * @param r a kör sugara
     * @return 2 * r * π
     */
    static double kerulet(double r) {
        return 2 * r * Math.PI;
    }

    // ===================== Terület (túltöltött metódusok) =====================

    /**
     * Négyzet területe.
     *
     * @param a az oldal hossza
     * @return a^2
     */
    static int terulet(int a) {
        return a * a;
    }

    /**
     * Téglalap területe.
     *
     * @param a az egyik oldal hossza
     * @param b a másik oldal hossza
     * @return a * b
     */
    static int terulet(int a, int b) {
        return a * b;
    }

    /**
     * Háromszög területe az egyik oldal és a hozzá tartozó magasság alapján.
     *
     * @param a  az oldal hossza
     * @param ma az a oldalhoz tartozó magasság
     * @return (a * ma) / 2
     */
    static double terulet(double a, double ma) {
        return (a * ma) / 2;
    }

    /**
     * Kör területe.
     *
     * @param r a kör sugara
     * @return r^2 * π
     */
    static double terulet(double r) {
        return r * r * Math.PI;
    }

    // ===================== Bekérő segédmetódusok =====================

    /**
     * Bekér egy pozitív egész számot; hibás bemenetnél újra kérdez.
     *
     * @param kerdes a kiírandó kérdés
     * @return a beolvasott pozitív egész
     */
    static int egeszBekerese(String kerdes) {
        while (true) {
            System.out.print(kerdes);
            if (!SC.hasNext()) {
                System.out.println("\nA bemenet véget ért. A program leáll.");
                System.exit(1);
            }
            if (SC.hasNextInt()) {
                int szam = SC.nextInt();
                if (szam > 0) {
                    return szam;
                }
            } else {
                SC.next();
            }
            System.out.println("Pozitív egész számot adjon meg!");
        }
    }

    /**
     * Bekér egy pozitív valós számot; hibás bemenetnél újra kérdez.
     *
     * @param kerdes a kiírandó kérdés
     * @return a beolvasott pozitív valós szám
     */
    static double valosBekerese(String kerdes) {
        while (true) {
            System.out.print(kerdes);
            if (!SC.hasNext()) {
                System.out.println("\nA bemenet véget ért. A program leáll.");
                System.exit(1);
            }
            if (SC.hasNextDouble()) {
                double szam = SC.nextDouble();
                if (szam > 0) {
                    return szam;
                }
            } else {
                SC.next();
            }
            System.out.println("Pozitív számot adjon meg!");
        }
    }
}
