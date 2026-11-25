import java.util.Scanner;

/**
 * Feladat III.A, 1. lépés: bekér egy egész számot, és üzenetet ír ki,
 * ha a szám egyjegyű. (Ha nem egyjegyű, a program nem ír ki semmit.)
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

        // 2. Egyszerű elágazás (else ág nélkül): csak akkor fut le a blokk,
        //    ha a feltétel igaz. Egyjegyű a szám, ha -9 és 9 közé esik –
        //    a negatív számokat is figyelembe vesszük, ezért mindkét határt vizsgáljuk.
        if (szam >= -9 && szam <= 9) {
            System.out.println("A szám egyjegyű.");
        }

        sc.close();
    }
}
