import java.util.Scanner;

/**
 * 3. lecke, önálló (otthoni) feladat: napi kalóriaszámláló.
 * Az adatok konzolról érkeznek, a program a nap végén kiírja az egyenleget.
 */
public class App {

    private static final Scanner SC = new Scanner(System.in);

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. Név bekérése és a számláló objektum létrehozása.
        String nev = sorBekeres("Név: ");
        KaloriaSzamlalo szamlalo = new KaloriaSzamlalo(nev.isEmpty() ? "Névtelen" : nev);

        // 2. Események rögzítése, amíg a felhasználó a nap végét nem választja.
        int valasztas;
        do {
            System.out.println();
            System.out.println("1. Táplálkozás (kcal)");
            System.out.println("2. Sportolás (óra, 600 kcal/óra)");
            System.out.println("3. Pihenés (óra, 40 kcal/óra)");
            System.out.println("4. Nap vége");
            valasztas = (int) szamBekeres("Választás: ");
            switch (valasztas) {
                case 1 -> szamlalo.taplalkozas(szamBekeres("Elfogyasztott kalória: "));
                case 2 -> szamlalo.sportolas(szamBekeres("Sporttal töltött órák: "));
                case 3 -> szamlalo.pihenes(szamBekeres("Pihenéssel töltött órák: "));
                case 4 -> { /* a ciklus feltétele kezeli a kilépést */ }
                default -> System.out.println("Nincs ilyen menüpont.");
            }
            // Minden lépés után megmutatjuk a pillanatnyi állást.
            if (valasztas >= 1 && valasztas <= 3) {
                System.out.printf("Pillanatnyi érték: %.0f kcal%n", szamlalo.getNapiKaloria());
            }
        } while (valasztas != 4);

        // 3. A nap végi érték kiírása.
        System.out.printf("%n%s nap végi kalóriaegyenlege: %.0f kcal%n",
                szamlalo.getNev(), szamlalo.getNapiKaloria());
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

    /**
     * Nemnegatív számot kér be (tizedesvessző is megengedett).
     *
     * @param uzenet a kiírandó kérdés
     * @return a beolvasott szám
     */
    private static double szamBekeres(String uzenet) {
        while (true) {
            try {
                double ertek = Double.parseDouble(sorBekeres(uzenet).replace(',', '.'));
                if (ertek >= 0) {
                    return ertek;
                }
                System.out.println("Negatív érték nem adható meg!");
            } catch (NumberFormatException e) {
                System.out.println("Kérem, számot adjon meg!");
            }
        }
    }
}
