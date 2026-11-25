/**
 * 4. lecke, Feladat I: vegyeskereskedés (ABC) alapszintű raktárkezelése.
 * Létrehoz egy-egy objektumot minden terméktípusból, és bemutatja
 * az érvénytelen adatok kivételkezelését.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Raktar raktar = new Raktar(10);

        // 1. Egy-egy objektum minden típusból (a feladat minimuma), plusz még néhány.
        raktar.felvesz(new Tejtermek("Trappista sajt", "TEJ-001", 4290, 12, 45.0));
        raktar.felvesz(new Tejtermek("Félzsíros tej 1 l", "TEJ-002", 399, 40, 2.8));
        raktar.felvesz(new Pekaru("Teljes kiőrlésű kenyér", "PEK-001", 899, 15, true));
        raktar.felvesz(new Pekaru("Kifli", "PEK-002", 49, 120, false));
        raktar.felvesz(new Termek("Mosogatószer", "ALT-001", 790, 25));

        System.out.println("=== Raktárkészlet ===");
        raktar.listaz();
        System.out.println("Összérték: " + raktar.osszErtek() + " Ft");

        // 2. Érvénytelen adatok: a konstruktorok kivételt dobnak, mi elkapjuk és kiírjuk.
        System.out.println();
        System.out.println("=== Érvénytelen adatok kezelése ===");
        probalFelvenni(raktar, () -> new Tejtermek("Tejföl", "TEJ-003", 459, 10, 120));
        probalFelvenni(raktar, () -> new Pekaru("Zsemle", "PEK-003", -60, 50, false));
        probalFelvenni(raktar, () -> new Termek("", "ALT-002", 100, 1));
        probalFelvenni(raktar, () -> new Termek("Gyufa", "ALT-003", 50, -3));
        probalFelvenni(raktar, () -> new Termek("Másik kifli", "PEK-002", 55, 10));

        // 3. Eladás a készletből: a túl nagy kiadást is kivétel jelzi.
        System.out.println();
        System.out.println("=== Eladás ===");
        Termek kifli = raktar.keres("PEK-002");
        try {
            kifli.kiad(20);
            System.out.println("20 db kifli eladva, maradt: " + kifli.getDarabszam());
            kifli.kiad(500);
            System.out.println("ez a sor nem fut le");
        } catch (IllegalArgumentException e) {
            System.out.println("Hiba: " + e.getMessage());
        }
    }

    /**
     * Egy termék létrehozását és felvételét próbálja meg, és a hibát kiírja.
     * A létrehozást egy lambda (TermekKeszito) végzi, hogy a konstruktor
     * kivétele is a try blokkon belül keletkezzen.
     *
     * @param raktar  a raktár
     * @param keszito a terméket létrehozó kód
     */
    private static void probalFelvenni(Raktar raktar, TermekKeszito keszito) {
        try {
            Termek t = keszito.letrehoz();
            raktar.felvesz(t);
            System.out.println("Felvéve: " + t.getMegnevezes());
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Multi-catch: két kivételtípus egy ágban.
            System.out.println("Elutasítva: " + e.getMessage());
        }
    }

    /**
     * Egyszerű funkcionális interfész egy termék létrehozásához
     * (az interfészeket az 5. lecke tárgyalja részletesen).
     */
    @FunctionalInterface
    private interface TermekKeszito {
        /** @return az új termék */
        Termek letrehoz();
    }
}
