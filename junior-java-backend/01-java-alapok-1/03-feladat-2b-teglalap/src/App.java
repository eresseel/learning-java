import java.util.Scanner;

/**
 * Bekéri egy téglalap két oldalának hosszát, majd formázottan kiírja a
 * kerületét és a területét.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Az "a" oldal bekérése. A hasNextDouble() ellenőrzi, hogy valóban
        //    szám következik-e, így hibás bemenetnél nem száll el a program.
        System.out.print("Az a oldal hossza (cm): ");
        if (!sc.hasNextDouble()) {
            System.out.println("Hibás bemenet: számot kell megadni.");
            return; // a main-ből visszatérve a program véget ér
        }
        double a = sc.nextDouble();

        // 2. A "b" oldal bekérése ugyanígy.
        System.out.print("A b oldal hossza (cm): ");
        if (!sc.hasNextDouble()) {
            System.out.println("Hibás bemenet: számot kell megadni.");
            return;
        }
        double b = sc.nextDouble();

        // 3. Ellenőrzés: oldalhossz csak pozitív lehet.
        if (a <= 0 || b <= 0) {
            System.out.println("Az oldalak hossza csak pozitív szám lehet.");
            return;
        }

        // 4. Számítás a képletek alapján: K = 2 * (a + b), T = a * b.
        double kerulet = 2 * (a + b);
        double terulet = a * b;

        // 5. Formázott kiírás printf-fel:
        //    %.2f – valós szám két tizedesjegyre kerekítve,
        //    %8.2f – ugyanez legalább 8 karakter szélesen, jobbra igazítva,
        //    %n – platformfüggetlen sortörés.
        System.out.printf("%nA téglalap oldalai: %.2f cm és %.2f cm%n", a, b);
        System.out.printf("Kerület: %8.2f cm%n", kerulet);
        System.out.printf("Terület: %8.2f cm²%n", terulet);

        sc.close();
    }
}
