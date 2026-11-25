import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 5. lecke, Feladat I: egy cég kapcsolatainak és szolgáltatásainak adminisztrálása,
 * egységes CSV mentéssel.
 */
public class App {

    private static final Scanner SC = new Scanner(System.in);

    /**
     * Belépési pont: menü az adatok felvételéhez, listázásához és mentéséhez.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. Kezdő (fiktív) adatok.
        List<Kapcsolat> kapcsolatok = new ArrayList<>();
        kapcsolatok.add(new Kapcsolat("Minta Kft.", "1000 Budapest, Példa utca 1.", "info@minta.pelda.hu", "+36 1 000 0000"));
        kapcsolatok.add(new Kapcsolat("Teszt Bt.", "9999 Kitalált, Fő tér 2.", "iroda@teszt.pelda.hu", "+36 1 000 0001"));
        List<Szolgaltatas> szolgaltatasok = new ArrayList<>();
        szolgaltatasok.add(new Szolgaltatas("Weboldal-karbantartás (havi)", 25000));
        szolgaltatasok.add(new Szolgaltatas("Rendszergazdai óradíj", 12000));

        String valasztas;
        do {
            System.out.println();
            System.out.println("=== Kapcsolatok és szolgáltatások ===");
            System.out.println("1. Új kapcsolat");
            System.out.println("2. Új szolgáltatás");
            System.out.println("3. Listázás");
            System.out.println("4. Mentés CSV-be");
            System.out.println("5. Kilépés");
            valasztas = sorBekeres("Választás: ");
            switch (valasztas) {
                case "1" -> kapcsolatok.add(new Kapcsolat(
                        sorBekeres("Név: "), sorBekeres("Cím: "),
                        sorBekeres("Email: "), sorBekeres("Telefon: ")));
                case "2" -> {
                    String megnevezes = sorBekeres("Megnevezés: ");
                    try {
                        int ar = Integer.parseInt(sorBekeres("Ár (Ft): "));
                        szolgaltatasok.add(new Szolgaltatas(megnevezes, ar));
                    } catch (NumberFormatException e) {
                        System.out.println("Hibás ár, a szolgáltatás nem került felvételre.");
                    }
                }
                case "3" -> {
                    System.out.println("--- Kapcsolatok ---");
                    kapcsolatok.forEach(System.out::println);
                    System.out.println("--- Szolgáltatások ---");
                    szolgaltatasok.forEach(System.out::println);
                }
                case "4" -> mentes(kapcsolatok, szolgaltatasok);
                case "5" -> System.out.println("Viszlát!");
                default -> System.out.println("Nincs ilyen menüpont.");
            }
        } while (!valasztas.equals("5"));
    }

    /**
     * Mindkét listát ugyanazzal a metódussal menti – ez az interfész haszna –,
     * majd ellenőrzésképp visszaolvassa és kiírja a fájlokat.
     *
     * @param kapcsolatok    a kapcsolatok
     * @param szolgaltatasok a szolgáltatások
     */
    private static void mentes(List<Kapcsolat> kapcsolatok, List<Szolgaltatas> szolgaltatasok) {
        try {
            int k = CsvMento.ment("kapcsolatok.csv", kapcsolatok);
            int s = CsvMento.ment("szolgaltatasok.csv", szolgaltatasok);
            System.out.println("Mentve: kapcsolatok.csv (" + k + " sor), szolgaltatasok.csv (" + s + " sor)");
            for (String fajl : new String[]{"kapcsolatok.csv", "szolgaltatasok.csv"}) {
                System.out.println("--- " + fajl + " ---");
                Files.readAllLines(Path.of(fajl), StandardCharsets.UTF_8).forEach(System.out::println);
            }
        } catch (IOException e) {
            System.out.println("Hiba a mentés során: " + e.getMessage());
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
