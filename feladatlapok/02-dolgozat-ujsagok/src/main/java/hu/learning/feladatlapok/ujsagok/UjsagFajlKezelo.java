package hu.learning.feladatlapok.ujsagok;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Az ujsagok.csv fájl olvasása és írása (Feladat II. és X.).
 * <p>
 * Soronként egy sajtótermék, a mezőket kötőjel választja el:
 * <pre>
 * Ujsag   - megnevezés - kiadó - ár
 * Napilap - megnevezés - kiadó - ár - heti megjelenés
 * Hetilap - megnevezés - kiadó - ár - hét hányadik napja
 * </pre>
 * Mivel a kötőjel az elválasztó, egyik szöveges mező sem tartalmazhat kötőjelet.
 */
public final class UjsagFajlKezelo {

    /** A mezőelválasztó karakter a fájlban. */
    public static final String ELVALASZTO = "-";

    /** Csak statikus segédmetódusok vannak, ezért nem példányosítható. */
    private UjsagFajlKezelo() {
    }

    /**
     * Beolvassa a fájlt, és az adatokat egy TÖMBBEN adja vissza (a feladat tömböt kér).
     * A dolgozat szerint feltételezhetjük, hogy a fájl helyes adatokat tartalmaz;
     * csak az üres sorokat hagyjuk ki.
     *
     * @param fajl a CSV fájl útvonala
     * @return a beolvasott újságok tömbje, a fájlbeli sorrendben
     * @throws IOException ha a fájl nem olvasható
     */
    public static Ujsag[] beolvas(Path fajl) throws IOException {
        // 1. Az összes sor beolvasása (UTF-8 kódolással).
        List<String> sorok = Files.readAllLines(fajl, StandardCharsets.UTF_8);

        // 2. Megszámoljuk a nem üres sorokat, hogy tudjuk, mekkora tömb kell.
        int db = 0;
        for (String sor : sorok) {
            if (!sor.isBlank()) {
                db++;
            }
        }

        // 3. Létrehozzuk a pontos méretű tömböt, és feltöltjük.
        Ujsag[] ujsagok = new Ujsag[db];
        int i = 0;
        for (String sor : sorok) {
            if (!sor.isBlank()) {
                ujsagok[i++] = sorbolUjsag(sor);
            }
        }
        return ujsagok;
    }

    /**
     * Egy CSV sorból a megfelelő típusú objektumot hozza létre.
     * Az első mező dönti el, melyik osztályt példányosítjuk – a visszatérési típus
     * mégis az ős ({@link Ujsag}), ez a polimorfizmus.
     *
     * @param sor egy sor a fájlból, pl. „Hetilap - Kertbarat Het - Zold Lap Kft - 690 - 4”
     * @return a sorból létrehozott újság
     * @throws IllegalArgumentException ha a típus ismeretlen, vagy az adatok érvénytelenek
     * @throws NumberFormatException    ha egy számmező nem szám
     */
    public static Ujsag sorbolUjsag(String sor) {
        // 1. Feldarabolás a kötőjelek mentén, a mezők körüli szóközök levágása.
        String[] d = sor.split(ELVALASZTO);
        for (int i = 0; i < d.length; i++) {
            d[i] = d[i].trim();
        }

        // 2. A típus alapján a megfelelő konstruktor hívása (switch kifejezés).
        return switch (d[0]) {
            case "Ujsag" -> new Ujsag(d[1], d[2], Integer.parseInt(d[3]));
            case "Napilap" -> new Napilap(d[1], d[2], Integer.parseInt(d[3]), Integer.parseInt(d[4]));
            case "Hetilap" -> new Hetilap(d[1], d[2], Integer.parseInt(d[3]), Integer.parseInt(d[4]));
            default -> throw new IllegalArgumentException("Ismeretlen típus a fájlban: " + d[0]);
        };
    }

    /**
     * Egy újság objektumot visszaalakít a fájl formátumának megfelelő sorrá.
     *
     * @param u az újság
     * @return a CSV sor (sorvége jel nélkül)
     */
    public static String csvSor(Ujsag u) {
        // 1. A közös mezők minden típusnál szerepelnek.
        String alap = u.tipus() + " - " + u.getMegnevezes() + " - " + u.getKiado() + " - " + u.getAr();

        // 2. A típusfüggő ötödik mező: mintaillesztéses instanceof (Java 16+).
        if (u instanceof Napilap n) {
            return alap + " - " + n.getHetiMegjelenes();
        } else if (u instanceof Hetilap h) {
            return alap + " - " + h.getHetNapja();
        }
        return alap;
    }

    /**
     * Ellenőrzi, hogy egy szöveges mező beírható-e a fájlba (nem tartalmaz elválasztót).
     *
     * @param ertek    a vizsgált szöveg
     * @param mezoNev  a mező neve a hibaüzenethez
     * @throws IllegalArgumentException ha a szöveg kötőjelet tartalmaz
     */
    public static void ellenorizCsvMezo(String ertek, String mezoNev) {
        if (ertek != null && ertek.contains(ELVALASZTO)) {
            throw new IllegalArgumentException("A(z) " + mezoNev
                    + " nem tartalmazhat kötőjelet, mert az a fájlban az elválasztó karakter!");
        }
    }

    /**
     * Hozzáfűz egy újságot a fájl végéhez (Feladat X.). Ha a fájl nem létezik, létrehozza.
     *
     * @param fajl a CSV fájl
     * @param u    a hozzáfűzendő újság
     * @throws IOException ha az írás nem sikerül
     */
    public static void hozzafuz(Path fajl, Ujsag u) throws IOException {
        // 1. Ha a fájl nem sortöréssel végződik, előbb egy sortörést írunk,
        //    különben az új adat az utolsó sor végére „ragadna”.
        String elotag = "";
        if (Files.exists(fajl) && Files.size(fajl) > 0) {
            String tartalom = Files.readString(fajl, StandardCharsets.UTF_8);
            if (!tartalom.endsWith("\n")) {
                elotag = System.lineSeparator();
            }
        }
        // 2. Írás APPEND módban: a meglévő tartalom megmarad.
        Files.writeString(fajl, elotag + csvSor(u) + System.lineSeparator(), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
