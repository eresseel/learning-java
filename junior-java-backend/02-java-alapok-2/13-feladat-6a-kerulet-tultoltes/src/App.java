import java.util.Scanner;

/**
 * Feladat VI.A: téglalap, négyzet és háromszög kerületének kiszámolása
 * metódus-túltöltéssel (method overloading).
 * <p>
 * Mindhárom metódus neve {@code kerulet}; a fordító a paraméterek SZÁMA alapján
 * dönti el, melyiket kell meghívnia (metódus szignatúra = név + paraméterlista):
 * <ul>
 *   <li>{@code kerulet(int a)} – négyzet: 4 * a</li>
 *   <li>{@code kerulet(int a, int b)} – téglalap: 2 * (a + b)</li>
 *   <li>{@code kerulet(int a, int b, int c)} – háromszög: a + b + c</li>
 * </ul>
 * Az oldalhosszak egész centiméterek.
 */
public class App {

    /** A bemenetet olvasó Scanner. */
    static final Scanner SC = new Scanner(System.in);

    /**
     * A program belépési pontja: menüből választott alakzat kerületét számolja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        System.out.println("Kerületszámítás – 1: téglalap, 2: négyzet, 3: háromszög");
        int valasztas = egeszBekerese("Választás: ");

        // A switch ágaiban ugyanazt a "kerulet" nevet hívjuk, de eltérő számú
        // paraméterrel – a fordító mindig a megfelelő túltöltött változatot választja.
        switch (valasztas) {
            case 1 -> {
                int a = egeszBekerese("a oldal (cm): ");
                int b = egeszBekerese("b oldal (cm): ");
                System.out.println("A téglalap kerülete: " + kerulet(a, b) + " cm");
            }
            case 2 -> {
                int a = egeszBekerese("a oldal (cm): ");
                System.out.println("A négyzet kerülete: " + kerulet(a) + " cm");
            }
            case 3 -> {
                int a = egeszBekerese("a oldal (cm): ");
                int b = egeszBekerese("b oldal (cm): ");
                int c = egeszBekerese("c oldal (cm): ");
                System.out.println("A háromszög kerülete: " + kerulet(a, b, c) + " cm");
            }
            default -> System.out.println("Nincs ilyen menüpont.");
        }
    }

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
}
