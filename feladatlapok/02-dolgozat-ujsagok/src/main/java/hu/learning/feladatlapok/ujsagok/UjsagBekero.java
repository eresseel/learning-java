package hu.learning.feladatlapok.ujsagok;

import java.util.Scanner;
import java.util.function.Function;

/**
 * Konzolos, ellenőrzött adatbekérés (Feladat IX.).
 * <p>
 * Az ellenőrzést nem itt írjuk meg újra: a bekérő az osztályok statikus
 * {@code ellenoriz...} metódusait hívja (osztályszintű ellenőrzés). Érvénytelen
 * adatnál kiírja a kivétel üzenetét, és újra kérdez.
 * <p>
 * Ha a bemenet elfogy (EOF, pl. pipe-olt bemenet végén), a metódusok
 * {@code null}-t adnak vissza, így a program kivétel nélkül befejeződhet.
 */
public class UjsagBekero {

    private final Scanner scanner;

    /**
     * @param scanner a bemenet olvasója (általában {@code new Scanner(System.in)})
     */
    public UjsagBekero(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Kiír egy kérdést, és beolvas egy sort.
     *
     * @param kerdes a kiírandó kérdés
     * @return a beolvasott sor (levágott szóközökkel), vagy {@code null}, ha nincs több bemenet
     */
    public String sortBeker(String kerdes) {
        System.out.print(kerdes);
        // EOF esetén a nextLine() kivételt dobna, ezért előbb megnézzük, van-e még sor.
        if (!scanner.hasNextLine()) {
            System.out.println();
            return null;
        }
        return scanner.nextLine().trim();
    }

    /**
     * Addig kérdez, amíg az átalakító/ellenőrző függvény kivétel nélkül le nem fut.
     * Ez az általános „ismételd, amíg jó nem lesz” ciklus, minden mezőnél ezt használjuk.
     *
     * @param kerdes    a kiírandó kérdés
     * @param ellenorzo a beolvasott szöveget érvényes értékké alakító függvény;
     *                  érvénytelen adatnál {@link IllegalArgumentException}-t dob
     * @param <T>       az eredmény típusa
     * @return az érvényes érték, vagy {@code null}, ha elfogyott a bemenet
     */
    public <T> T ellenorzottenBeker(String kerdes, Function<String, T> ellenorzo) {
        while (true) {
            String sor = sortBeker(kerdes);
            if (sor == null) {
                return null; // nincs több bemenet
            }
            try {
                // Az ellenőrző dob kivételt, ha az adat hibás. A NumberFormatException
                // is IllegalArgumentException leszármazott, így azt is elkapjuk.
                return ellenorzo.apply(sor);
            } catch (NumberFormatException e) {
                System.out.println("  Hiba: egész számot adjon meg!");
            } catch (IllegalArgumentException e) {
                System.out.println("  Hiba: " + e.getMessage());
            }
        }
    }

    /**
     * Bekér egy teljes újságot: típust, közös adatokat és a típusfüggő mezőt.
     *
     * @return az ellenőrzötten létrehozott újság, vagy {@code null}, ha elfogyott a bemenet
     */
    public Ujsag ujsagotBeker() {
        // 1. Típus: csak a három ismert érték fogadható el.
        String tipus = ellenorzottenBeker("Típus (Ujsag / Napilap / Hetilap): ", s -> {
            for (String t : new String[]{"Ujsag", "Napilap", "Hetilap"}) {
                if (t.equalsIgnoreCase(s)) {
                    return t;
                }
            }
            throw new IllegalArgumentException("Ismeretlen típus: " + s);
        });
        if (tipus == null) {
            return null;
        }

        // 2. Szöveges mezők: nem üres (Ujsag szabálya) és nincs benne kötőjel (fájlformátum).
        String megnevezes = ellenorzottenBeker("Megnevezés: ", s -> {
            UjsagFajlKezelo.ellenorizCsvMezo(s, "megnevezés");
            return Ujsag.ellenorizNemUres(s, "megnevezés");
        });
        if (megnevezes == null) {
            return null;
        }
        String kiado = ellenorzottenBeker("Kiadó: ", s -> {
            UjsagFajlKezelo.ellenorizCsvMezo(s, "kiadó");
            return Ujsag.ellenorizNemUres(s, "kiadó");
        });
        if (kiado == null) {
            return null;
        }

        // 3. Ár: egész szám és nem negatív.
        Integer ar = ellenorzottenBeker("Ár (Ft): ", s -> Ujsag.ellenorizAr(Integer.parseInt(s)));
        if (ar == null) {
            return null;
        }

        // 4. Típusfüggő mező és az objektum létrehozása.
        switch (tipus) {
            case "Napilap" -> {
                Integer heti = ellenorzottenBeker("Heti megjelenés (4..7): ",
                        s -> Napilap.ellenorizHetiMegjelenes(Integer.parseInt(s)));
                return heti == null ? null : new Napilap(megnevezes, kiado, ar, heti);
            }
            case "Hetilap" -> {
                Integer nap = ellenorzottenBeker("A hét hányadik napján jelenik meg (1..7): ",
                        s -> Hetilap.ellenorizHetNapja(Integer.parseInt(s)));
                return nap == null ? null : new Hetilap(megnevezes, kiado, ar, nap);
            }
            default -> {
                return new Ujsag(megnevezes, kiado, ar);
            }
        }
    }
}
