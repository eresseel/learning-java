import java.util.Random;
import java.util.Scanner;

/**
 * Feladat VII.A: egy 40 elemű tömb feltöltése 1 és 100 közötti, egymástól
 * különböző véletlenszámokkal; kiírás 5x8-as formában, csökkenő sorrendbe
 * rendezés saját algoritmussal (minimum-/maximumkiválasztásos rendezés),
 * kiírás 4x10-es formában, végül bináris keresés egy bekért számra.
 */
public class App {

    /** A tömb elemszáma. */
    static final int ELEMSZAM = 40;

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        int[] szamok = egyediVeletlenTomb(ELEMSZAM, 1, 100);

        System.out.println("Az eredeti tömb (5 sor x 8 oszlop):");
        tablazatosKiiras(szamok, 8);

        kivalasztasosRendezesCsokkeno(szamok);
        System.out.println("\nA csökkenő sorrendbe rendezett tömb (4 sor x 10 oszlop):");
        tablazatosKiiras(szamok, 10);

        Scanner sc = new Scanner(System.in);
        System.out.print("\nKeresett szám: ");
        if (!sc.hasNextInt()) {
            System.out.println("Nem egész számot adott meg.");
            return;
        }
        int keresett = sc.nextInt();
        int index = binarisKeresesCsokkeno(szamok, keresett);
        if (index >= 0) {
            System.out.println("A(z) " + keresett + " megtalálható a tömbben, indexe: " + index);
        } else {
            System.out.println("A(z) " + keresett + " nem található meg a tömbben.");
        }
        sc.close();
    }

    /**
     * Létrehoz egy tömböt egymástól különböző véletlenszámokkal.
     *
     * @param elemszam a tömb mérete (legfeljebb max - min + 1 lehet)
     * @param min      a legkisebb lehetséges érték
     * @param max      a legnagyobb lehetséges érték (beleértve)
     * @return a feltöltött tömb
     */
    static int[] egyediVeletlenTomb(int elemszam, int min, int max) {
        Random rnd = new Random();
        int[] tomb = new int[elemszam];
        int db = 0; // eddig ennyi elemet töltöttünk fel
        // Addig generálunk, amíg meg nem telik a tömb. Ha a szám már szerepel,
        // eldobjuk és újat generálunk – ezért nem for, hanem while ciklus kell.
        while (db < elemszam) {
            int uj = rnd.nextInt(min, max + 1);
            if (!tartalmazza(tomb, db, uj)) {
                tomb[db] = uj;
                db++;
            }
        }
        return tomb;
    }

    /**
     * Eldöntés tétele: szerepel-e az érték a tömb első {@code db} elemében.
     *
     * @param tomb   a vizsgált tömb
     * @param db     ennyi elemet vizsgálunk a tömb elejéről
     * @param ertek  a keresett érték
     * @return {@code true}, ha szerepel
     */
    static boolean tartalmazza(int[] tomb, int db, int ertek) {
        for (int i = 0; i < db; i++) {
            if (tomb[i] == ertek) {
                return true;
            }
        }
        return false;
    }

    /**
     * Kiírja a tömböt táblázatosan, soronként adott számú elemmel.
     *
     * @param tomb          a kiírandó tömb
     * @param oszlopokSzama egy sorba kerülő elemek száma
     */
    static void tablazatosKiiras(int[] tomb, int oszlopokSzama) {
        for (int i = 0; i < tomb.length; i++) {
            System.out.printf("%4d", tomb[i]);
            // Az (i + 1). elem után akkor kell sortörés, ha (i + 1) osztható
            // az oszlopok számával – így lesz pl. 40 elemből 5 sor x 8 oszlop.
            if ((i + 1) % oszlopokSzama == 0) {
                System.out.println();
            }
        }
    }

    /**
     * Csökkenő sorrendbe rendezi a tömböt helyben, (maximum)kiválasztásos rendezéssel.
     * <p>
     * Működés: minden i. lépésben megkeressük a még rendezetlen rész
     * (i..vége) legnagyobb elemét, és kicseréljük az i. elemmel. Így a tömb
     * eleje lépésről lépésre rendezetté válik. Lépésszám: kb. n²/2 összehasonlítás.
     *
     * @param tomb a rendezendő tömb (a metódus a tömb tartalmát módosítja!)
     */
    static void kivalasztasosRendezesCsokkeno(int[] tomb) {
        for (int i = 0; i < tomb.length - 1; i++) {
            // 1. A legnagyobb elem indexének keresése az i..vége tartományban.
            int maxIndex = i;
            for (int j = i + 1; j < tomb.length; j++) {
                if (tomb[j] > tomb[maxIndex]) {
                    maxIndex = j;
                }
            }
            // 2. Csere egy segédváltozóval (ha nem önmagával cserélnénk).
            if (maxIndex != i) {
                int seged = tomb[i];
                tomb[i] = tomb[maxIndex];
                tomb[maxIndex] = seged;
            }
        }
        // A tömb referencia szerint adódik át: a metódusban a hívó tömbjét
        // rendezzük, ezért nem kell visszaadni.
    }

    /**
     * Bináris (logaritmikus) keresés CSÖKKENŐ sorrendbe rendezett tömbben.
     * <p>
     * Mindig a vizsgált tartomány közepén lévő elemet nézzük meg: ha az a
     * keresett, kész; ha a középső elem nagyobb, a keresett csak a jobb oldali
     * (kisebb értékeket tartalmazó) félben lehet, különben a bal oldaliban.
     * Minden lépés megfelezi a tartományt, így 40 elemnél legfeljebb 6 lépés kell.
     *
     * @param tomb     csökkenő sorrendbe rendezett tömb
     * @param keresett a keresett érték
     * @return a megtalált elem indexe, vagy -1, ha nincs a tömbben
     */
    static int binarisKeresesCsokkeno(int[] tomb, int keresett) {
        int bal = 0;
        int jobb = tomb.length - 1;
        while (bal <= jobb) {
            int kozep = (bal + jobb) / 2;
            if (tomb[kozep] == keresett) {
                return kozep;
            } else if (tomb[kozep] > keresett) {
                bal = kozep + 1;   // csökkenő tömb: a kisebbek jobbra vannak
            } else {
                jobb = kozep - 1;  // a nagyobbak balra vannak
            }
        }
        return -1;
    }
}
