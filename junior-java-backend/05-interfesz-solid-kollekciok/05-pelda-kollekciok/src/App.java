import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A Java Collections Framework alapjai: csomagoló osztályok és autoboxing,
 * valamint a List, Set és Map interfész fontosabb megvalósításai.
 * <p>
 * Mindenhol az interfész a változó típusa (List, Set, Map), a konkrét osztály
 * csak a new után szerepel – így a megvalósítás egy helyen cserélhető.
 */
public class App {

    /**
     * Belépési pont: sorban lefuttatja a bemutató részeket.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        csomagoloOsztalyok();
        listak();
        halmazok();
        mapek();
    }

    /** Csomagoló osztályok, autoboxing és unboxing. */
    private static void csomagoloOsztalyok() {
        System.out.println("=== Csomagoló osztályok ===");
        // 1. Autoboxing: a primitív int automatikusan Integer objektummá alakul.
        Integer szam = 10;
        // 2. Unboxing: az Integer automatikusan primitív int-té alakul.
        int szam2 = szam;
        System.out.println("Integer szam = " + szam + ", int szam2 = " + szam2);

        // 3. A kollekciók csak objektumot tárolnak: List<int> nem létezik, List<Integer> igen.
        List<Integer> lista = new ArrayList<>();
        lista.add(5);            // autoboxing: int → Integer
        int elso = lista.get(0); // unboxing: Integer → int
        System.out.println("Listából kivett elem + 1 = " + (elso + 1));

        // 4. A csomagoló osztályok hasznos statikus metódusai.
        System.out.println("Integer.parseInt(\"42\") = " + Integer.parseInt("42")
                + ", Double.valueOf(\"3.5\") = " + Double.valueOf("3.5")
                + ", Character.isDigit('7') = " + Character.isDigit('7')
                + ", Integer.MAX_VALUE = " + Integer.MAX_VALUE);

        // 5. Csapda: objektumokat equals()-szal hasonlítunk, nem ==-vel!
        Integer a = 1000;
        Integer b = 1000;
        System.out.println("a == b: " + (a == b) + ", a.equals(b): " + a.equals(b));
    }

    /** List: rendezett sorozat, lehet benne duplikáció és null is. */
    private static void listak() {
        System.out.println();
        System.out.println("=== List (ArrayList, LinkedList) ===");
        // ArrayList: tömb alapú, gyors indexelés.
        List<String> varosok = new ArrayList<>();
        varosok.add("Szeged");
        varosok.add("Budapest");
        varosok.add("Debrecen");
        varosok.add("Budapest");     // duplikáció megengedett
        varosok.add(1, "Pécs");       // beszúrás index alapján
        System.out.println("ArrayList: " + varosok + ", méret: " + varosok.size());
        System.out.println("get(2): " + varosok.get(2) + ", indexOf(Budapest): " + varosok.indexOf("Budapest")
                + ", contains(Győr): " + varosok.contains("Győr"));
        varosok.remove("Budapest");   // az első előfordulást törli
        Collections.sort(varosok);    // rendezés (a String Comparable)
        System.out.println("Törlés és rendezés után: " + varosok);

        // LinkedList: láncolt lista, gyors beszúrás/törlés az elején és a végén.
        LinkedList<Integer> sor = new LinkedList<>(List.of(2, 3));
        sor.addFirst(1);
        sor.addLast(4);
        System.out.println("LinkedList: " + sor + ", első: " + sor.getFirst() + ", utolsó: " + sor.getLast());
        sor.removeFirst();
        System.out.println("removeFirst után: " + sor);

        // Bejárás foreach-csel (a List Iterable).
        int osszeg = 0;
        for (int x : sor) {
            osszeg += x;
        }
        System.out.println("Összeg: " + osszeg);
    }

    /** Set: halmaz – egy elem csak egyszer szerepelhet. */
    private static void halmazok() {
        System.out.println();
        System.out.println("=== Set (HashSet, LinkedHashSet, TreeSet) ===");
        String[] szavak = {"körte", "alma", "szilva", "alma", "barack", "körte"};

        // A három megvalósítás ugyanazt a halmazt tárolja, csak a bejárás sorrendje más.
        Set<String> hash = new HashSet<>();
        Set<String> linked = new LinkedHashSet<>();
        Set<String> tree = new TreeSet<>();
        for (String sz : szavak) {
            hash.add(sz);
            linked.add(sz);
            // Az add() visszatérési értéke megmondja, bekerült-e (ismétlődésnél false).
            if (!tree.add(sz)) {
                System.out.println("Már benne volt: " + sz);
            }
        }
        System.out.println("HashSet (nincs garantált sorrend): " + hash);
        System.out.println("LinkedHashSet (beszúrási sorrend):  " + linked);
        System.out.println("TreeSet (rendezett):                " + tree);

        // Halmazműveletek: metszet és unió.
        Set<String> masik = new TreeSet<>(List.of("alma", "kiwi", "szilva"));
        Set<String> metszet = new TreeSet<>(tree);
        metszet.retainAll(masik);
        Set<String> unio = new TreeSet<>(tree);
        unio.addAll(masik);
        System.out.println("Metszet: " + metszet + ", unió: " + unio);
    }

    /** Map: kulcs–érték párok, a kulcs egyedi. */
    private static void mapek() {
        System.out.println();
        System.out.println("=== Map (Hashtable, HashMap, TreeMap) ===");
        // A Map kétszeresen generikus: külön típusa van a kulcsnak és az értéknek.
        Map<String, Integer> nepesseg = new HashMap<>();
        nepesseg.put("Debrecen", 200000);
        nepesseg.put("Szeged", 160000);
        nepesseg.put("Győr", 130000);
        nepesseg.put("Szeged", 159000);   // létező kulcs: az érték felülíródik
        System.out.println("HashMap: " + nepesseg);
        System.out.println("get(Szeged): " + nepesseg.get("Szeged")
                + ", containsKey(Pécs): " + nepesseg.containsKey("Pécs")
                + ", getOrDefault(Pécs, 0): " + nepesseg.getOrDefault("Pécs", 0));

        // TreeMap: kulcs szerint rendezett.
        Map<String, Integer> rendezett = new TreeMap<>(nepesseg);
        System.out.println("TreeMap: " + rendezett);

        // Hashtable: régebbi, szálbiztos megvalósítás; null kulcsot/értéket nem enged.
        Map<String, Integer> tabla = new Hashtable<>(nepesseg);
        tabla.remove("Győr");
        System.out.println("Hashtable Győr nélkül: " + tabla);

        // Bejárás kulcs–érték párokon (entrySet).
        for (Map.Entry<String, Integer> e : rendezett.entrySet()) {
            System.out.println("  " + e.getKey() + " -> " + e.getValue() + " fő");
        }
    }
}
