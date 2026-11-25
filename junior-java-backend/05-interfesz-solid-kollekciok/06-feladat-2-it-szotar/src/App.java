import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * 5. lecke, Feladat II: informatikai szótár a szotar.csv fájlból,
 * angol–magyar és magyar–angol kereséssel.
 */
public class App {

    private static final Scanner SC = new Scanner(System.in);

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. A szótár beolvasása; ha nem sikerül, nincs értelme folytatni.
        Szotar szotar = new Szotar();
        try {
            int db = szotar.betolt("szotar.csv");
            System.out.println("Beolvasva " + db + " szópár a szotar.csv fájlból.");
        } catch (IOException e) {
            System.out.println("A szotar.csv nem olvasható (" + e.getMessage()
                    + "). A programot a feladat mappájából indítsa!");
            return;
        }

        // 2. Menü: irány választása, majd a kifejezés bekérése és a keresés.
        String valasztas;
        do {
            System.out.println();
            System.out.println("1. Angol → magyar");
            System.out.println("2. Magyar → angol");
            System.out.println("3. Kilépés");
            valasztas = sorBekeres("Választás: ");
            if (valasztas.equals("1") || valasztas.equals("2")) {
                boolean angolrol = valasztas.equals("1");
                String kifejezes = sorBekeres("Keresett kifejezés: ");
                String eredmeny = szotar.fordit(kifejezes, angolrol);
                if (eredmeny != null) {
                    System.out.println(kifejezes + " = " + eredmeny);
                } else {
                    System.out.println("A(z) \"" + kifejezes + "\" kifejezés nem szerepel a szótárban.");
                    List<String> javaslat = szotar.javaslatok(kifejezes.isEmpty() ? "" : kifejezes.substring(0, 1), angolrol);
                    if (!javaslat.isEmpty()) {
                        System.out.println("Hasonló kezdetű szavak: " + String.join(", ", javaslat));
                    }
                }
            } else if (!valasztas.equals("3")) {
                System.out.println("Nincs ilyen menüpont.");
            }
        } while (!valasztas.equals("3"));
        System.out.println("Viszlát!");
    }

    /**
     * Beolvas egy sort; ha elfogyott a bemenet, kilép a programból.
     *
     * @param uzenet a kiírandó kérdés
     * @return a beolvasott sor
     */
    private static String sorBekeres(String uzenet) {
        System.out.print(uzenet);
        if (!SC.hasNextLine()) {
            System.out.println();
            System.out.println("Nincs több bemenet, a program kilép.");
            System.exit(0);
        }
        return SC.nextLine().trim();
    }
}
