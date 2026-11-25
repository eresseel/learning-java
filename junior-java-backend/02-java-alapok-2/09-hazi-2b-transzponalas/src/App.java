import java.util.Random;

/**
 * Önálló (házi) feladat II.B: egy 4x3-as mátrix feltöltése 50 és 100 közötti
 * véletlenszámokkal, transzponálása egy másik (3x4-es) mátrixba, majd mindkét
 * mátrix kiírása sor/oszlop formában.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Random rnd = new Random();

        // 1. Az eredeti mátrix: 4 sor, 3 oszlop, értékek 50..100 (mindkettő benne).
        int[][] eredeti = new int[4][3];
        for (int i = 0; i < eredeti.length; i++) {
            for (int j = 0; j < eredeti[i].length; j++) {
                eredeti[i][j] = rnd.nextInt(50, 101);
            }
        }

        // 2. Transzponálás: a sorokból oszlopok lesznek. Az új mátrix mérete
        //    fordított (3x4), és az [i][j] elem a [j][i] helyre kerül.
        int[][] transzponalt = new int[eredeti[0].length][eredeti.length];
        for (int i = 0; i < eredeti.length; i++) {
            for (int j = 0; j < eredeti[i].length; j++) {
                transzponalt[j][i] = eredeti[i][j];
            }
        }

        // 3. Kiírás egy közös metódussal – ugyanaz a kód mindkét mátrixra jó.
        System.out.println("Eredeti mátrix (4x3):");
        matrixKiirasa(eredeti);
        System.out.println("Transzponált mátrix (3x4):");
        matrixKiirasa(transzponalt);
    }

    /**
     * Kiír egy mátrixot sor/oszlop formában, egyenletes oszlopszélességgel.
     *
     * @param matrix a kiírandó kétdimenziós tömb
     */
    static void matrixKiirasa(int[][] matrix) {
        for (int[] sor : matrix) {           // a mátrix minden sora maga is egy tömb
            for (int ertek : sor) {
                System.out.printf("%5d", ertek);
            }
            System.out.println();
        }
    }
}
