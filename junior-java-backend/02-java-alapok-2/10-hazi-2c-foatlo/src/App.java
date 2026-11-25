import java.util.Random;

/**
 * Önálló (házi) feladat II.C: egy 3x3-as mátrix feltöltése egyjegyű
 * véletlenszámok négyzetével; a főátló elemeinek és összegüknek kiírása, majd
 * az eredetileg generált (négyzetre emelés előtti) számok kiírása fordított
 * sorrendben, az utolsó elemtől az elsőig.
 */
public class App {

    /** A négyzetes mátrix mérete. */
    static final int MERET = 3;

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Random rnd = new Random();

        // 1. Két mátrixot töltünk párhuzamosan: az "eredeti" a generált egyjegyű
        //    számokat őrzi meg (0..9), a "negyzetek" ezek négyzetét. Az eredetit
        //    azért tároljuk, mert a feladat végén vissza kell adni ezeket.
        //    (Alternatíva lenne a Math.sqrt-tel visszaszámolni, de a tárolás egyszerűbb.)
        int[][] eredeti = new int[MERET][MERET];
        int[][] negyzetek = new int[MERET][MERET];
        for (int i = 0; i < MERET; i++) {
            for (int j = 0; j < MERET; j++) {
                eredeti[i][j] = rnd.nextInt(10);           // 0..9
                negyzetek[i][j] = eredeti[i][j] * eredeti[i][j];
            }
        }

        System.out.println("A négyzetre emelt értékek mátrixa:");
        for (int[] sor : negyzetek) {
            for (int ertek : sor) {
                System.out.printf("%4d", ertek);
            }
            System.out.println();
        }

        // 2. Főátló: azok az elemek, ahol a sor- és oszlopindex egyenlő ([0][0],
        //    [1][1], [2][2]). Ehhez elég EGY ciklus, nem kell egymásba ágyazni.
        int foatloOsszeg = 0;
        System.out.print("\nA főátló elemei: ");
        for (int i = 0; i < MERET; i++) {
            System.out.print(negyzetek[i][i] + " ");
            foatloOsszeg += negyzetek[i][i];
        }
        System.out.println("\nA főátló összege: " + foatloOsszeg);

        // 3. Az eredetileg generált számok fordított sorrendben: az utolsó sor
        //    utolsó elemétől haladunk visszafelé az első sor első eleméig.
        System.out.print("\nAz eredeti számok fordított sorrendben: ");
        for (int i = MERET - 1; i >= 0; i--) {
            for (int j = MERET - 1; j >= 0; j--) {
                System.out.print(eredeti[i][j] + " ");
            }
        }
        System.out.println();
    }
}
