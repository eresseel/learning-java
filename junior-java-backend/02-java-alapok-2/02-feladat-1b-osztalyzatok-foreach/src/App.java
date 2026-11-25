import java.util.Scanner;

/**
 * Bekéri egy tanuló hat tantárgyának év végi osztályzatát, tömbben tárolja,
 * egymás mellé kilistázza őket, majd kiírja a tanuló átlagát.
 * <p>
 * Ebben a változatban – ahol lehet – bővített {@code for} ciklust (for-each)
 * használunk. A feltöltéshez viszont továbbra is index kell, mert a for-each
 * ciklusváltozója csak az elem MÁSOLATA, rajta keresztül nem írhatunk a tömbbe.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        final int TANTARGYAK_SZAMA = 6;
        int[] osztalyzatok = new int[TANTARGYAK_SZAMA];

        // 1. Feltöltés: itt NEM használhatunk for-each-et, mert az elemekbe
        //    írni kell, ehhez pedig az index kell.
        for (int i = 0; i < osztalyzatok.length; i++) {
            osztalyzatok[i] = osztalyzatBekerese(sc, i + 1);
        }

        // 2. Listázás for-each ciklussal: "minden jegy-re az osztalyzatok tömbben".
        //    Nincs index, nincs határfeltétel – nem lehet elrontani a határokat.
        System.out.print("Osztályzatok: ");
        for (int jegy : osztalyzatok) {
            System.out.print(jegy + " ");
        }
        System.out.println();

        // 3. Összegzés szintén for-each ciklussal, csak olvassuk az elemeket.
        int osszeg = 0;
        for (int jegy : osztalyzatok) {
            osszeg += jegy;
        }
        double atlag = (double) osszeg / osztalyzatok.length;
        System.out.printf("Átlag: %.2f%n", atlag);

        sc.close();
    }

    /**
     * Bekér egy 1 és 5 közötti osztályzatot; hibás értéknél újra kérdez.
     *
     * @param sc      a bemenetet olvasó Scanner
     * @param sorszam a tantárgy sorszáma (csak a kiíráshoz kell)
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
                    return jegy;
                }
            } else {
                sc.next(); // a nem szám bemenetet eldobjuk
            }
            System.out.println("Hibás érték, 1 és 5 közötti egész számot adjon meg!");
        }
    }
}
