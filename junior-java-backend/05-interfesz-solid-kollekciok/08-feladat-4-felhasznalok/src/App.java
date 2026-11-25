import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 5. lecke, Feladat IV: felhasználói adatok beolvasása a felhasznalok.txt fájlból
 * ArrayList-be, listázás, szűrés jogosultság szerint, keresés és statisztika.
 */
public class App {

    private static final Scanner SC = new Scanner(System.in);

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. Beolvasás: a lista típusa az interfész (List), a megvalósítás ArrayList.
        List<Felhasznalo> felhasznalok = new ArrayList<>();
        try {
            beolvas("felhasznalok.txt", felhasznalok);
        } catch (IOException e) {
            System.out.println("A felhasznalok.txt nem olvasható (" + e.getMessage()
                    + "). A programot a feladat mappájából indítsa!");
            return;
        }
        System.out.println("Beolvasva " + felhasznalok.size() + " felhasználó.");

        // 2. Menü.
        String valasztas;
        do {
            System.out.println();
            System.out.println("1. Összes felhasználó");
            System.out.println("2. Szűrés jogosultság szerint");
            System.out.println("3. Keresés azonosító alapján");
            System.out.println("4. Statisztika");
            System.out.println("5. Kilépés");
            valasztas = sorBekeres("Választás: ");
            switch (valasztas) {
                case "1" -> felhasznalok.forEach(System.out::println);
                case "2" -> szures(felhasznalok);
                case "3" -> kereses(felhasznalok);
                case "4" -> statisztika(felhasznalok);
                case "5" -> System.out.println("Viszlát!");
                default -> System.out.println("Nincs ilyen menüpont.");
            }
        } while (!valasztas.equals("5"));
    }

    /**
     * Beolvassa a fájlt; az üres és '#'-tel kezdődő (megjegyzés) sorokat átugorja,
     * a hibás sorokat jelzi és kihagyja.
     *
     * @param fajlnev a fájl neve
     * @param cel     a lista, amelybe a felhasználók kerülnek
     * @throws IOException ha a fájl nem olvasható
     */
    private static void beolvas(String fajlnev, List<Felhasznalo> cel) throws IOException {
        List<String> sorok = Files.readAllLines(Path.of(fajlnev), StandardCharsets.UTF_8);
        for (int i = 0; i < sorok.size(); i++) {
            String sor = sorok.get(i).trim();
            if (sor.isEmpty() || sor.startsWith("#")) {
                continue;
            }
            try {
                cel.add(Felhasznalo.sorbol(sor));
            } catch (IllegalArgumentException e) {
                // Egy hibás sor miatt nem áll le a beolvasás, csak kihagyjuk.
                System.out.println("Kihagyott sor (" + (i + 1) + ".): " + e.getMessage());
            }
        }
    }

    /**
     * Jogosultság bekérése és a megfelelő felhasználók kiírása.
     *
     * @param felhasznalok a felhasználók listája
     */
    private static void szures(List<Felhasznalo> felhasznalok) {
        String kod = sorBekeres("Jogosultság (1 = Vendég, 2 = Felhasználó, 3 = Admin): ");
        try {
            Jogosultsag j = Jogosultsag.kodAlapjan(Integer.parseInt(kod));
            // Kiválogatás tétele: új listába gyűjtjük a feltételnek megfelelőket.
            List<Felhasznalo> talalat = new ArrayList<>();
            for (Felhasznalo f : felhasznalok) {
                if (f.getJogosultsag() == j) {
                    talalat.add(f);
                }
            }
            System.out.println(j.getMegnevezes() + " jogosultságú felhasználók (" + talalat.size() + " fő):");
            talalat.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Érvénytelen jogosultság: " + kod);
        }
    }

    /**
     * Azonosító bekérése és a felhasználó megkeresése.
     *
     * @param felhasznalok a felhasználók listája
     */
    private static void kereses(List<Felhasznalo> felhasznalok) {
        try {
            int id = Integer.parseInt(sorBekeres("Azonosító: "));
            for (Felhasznalo f : felhasznalok) {
                if (f.getAzonosito() == id) {
                    System.out.println(f);
                    return;
                }
            }
            System.out.println("Nincs ilyen azonosítójú felhasználó.");
        } catch (NumberFormatException e) {
            System.out.println("Az azonosító szám kell legyen.");
        }
    }

    /**
     * Megszámolja, hány felhasználó tartozik az egyes jogosultságokhoz.
     *
     * @param felhasznalok a felhasználók listája
     */
    private static void statisztika(List<Felhasznalo> felhasznalok) {
        for (Jogosultsag j : Jogosultsag.values()) {
            int db = 0;
            for (Felhasznalo f : felhasznalok) {
                if (f.getJogosultsag() == j) {
                    db++;
                }
            }
            System.out.printf("  %-12s %d fő%n", j.getMegnevezes(), db);
        }
        System.out.println("  Összesen:    " + felhasznalok.size() + " fő");
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
