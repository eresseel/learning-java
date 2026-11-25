import java.util.Scanner;

/**
 * Addig kér be számokat, amíg a felhasználó nem páros (azaz páratlan)
 * számot ad meg.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int szam;

        // Hátultesztelős ciklus: legalább egyszer mindenképp be kell kérni egy számot,
        // és csak utána tudjuk eldönteni, kell-e folytatni. Erre a do-while ideális.
        do {
            System.out.print("Adjon meg egy egész számot: ");

            // Ha a bemenet véget ért (pl. pipe-olt bemenetnél elfogytak a sorok),
            // kilépünk, különben végtelen ciklusba kerülnénk.
            if (!sc.hasNext()) {
                System.out.println("\nA bemenet véget ért.");
                return;
            }
            // Ha nem egész szám jött, eldobjuk a hibás tokent és újra kérdezünk.
            if (!sc.hasNextInt()) {
                System.out.println("Ez nem egész szám: " + sc.next());
                szam = 0; // páros érték, így a ciklus folytatódik
                continue; // a continue a do-while feltételére ugrik
            }
            szam = sc.nextInt();

            if (szam % 2 == 0) {
                System.out.println(szam + " páros, kérek egy másikat.");
            }
            // Páratlan számnál a maradék 1 vagy (negatív számnál!) -1,
            // ezért a feltétel "!= 0" és nem "== 1".
        } while (szam % 2 == 0);

        System.out.println("Végre egy páratlan szám: " + szam);
        sc.close();
    }
}
