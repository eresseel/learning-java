import java.util.Scanner;

/**
 * Bekér egy alapot és egy (nemnegatív egész) kitevőt, majd a hatványt
 * ciklussal, ismételt szorzással számolja ki (Math.pow használata nélkül).
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Alap bekérése – lehet valós szám is (pl. 1.5).
        System.out.print("Alap: ");
        if (!sc.hasNextDouble()) {
            System.out.println("Nem számot adott meg.");
            return;
        }
        double alap = sc.nextDouble();

        // 2. Kitevő bekérése – nemnegatív egész, hiszen ennyiszer szorzunk.
        System.out.print("Kitevő (nemnegatív egész): ");
        if (!sc.hasNextInt()) {
            System.out.println("Nem egész számot adott meg.");
            return;
        }
        int kitevo = sc.nextInt();
        if (kitevo < 0) {
            System.out.println("A kitevő nem lehet negatív.");
            return;
        }

        // 3. Hatványozás ciklussal: az eredményt 1-ről indítjuk (a szorzás
        //    semleges eleme), és "kitevo"-szer megszorozzuk az alappal.
        //    Kitevő = 0 esetén a ciklus egyszer sem fut le, így az eredmény 1 –
        //    ez matematikailag is helyes (a^0 = 1).
        double eredmeny = 1;
        for (int i = 1; i <= kitevo; i++) {
            eredmeny *= alap;
        }

        // 4. Kiírás. A %s a double-t "természetes" alakban írja ki (pl. 1024.0);
        //    ellenőrzésképpen a beépített Math.pow eredményét is mellé tesszük.
        System.out.println(alap + "^" + kitevo + " = " + eredmeny);
        System.out.println("Ellenőrzés Math.pow-val: " + Math.pow(alap, kitevo));

        sc.close();
    }
}
