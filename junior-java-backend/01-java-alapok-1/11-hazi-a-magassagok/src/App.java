import java.util.Scanner;

/**
 * Bekéri két ember magasságát centiméterben, és kiírja, melyikük a magasabb
 * és mennyivel – külön üzenettel arra az esetre, ha egyforma magasak.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Az első ember magassága.
        System.out.print("Az első ember magassága (cm): ");
        if (!sc.hasNextInt()) {
            System.out.println("Nem egész számot adott meg.");
            return;
        }
        int elso = sc.nextInt();

        // 2. A második ember magassága.
        System.out.print("A második ember magassága (cm): ");
        if (!sc.hasNextInt()) {
            System.out.println("Nem egész számot adott meg.");
            return;
        }
        int masodik = sc.nextInt();

        // 3. Ésszerűségi ellenőrzés: a magasság csak pozitív lehet.
        if (elso <= 0 || masodik <= 0) {
            System.out.println("A magasság csak pozitív szám lehet.");
            return;
        }

        // 4. Többágú elágazás a három lehetséges esetre. A különbséget mindig
        //    a nagyobból vonjuk ki a kisebbet, így pozitív szám lesz.
        if (elso > masodik) {
            System.out.println("Az első ember a magasabb, " + (elso - masodik) + " cm-rel.");
        } else if (masodik > elso) {
            System.out.println("A második ember a magasabb, " + (masodik - elso) + " cm-rel.");
        } else {
            System.out.println("Egyforma magasak (" + elso + " cm).");
        }

        sc.close();
    }
}
