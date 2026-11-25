import java.util.Map;
import java.util.TreeMap;

/**
 * Egy zöldséges árlistája (Ft/kg), Map adatszerkezetben: termék neve → kilogrammonkénti ár.
 * A TreeMap a kulcsok (terméknevek) szerint rendezve tárol, így a lista ábécérendben jelenik meg.
 */
public class Arlista {

    private final Map<String, Integer> arak = new TreeMap<>();

    /**
     * Létrehozza az árlistát a feladatban megadott kezdő adatokkal.
     */
    public Arlista() {
        arak.put("eper", 2900);
        arak.put("alma", 590);
        arak.put("burgonya", 350);
        arak.put("cukkini", 890);
        arak.put("gomba", 750);
        arak.put("kivi", 850);
    }

    /**
     * Új termék felvétele.
     *
     * @param termek a termék neve
     * @param ar     a kilogrammonkénti ár
     * @return false, ha már van ilyen termék (ekkor nem írjuk felül)
     */
    public boolean felvesz(String termek, int ar) {
        // putIfAbsent: csak akkor tesz be, ha a kulcs még nem létezik; ilyenkor null-t ad vissza.
        return arak.putIfAbsent(normal(termek), ar) == null;
    }

    /**
     * Termék törlése.
     *
     * @param termek a termék neve
     * @return true, ha volt ilyen termék és törlődött
     */
    public boolean torol(String termek) {
        return arak.remove(normal(termek)) != null;
    }

    /**
     * Árváltozás egy meglévő terméknél.
     *
     * @param termek a termék neve
     * @param ujAr   az új kilogrammonkénti ár
     * @return true, ha a termék létezett és az ára módosult
     */
    public boolean arValtozas(String termek, int ujAr) {
        // replace: csak meglévő kulcsnál módosít, és visszaadja a régi értéket (vagy null-t).
        return arak.replace(normal(termek), ujAr) != null;
    }

    /**
     * @param termek a termék neve
     * @return a kilogrammonkénti ár, vagy null, ha nincs ilyen termék
     */
    public Integer ar(String termek) {
        return arak.get(normal(termek));
    }

    /** Kiírja a terméklistát (termék és ár). */
    public void listaz() {
        for (Map.Entry<String, Integer> e : arak.entrySet()) {
            System.out.printf("  %-12s %6d Ft/kg%n", e.getKey(), e.getValue());
        }
    }

    /**
     * Egységes kulcsformátum: szóközök nélkül, kisbetűvel.
     *
     * @param termek a felhasználó által beírt név
     * @return a kulcsként használt név
     */
    private static String normal(String termek) {
        return termek.trim().toLowerCase();
    }
}
