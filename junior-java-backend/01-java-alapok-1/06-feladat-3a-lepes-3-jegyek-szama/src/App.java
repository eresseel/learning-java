import java.util.Scanner;

/**
 * Feladat III.A, 3. lépés: bekér egy egész számot, és kiírja, hogy
 * "egyjegyű", "kétjegyű" vagy "többjegyű" (többágú elágazás: if – else if – else).
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Szám bekérése, ellenőrizve, hogy egész szám érkezik-e.
        System.out.print("Adjon meg egy egész számot: ");
        if (!sc.hasNextInt()) {
            System.out.println("Nem egész számot adott meg.");
            return;
        }
        int szam = sc.nextInt();

        // 2. Az előjel nem számít a jegyek számába, ezért az abszolútértékkel dolgozunk.
        int abszolut = Math.abs(szam);

        // 3. Többágú elágazás: a feltételeket felülről lefelé vizsgálja a program,
        //    és az ELSŐ igaz feltételhez tartozó ág fut le. Ezért a második
        //    feltételnél már nem kell vizsgálni, hogy abszolut > 9, hiszen
        //    ha 9 vagy kisebb lenne, az első ág futott volna le.
        if (abszolut <= 9) {
            System.out.println("egyjegyű");
        } else if (abszolut <= 99) {
            System.out.println("kétjegyű");
        } else {
            System.out.println("többjegyű");
        }

        sc.close();
    }
}
