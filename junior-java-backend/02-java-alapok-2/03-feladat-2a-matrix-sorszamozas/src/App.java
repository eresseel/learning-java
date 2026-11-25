/**
 * Létrehoz egy 5x3-as kétdimenziós tömböt, feltölti 1-től kezdődő futó
 * sorszámmal (egy segédváltozó segítségével), majd kilistázza az elemeket
 * az elsőtől az utolsóig, illetve az utolsótól az elsőig.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. 5 sor és 3 oszlop. matrix.length a sorok száma (5),
        //    matrix[i].length az i-edik sor hossza, azaz az oszlopok száma (3).
        int[][] matrix = new int[5][3];

        // 2. Feltöltés egymásba ágyazott ciklusokkal. A "sorszam" segédváltozó
        //    számolja, hányadik elemnél tartunk: minden értékadás után eggyel nő.
        //    (Debugger: tegyünk töréspontot a következő sorra, és figyeljük
        //     az i, j és sorszam változók értékét!)
        int sorszam = 1;
        for (int i = 0; i < matrix.length; i++) {          // külső ciklus: sorok
            for (int j = 0; j < matrix[i].length; j++) {   // belső ciklus: oszlopok
                matrix[i][j] = sorszam;
                sorszam++;
            }
        }

        // 3. Listázás az elsőtől az utolsóig, sor/oszlop formában.
        //    %3d: legalább 3 karakter szélesen, jobbra igazítva – így szép oszlopok lesznek.
        System.out.println("Az elsőtől az utolsóig:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%3d", matrix[i][j]);
            }
            System.out.println(); // sor végén sortörés
        }

        // 4. Listázás az utolsótól az elsőig: mindkét ciklus visszafelé halad,
        //    az utolsó indextől (length - 1) a 0-ig.
        System.out.println("Az utolsótól az elsőig:");
        for (int i = matrix.length - 1; i >= 0; i--) {
            for (int j = matrix[i].length - 1; j >= 0; j--) {
                System.out.printf("%3d", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
