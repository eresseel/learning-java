/**
 * Létrehoz egy 5x3-as kétdimenziós tömböt, és feltölti 1-től kezdődő futó
 * sorszámmal – ezúttal segédváltozó NÉLKÜL: a sorszámot a sor- és oszlopindexből
 * számoljuk ki. Ezután kilistázza az elemeket előre és visszafelé is.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        int[][] matrix = new int[5][3];
        int oszlopokSzama = matrix[0].length; // 3 – minden sor egyforma hosszú

        // 1. Feltöltés képlettel: az i-edik sor előtt már i * oszlopokSzama elem van,
        //    a soron belül a j-edik elem következik, és mivel 1-től számozunk, +1.
        //    Példa: i = 2, j = 1  ->  2 * 3 + 1 + 1 = 8.
        //    (Debugger: töréspont a következő sorra, figyeljük az i, j és a
        //     kiszámolt érték alakulását – a "Watch" ablakba felvehető a
        //     i * oszlopokSzama + j + 1 kifejezés is.)
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = i * oszlopokSzama + j + 1;
            }
        }

        // 2. Listázás az elsőtől az utolsóig.
        System.out.println("Az elsőtől az utolsóig:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%3d", matrix[i][j]);
            }
            System.out.println();
        }

        // 3. Listázás az utolsótól az elsőig.
        System.out.println("Az utolsótól az elsőig:");
        for (int i = matrix.length - 1; i >= 0; i--) {
            for (int j = matrix[i].length - 1; j >= 0; j--) {
                System.out.printf("%3d", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
