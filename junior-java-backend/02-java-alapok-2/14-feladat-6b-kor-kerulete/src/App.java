import java.util.Scanner;

/**
 * Feladat VI.B: az előző program (téglalap, négyzet, háromszög kerülete
 * túltöltéssel) bővítése a kör kerületének kiszámításával.
 * <p>
 * Probléma: a kör kerülete is egyparaméteres (sugár), mint a négyzeté. Két
 * {@code kerulet(int)} metódus nem létezhet, mert a szignatúrájuk azonos lenne
 * (a paraméter NEVE nem számít, csak a típusa és a sorrendje). Ezért a kör
 * sugara {@code double} típusú: a {@code kerulet(double r)} már más szignatúra.
 * <ul>
 *   <li>{@code kerulet(int a)} – négyzet: 4 * a</li>
 *   <li>{@code kerulet(int a, int b)} – téglalap: 2 * (a + b)</li>
 *   <li>{@code kerulet(int a, int b, int c)} – háromszög: a + b + c</li>
 *   <li>{@code kerulet(double r)} – kör: 2 * r * π (ÚJ)</li>
 * </ul>
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
        System.out.println("Kerületszámítás – 1: téglalap, 2: négyzet, 3: háromszög, 4: kör");
        int valasztas = egeszBekerese("Választás: ");

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
            case 4 -> {
                // A sugár double, így a fordító a kerulet(double) változatot hívja.
                double r = valosBekerese("sugár (cm): ");
                System.out.printf("A kör kerülete: %.2f cm%n", kerulet(r));
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
     * Kör kerülete. A négyzetes {@code kerulet(int)}-től a paraméter TÍPUSA különbözteti meg.
     * Figyelem: {@code kerulet(5)} a négyzetet, {@code kerulet(5.0)} a kört számolja!
     *
     * @param r a kör sugara
     * @return 2 * r * π
     */
    static double kerulet(double r) {
        return 2 * r * Math.PI;
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

    /**
     * Bekér egy pozitív valós számot; hibás bemenetnél újra kérdez.
     * (Túltöltés helyett itt más nevet adtunk, mert a paraméterlista azonos lenne.)
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
