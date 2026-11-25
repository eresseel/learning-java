import java.util.Arrays;

/**
 * A diasoron felsorolt fontosabb beépített interfészek bemutatása:
 * Comparable, Cloneable, Closeable, Iterable.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Termek[] termekek = {
                new Termek("Kenyér", 899),
                new Termek("Tej", 399),
                new Termek("Sajt", 4290),
                new Termek("Alma", 590)
        };

        // 1. Comparable: az Arrays.sort a compareTo() alapján rendez.
        System.out.println("=== Comparable ===");
        Arrays.sort(termekek);
        System.out.println("Ár szerint rendezve: " + Arrays.toString(termekek));
        System.out.println("Kenyér vs Tej: " + termekek[2].compareTo(termekek[0]) + " (pozitív = drágább)");

        // 2. Cloneable: a másolat önálló objektum, módosítása nem hat az eredetire.
        System.out.println();
        System.out.println("=== Cloneable ===");
        Termek eredeti = termekek[0];
        Termek masolat = eredeti.clone();
        masolat.setAr(450);
        System.out.println("Eredeti: " + eredeti + ", másolat: " + masolat
                + ", ugyanaz az objektum? " + (eredeti == masolat));

        // 3. Iterable: a saját Polc osztály foreach-csel bejárható.
        System.out.println();
        System.out.println("=== Iterable ===");
        Polc polc = new Polc(3);
        for (Termek t : termekek) {
            polc.feltesz(t);   // csak az első 3 fér fel
        }
        for (Termek t : polc) {
            System.out.println("A polcon: " + t);
        }

        // 4. Closeable: try-with-resources – a close() automatikusan lefut a blokk végén.
        System.out.println();
        System.out.println("=== Closeable ===");
        try (Naplo naplo = new Naplo("bolt")) {
            naplo.ir("Nyitás");
            naplo.ir("Árazás kész");
        }
        System.out.println("A try blokk után a napló már le van zárva.");
    }
}
