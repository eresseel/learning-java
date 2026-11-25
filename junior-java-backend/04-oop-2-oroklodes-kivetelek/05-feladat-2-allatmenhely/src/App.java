import java.util.Scanner;

/**
 * Összefoglaló projektmunka: állatmenhely nyilvántartó program menürendszerrel.
 */
public class App {

    private static final Scanner SC = new Scanner(System.in);

    /**
     * Belépési pont: menü megjelenítése, amíg a felhasználó ki nem lép.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Menhely menhely = new Menhely();
        String valasztas;
        do {
            System.out.println();
            System.out.println("=== Állatmenhely (" + menhely.getDarab() + "/" + Menhely.KAPACITAS + ") ===");
            System.out.println("1. Menhelyre történő felvétel");
            System.out.println("2. Menhely állatainak listázása");
            System.out.println("3. Menhely felszabadítása");
            System.out.println("4. Kilépés");
            valasztas = sorBekeres("Választás: ");
            switch (valasztas) {
                case "1" -> felvetel(menhely);
                case "2" -> listazas(menhely);
                case "3" -> felszabaditas(menhely);
                case "4" -> System.out.println("Viszlát!");
                default -> System.out.println("Nincs ilyen menüpont.");
            }
        } while (!valasztas.equals("4"));
    }

    /**
     * 1. menüpont: adatbekérés és felvétel. A hibás adatot az osztályok
     * kivétellel jelzik, amit itt kapunk el.
     *
     * @param menhely a menhely
     */
    private static void felvetel(Menhely menhely) {
        // 1. Ha betelt a menhely, adatot sem kérünk be.
        if (menhely.isTele()) {
            System.out.println("A menhely megtelt, további állat nem vehető fel!");
            return;
        }
        // 2. Faj kiválasztása.
        String faj = sorBekeres("Kutya vagy macska? (k/m): ").toLowerCase();
        if (!faj.equals("k") && !faj.equals("m")) {
            System.out.println("Csak 'k' vagy 'm' adható meg.");
            return;
        }
        try {
            // 3. Közös adatok bekérése.
            String nev = sorBekeres("Név: ");
            double suly = Double.parseDouble(sorBekeres("Súly (kg): ").replace(',', '.'));
            // 4. Faj-specifikus adat és az objektum létrehozása (polimorf Allat változóba).
            Allat allat;
            if (faj.equals("k")) {
                int marmagassag = Integer.parseInt(sorBekeres("Marmagasság (cm): "));
                allat = new Kutya(nev, suly, marmagassag);
            } else {
                String szor = sorBekeres("Rövidszőrű vagy hosszúszőrű? (r/h): ").toLowerCase();
                if (!szor.equals("r") && !szor.equals("h")) {
                    throw new IllegalArgumentException("A szőrhossz csak 'r' vagy 'h' lehet!");
                }
                allat = new Macska(nev, suly, szor.equals("r"));
            }
            menhely.felvesz(allat);
            System.out.println("Felvéve: " + allat.getNev());
        } catch (NumberFormatException e) {
            // A NumberFormatException az IllegalArgumentException leszármazottja,
            // ezért az ő ága kerül előre (a specifikusabb előbb!).
            System.out.println("Hibás számformátum, a felvétel elmaradt.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Hiba: " + e.getMessage() + " A felvétel elmaradt.");
        }
    }

    /**
     * 2. menüpont: listázás, de csak ha van állat a menhelyen.
     *
     * @param menhely a menhely
     */
    private static void listazas(Menhely menhely) {
        if (menhely.isUres()) {
            System.out.println("A menhelyen jelenleg nincs állat.");
            return;
        }
        menhely.listaz();
    }

    /**
     * 3. menüpont: a legrégebben felvett állat kikerül, csak ha van állat.
     *
     * @param menhely a menhely
     */
    private static void felszabaditas(Menhely menhely) {
        try {
            Allat kikerult = menhely.felszabadit();
            System.out.println("Gazdihoz került: " + kikerult.getNev());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
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
}
