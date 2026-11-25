import java.util.Scanner;

/**
 * Bekéri egy tanuló hat tantárgyának év végi osztályzatát, tömbben tárolja,
 * egymás mellé kilistázza őket, majd kiírja a tanuló átlagát.
 * <p>
 * Ebben a változatban minden ciklus hagyományos, indexes {@code for} ciklus.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. A tantárgyak számát konstansban tároljuk – így egy helyen módosítható.
        final int TANTARGYAK_SZAMA = 6;
        int[] osztalyzatok = new int[TANTARGYAK_SZAMA];

        // 2. Feltöltés: az i-edik elembe az i+1-edik tantárgy jegye kerül.
        //    A ciklus határa a tömb length adattagja, nem egy "beégetett" 6-os.
        for (int i = 0; i < osztalyzatok.length; i++) {
            osztalyzatok[i] = osztalyzatBekerese(sc, i + 1);
        }

        // 3. Listázás egymás mellé, szóközzel elválasztva.
        System.out.print("Osztályzatok: ");
        for (int i = 0; i < osztalyzatok.length; i++) {
            System.out.print(osztalyzatok[i] + " ");
        }
        System.out.println();

        // 4. Átlag: összegzés, majd osztás. Az összeg int, ezért osztás előtt
        //    double-lé alakítjuk, különben egész osztás lenne (pl. 25 / 6 = 4).
        int osszeg = 0;
        for (int i = 0; i < osztalyzatok.length; i++) {
            osszeg += osztalyzatok[i];
        }
        double atlag = (double) osszeg / osztalyzatok.length;
        System.out.printf("Átlag: %.2f%n", atlag);

        sc.close();
    }

    /**
     * Bekér egy 1 és 5 közötti osztályzatot; hibás értéknél újra kérdez.
     *
     * @param sc        a bemenetet olvasó Scanner
     * @param sorszam   a tantárgy sorszáma (csak a kiíráshoz kell)
     * @return a beolvasott, érvényes osztályzat
     */
    static int osztalyzatBekerese(Scanner sc, int sorszam) {
        while (true) {
            System.out.print(sorszam + ". tantárgy osztályzata (1-5): ");
            if (!sc.hasNext()) {
                // A bemenet véget ért (pl. pipe-olt bemenetnél elfogytak a sorok):
                // kivételes stack trace helyett érthető üzenettel állunk le.
                System.out.println("\nA bemenet véget ért, nincs elég osztályzat. A program leáll.");
                System.exit(1);
            }
            if (sc.hasNextInt()) {
                int jegy = sc.nextInt();
                if (jegy >= 1 && jegy <= 5) {
                    return jegy; // érvényes jegy: kilépünk a metódusból
                }
            } else {
                sc.next(); // a nem szám bemenetet eldobjuk
            }
            System.out.println("Hibás érték, 1 és 5 közötti egész számot adjon meg!");
        }
    }
}
