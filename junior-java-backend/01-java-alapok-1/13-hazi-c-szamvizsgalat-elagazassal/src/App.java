import java.util.Scanner;

/**
 * Bekér egy egyjegyű pozitív egész számot, és elágazásokkal (if – else if – else)
 * eldönti, melyik kategóriába esik:
 * <ul>
 *   <li>1–3: "A szám nem nagyobb háromnál"</li>
 *   <li>4–6: "A szám 4 és 6 közötti"</li>
 *   <li>7–9: "A szám nagyobb hatnál"</li>
 *   <li>minden más: "Nem megfelelő értéket adott meg"</li>
 * </ul>
 * Ugyanennek a switch-es megoldása a szomszédos 14-es mappában található.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Szám bekérése. Ha nem egész szám érkezik, az is "nem megfelelő érték".
        System.out.print("Adjon meg egy egyjegyű pozitív egész számot: ");
        if (!sc.hasNextInt()) {
            System.out.println("Nem megfelelő értéket adott meg");
            return;
        }
        int szam = sc.nextInt();

        // 2. Először a hibás értékeket szűrjük ki (nem 1 és 9 közötti), így a
        //    további ágakban már csak az alsó/felső határt kell figyelni.
        if (szam < 1 || szam > 9) {
            System.out.println("Nem megfelelő értéket adott meg");
        } else if (szam <= 3) {
            System.out.println("A szám nem nagyobb háromnál");
        } else if (szam <= 6) {
            // ide csak 4, 5 vagy 6 juthat el
            System.out.println("A szám 4 és 6 közötti");
        } else {
            // ide csak 7, 8 vagy 9 juthat el
            System.out.println("A szám nagyobb hatnál");
        }

        sc.close();
    }
}
