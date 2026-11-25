import java.util.Scanner;

/**
 * Bekér egy osztályzatot (1–5), és kiírja szöveges formában
 * (elégtelen, elégséges, közepes, jó, jeles) – esetszétválasztással (switch).
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Osztályzat bekérése.
        System.out.print("Adja meg a tanuló osztályzatát (1-5): ");
        if (!sc.hasNextInt()) {
            System.out.println("hibás érték");
            return;
        }
        int osztalyzat = sc.nextInt();

        // 2. Esetszétválasztás (switch): a változó értékét összeveti a case
        //    címkékkel, és az egyezőnél kezdi a végrehajtást. A break kilép a
        //    switch-ből – ha lehagynánk, a következő case utasításai is lefutnának
        //    ("átesés", fall-through). A default ág minden más értékre lefut.
        switch (osztalyzat) {
            case 1:
                System.out.println("elégtelen");
                break;
            case 2:
                System.out.println("elégséges");
                break;
            case 3:
                System.out.println("közepes");
                break;
            case 4:
                System.out.println("jó");
                break;
            case 5:
                System.out.println("jeles");
                break;
            default:
                System.out.println("hibás érték");
                break;
        }

        // Megjegyzés: Java 14 óta létezik a tömörebb "nyilas" switch is, ahol nincs
        // átesés, ezért break sem kell:
        //   case 1 -> System.out.println("elégtelen");

        sc.close();
    }
}
