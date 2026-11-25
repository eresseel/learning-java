import java.util.Scanner;

/**
 * Feladat III.A, 2. lépés: bekér egy egész számot, és kiírja, hogy
 * egyjegyű-e vagy sem (kétirányú elágazás: if–else).
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

        // 2. Kétirányú elágazás: pontosan az egyik ág fut le.
        //    Math.abs: abszolútérték, így a negatív számokra is jól működik
        //    (pl. -7 is egyjegyű). Ez rövidebb, mint a két határ vizsgálata.
        if (Math.abs(szam) <= 9) {
            System.out.println("A szám egyjegyű.");
        } else {
            // Új ág az előző lépéshez képest.
            System.out.println("A szám nem egyjegyű.");
        }

        sc.close();
    }
}
