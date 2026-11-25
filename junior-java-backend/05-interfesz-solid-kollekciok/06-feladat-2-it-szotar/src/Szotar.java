import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Kétirányú informatikai szótár, amely a szavakat két Map-ben tárolja:
 * angol → magyar és magyar → angol irányban.
 */
public class Szotar {

    /** Angol kifejezés (kisbetűsen) → magyar jelentés. */
    private final Map<String, String> angolMagyar = new TreeMap<>();
    /** Magyar kifejezés (kisbetűsen) → angol jelentés. */
    private final Map<String, String> magyarAngol = new TreeMap<>();

    /**
     * Beolvassa a szótárat egy "angol;magyar" formátumú, fejléccel kezdődő CSV fájlból.
     *
     * @param fajlnev a CSV fájl neve
     * @return a beolvasott szópárok száma
     * @throws IOException ha a fájl nem olvasható
     */
    public int betolt(String fajlnev) throws IOException {
        int db = 0;
        try (BufferedReader olvaso = Files.newBufferedReader(Path.of(fajlnev), StandardCharsets.UTF_8)) {
            // 1. A fejlécsort átugorjuk.
            olvaso.readLine();
            String sor;
            // 2. Soronként olvasunk, amíg van adat.
            while ((sor = olvaso.readLine()) != null) {
                String[] reszek = sor.split(";");
                // 3. Hibás vagy üres sort kihagyunk.
                if (reszek.length != 2 || reszek[0].isBlank() || reszek[1].isBlank()) {
                    continue;
                }
                hozzaad(reszek[0].trim(), reszek[1].trim());
                db++;
            }
        }
        return db;
    }

    /**
     * Felvesz egy szópárt mindkét irányba. A kulcsot kisbetűsítjük, így a
     * keresés kis- és nagybetűtől független. Ha egy kulcs már létezik
     * (pl. két angol szó ugyanazt jelenti), a jelentéseket összefűzzük.
     *
     * @param angol  az angol kifejezés
     * @param magyar a magyar jelentés
     */
    private void hozzaad(String angol, String magyar) {
        // A merge: ha nincs még ilyen kulcs, beteszi; ha van, a két értéket összefűzi.
        angolMagyar.merge(angol.toLowerCase(), magyar, (regi, uj) -> regi + ", " + uj);
        magyarAngol.merge(magyar.toLowerCase(), angol, (regi, uj) -> regi + ", " + uj);
    }

    /**
     * Fordítás a megadott irányba.
     *
     * @param kifejezes    a keresett kifejezés
     * @param angolrolMagyarra true: angol → magyar, false: magyar → angol
     * @return a fordítás, vagy null, ha nincs a szótárban
     */
    public String fordit(String kifejezes, boolean angolrolMagyarra) {
        Map<String, String> irany = angolrolMagyarra ? angolMagyar : magyarAngol;
        return irany.get(kifejezes.trim().toLowerCase());
    }

    /**
     * Javaslatok: a megadott szövegrésszel kezdődő kulcsok.
     *
     * @param eleje            a kifejezés eleje
     * @param angolrolMagyarra a keresés iránya
     * @return a találó kulcsok (legfeljebb 5)
     */
    public List<String> javaslatok(String eleje, boolean angolrolMagyarra) {
        Map<String, String> irany = angolrolMagyarra ? angolMagyar : magyarAngol;
        List<String> talalatok = new ArrayList<>();
        String keresett = eleje.trim().toLowerCase();
        for (String kulcs : irany.keySet()) {
            if (!keresett.isEmpty() && kulcs.startsWith(keresett) && talalatok.size() < 5) {
                talalatok.add(kulcs);
            }
        }
        return talalatok;
    }

    /** @return a szótár mérete (angol kulcsok száma) */
    public int meret() {
        return angolMagyar.size();
    }
}
