import java.util.Scanner;

/**
 * Felhasználói azonosító generálása a felhasználó nevéből.
 * <p>
 * Az azonosító csupa kisbetűs, és három részből áll:
 * a vezetéknév első 3 betűje + a keresztnév első 3 betűje + a teljes névben
 * található 'e' betűk száma (kis- és nagybetű egyaránt számít).
 * Példa: Straub Ingrid → {@code string0}.
 */
public class App {

    /** Ennyi betűt veszünk a vezeték- és a keresztnévből. */
    static final int ELOTAG_HOSSZ = 3;

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Nevek bekérése; a trim() levágja a véletlen szóközöket.
        System.out.print("Vezetéknév: ");
        String vezeteknev = sc.hasNextLine() ? sc.nextLine().trim() : "";
        System.out.print("Keresztnév: ");
        String keresztnev = sc.hasNextLine() ? sc.nextLine().trim() : "";

        if (vezeteknev.isEmpty() || keresztnev.isEmpty()) {
            System.out.println("A vezeték- és a keresztnév sem lehet üres.");
            return;
        }

        // 2. Az azonosító előállítása, majd kiírás.
        String teljesNev = vezeteknev + " " + keresztnev;
        String azonosito = azonositoGeneralasa(vezeteknev, keresztnev);
        System.out.println("Teljes név: " + teljesNev);
        System.out.println("Azonosító:  " + azonosito);

        sc.close();
    }

    /**
     * Előállítja a felhasználói azonosítót.
     *
     * @param vezeteknev a felhasználó vezetékneve
     * @param keresztnev a felhasználó keresztneve
     * @return a csupa kisbetűs azonosító
     */
    static String azonositoGeneralasa(String vezeteknev, String keresztnev) {
        // substring(0, n): az első n karakter. A Math.min védi ki, hogy 3 betűnél
        // rövidebb névnél (pl. "Ó") StringIndexOutOfBoundsException keletkezzen.
        String eleje = vezeteknev.substring(0, Math.min(ELOTAG_HOSSZ, vezeteknev.length()));
        String kozepe = keresztnev.substring(0, Math.min(ELOTAG_HOSSZ, keresztnev.length()));
        int eBetukSzama = karakterDarab(vezeteknev + keresztnev, 'e');

        // A szám automatikusan szöveggé alakul az összefűzésnél,
        // a toLowerCase() pedig az egészet kisbetűssé teszi.
        return (eleje + kozepe + eBetukSzama).toLowerCase();
    }

    /**
     * Megszámolja, hányszor fordul elő egy betű a szövegben, kis- és nagybetűtől
     * függetlenül. (Az ékezetes 'é' külön betű, azt nem számolja.)
     *
     * @param szoveg a vizsgált szöveg
     * @param betu   a keresett betű
     * @return az előfordulások száma
     */
    static int karakterDarab(String szoveg, char betu) {
        int darab = 0;
        // A szöveget és a keresett betűt is kisbetűssé alakítjuk, így elég
        // egyetlen összehasonlítás. A charAt(i) a szöveg i-edik karaktere.
        String kisbetus = szoveg.toLowerCase();
        char keresett = Character.toLowerCase(betu);
        for (int i = 0; i < kisbetus.length(); i++) {
            if (kisbetus.charAt(i) == keresett) {
                darab++;
            }
        }
        return darab;
    }
}
