import java.util.Scanner;

/**
 * Önálló (házi) feladat I.: feltölt egy 10 elemű tömböt a felhasználótól bekért
 * egész számokkal, majd különféle lekérdezéseket végez rajta (A–G részfeladatok),
 * végül az értékeket egy szintén 10 elemű, kétdimenziós (2x5-ös) tömbbe másolja.
 */
public class App {

    /**
     * A program belépési pontja. A részfeladatokat a kommentek betűjele jelöli.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] szamok = new int[10];

        // 0. Feltöltés a felhasználótól bekért egész számokkal.
        for (int i = 0; i < szamok.length; i++) {
            szamok[i] = egeszBekerese(sc, (i + 1) + ". szám: ");
        }

        // A. Az adatok egymás mellé, szóközzel elválasztva.
        System.out.println("\nA) Az elemek:");
        for (int szam : szamok) {
            System.out.print(szam + " ");
        }
        System.out.println();

        // B. Az első elem négyzete. long-ba számolunk, mert egy nagy int
        //    négyzete már nem férne bele az int értéktartományába.
        long negyzet = (long) szamok[0] * szamok[0];
        System.out.println("\nB) Az első elem (" + szamok[0] + ") négyzete: " + negyzet);

        // C. Páratlan számok és indexeik, soronként egy pár.
        //    Páratlan: a 2-vel vett maradék nem 0 (negatív számnál -1 is lehet).
        System.out.println("\nC) Páratlan számok és indexeik:");
        for (int i = 0; i < szamok.length; i++) {
            if (szamok[i] % 2 != 0) {
                System.out.println("  szám: " + szamok[i] + ", index: " + i);
            }
        }

        // D. Hárommal osztható számok az utolsó elemtől az első felé haladva.
        System.out.println("\nD) Hárommal osztható számok visszafelé:");
        for (int i = szamok.length - 1; i >= 0; i--) {
            if (szamok[i] % 3 == 0) {
                System.out.print(szamok[i] + " ");
            }
        }
        System.out.println();

        // E. Az első öt és az utolsó öt elem összege külön-külön.
        //    A tömb felét a length / 2 adja, így 10-től eltérő hosszra is működne.
        int fele = szamok.length / 2;
        int elsoOtOsszege = 0;
        int utolsoOtOsszege = 0;
        for (int i = 0; i < szamok.length; i++) {
            if (i < fele) {
                elsoOtOsszege += szamok[i];
            } else {
                utolsoOtOsszege += szamok[i];
            }
        }
        System.out.println("\nE) Az első öt elem összege: " + elsoOtOsszege
                + ", az utolsó öt elem összege: " + utolsoOtOsszege);

        // F. 10 elemű kétdimenziós tömb: 2 sor x 5 oszlop. A k-adik egydimenziós
        //    elem helye: sor = k / oszlopok, oszlop = k % oszlopok (egész osztás és maradék).
        int[][] matrix = new int[2][5];
        int oszlopok = matrix[0].length;
        for (int k = 0; k < szamok.length; k++) {
            matrix[k / oszlopok][k % oszlopok] = szamok[k];
        }
        System.out.println("\nF) A kétdimenziós (2x5-ös) tömb:");
        for (int[] sor : matrix) {
            for (int ertek : sor) {
                System.out.printf("%6d", ertek);
            }
            System.out.println();
        }

        // G. Egymás alá a számok, mindkét tömbbeli indexükkel.
        System.out.println("\nG) Számok és indexeik mindkét tömbben:");
        for (int k = 0; k < szamok.length; k++) {
            int sor = k / oszlopok;
            int oszlop = k % oszlopok;
            System.out.printf("%6d  egydimenziós: [%d]  kétdimenziós: [%d][%d]%n",
                    szamok[k], k, sor, oszlop);
        }

        sc.close();
    }

    /**
     * Bekér egy egész számot; hibás bemenetnél újra kérdez.
     *
     * @param sc      a bemenetet olvasó Scanner
     * @param kerdes  a kiírandó kérdés szövege
     * @return a beolvasott egész szám
     */
    static int egeszBekerese(Scanner sc, String kerdes) {
        while (true) {
            System.out.print(kerdes);
            if (!sc.hasNext()) {
                // A bemenet véget ért (pl. pipe-olt bemenetnél elfogytak a sorok):
                // kivételes stack trace helyett érthető üzenettel állunk le.
                System.out.println("\nA bemenet véget ért, nincs elég szám. A program leáll.");
                System.exit(1);
            }
            if (sc.hasNextInt()) {
                return sc.nextInt();
            }
            System.out.println("Ez nem egész szám: " + sc.next());
        }
    }
}
