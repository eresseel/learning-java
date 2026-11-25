import java.util.Scanner;

/**
 * Önálló (házi) feladat II.A: tíz keresztnév bekérése, nagybetűs tárolása
 * vektorban, majd lekérdezések: minden második elem, a leghosszabb név, és
 * a legtöbb 'S' betűt tartalmazó név.
 */
public class App {

    /** A bekérendő nevek száma. */
    static final int NEVEK_SZAMA = 10;

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] nevek = new String[NEVEK_SZAMA];

        // 1. Bekérés. Üres sort nem fogadunk el; a nevet nagybetűsen tároljuk.
        int i = 0;
        while (i < nevek.length) {
            System.out.print((i + 1) + ". keresztnév: ");
            if (!sc.hasNextLine()) {
                System.out.println("\nA bemenet véget ért, nincs elég név. A program leáll.");
                return;
            }
            String nev = sc.nextLine().trim();
            if (nev.isEmpty()) {
                System.out.println("A név nem lehet üres.");
                continue; // i nem nő, ugyanazt a sorszámot kérdezzük újra
            }
            nevek[i] = nev.toUpperCase();
            i++;
        }

        // 2. Minden második elem: a 2., 4., 6. ... név, azaz az 1, 3, 5 ... indexek.
        //    A ciklusváltozót kettesével léptetjük (j += 2).
        System.out.print("\nMinden második név: ");
        for (int j = 1; j < nevek.length; j += 2) {
            System.out.print(nevek[j] + " ");
        }
        System.out.println();

        // 3. Leghosszabb név: maximumkiválasztás a length() alapján. Az indexet
        //    jegyezzük meg, nem a hosszt, így utána a nevet is ki tudjuk írni.
        //    Egyenlő hossz esetén az első ilyen név marad (szigorú > reláció).
        int leghosszabbIndex = 0;
        for (int j = 1; j < nevek.length; j++) {
            if (nevek[j].length() > nevek[leghosszabbIndex].length()) {
                leghosszabbIndex = j;
            }
        }
        System.out.println("Leghosszabb név: " + nevek[leghosszabbIndex]
                + " (" + nevek[leghosszabbIndex].length() + " betű)");

        // 4. Legtöbb 'S' betű: a nevek nagybetűsek, ezért 'S'-t keresünk.
        int legtobbSIndex = 0;
        int legtobbS = sBetukSzama(nevek[0]);
        for (int j = 1; j < nevek.length; j++) {
            int darab = sBetukSzama(nevek[j]);
            if (darab > legtobbS) {
                legtobbS = darab;
                legtobbSIndex = j;
            }
        }
        if (legtobbS == 0) {
            System.out.println("Egyik névben sincs 's' betű.");
        } else {
            System.out.println("A legtöbb 's' betű (" + legtobbS + " db) ebben a névben van: "
                    + nevek[legtobbSIndex]);
        }

        sc.close();
    }

    /**
     * Megszámolja az 'S' betűket egy (nagybetűs) névben.
     *
     * @param nev a vizsgált név
     * @return az 'S' betűk száma
     */
    static int sBetukSzama(String nev) {
        int darab = 0;
        for (char c : nev.toCharArray()) { // a szöveg karaktertömbként bejárható
            if (c == 'S') {
                darab++;
            }
        }
        return darab;
    }
}
