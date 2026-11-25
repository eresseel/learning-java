import java.util.Random;
import java.util.Scanner;

/**
 * Önálló (házi) feladat III.: vállalkozások előző évi havi nyereségének /
 * veszteségének tárolása és elemzése metódusokkal.
 * <p>
 * Adatszerkezet: kétdimenziós tömb, ahol a sor a vállalkozás, az oszlop a hónap
 * ({@code eredmenyek[vallalkozas][honap]}). Az értékek ezer forintban értendők,
 * a negatív szám veszteséget jelent.
 */
public class App {

    /** Hónapok száma egy évben. */
    static final int HONAPOK_SZAMA = 12;

    /** A hónapok nevei a kiíráshoz (index 0 = január). */
    static final String[] HONAPNEVEK = {
        "január", "február", "március", "április", "május", "június",
        "július", "augusztus", "szeptember", "október", "november", "december"
    };

    /** A bemenetet olvasó Scanner, minden metódus ezt használja. */
    static final Scanner SC = new Scanner(System.in);

    /**
     * A program belépési pontja: csak deklarációk és metódushívások.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        int vallalkozasokSzama = egeszBekerese("Vállalkozások száma: ", 1);
        int[][] eredmenyek = adatokBekerese(vallalkozasokSzama);
        haviEredmenyekListazasa(eredmenyek);
        veletlenHonapKiirasa(eredmenyek, new Random().nextInt(1, HONAPOK_SZAMA + 1));
        legjobbVallalkozasKiirasa(eredmenyek);
        vesztesegesHonapokKiirasa(eredmenyek);
    }

    /**
     * Bekér egy egész számot, amely legalább a megadott minimum; hibás
     * bemenetnél újra kérdez, a bemenet végén leállítja a programot.
     *
     * @param kerdes  a kiírandó kérdés
     * @param minimum a legkisebb elfogadható érték
     * @return a beolvasott szám
     */
    static int egeszBekerese(String kerdes, int minimum) {
        while (true) {
            System.out.print(kerdes);
            if (!SC.hasNext()) {
                System.out.println("\nA bemenet véget ért. A program leáll.");
                System.exit(1);
            }
            if (SC.hasNextInt()) {
                int szam = SC.nextInt();
                if (szam >= minimum) {
                    return szam;
                }
            } else {
                SC.next(); // a hibás tokent eldobjuk
            }
            System.out.println("Hibás érték, legalább " + minimum + " legyen és egész szám.");
        }
    }

    /**
     * Bekéri minden vállalkozás 12 havi eredményét.
     *
     * @param vallalkozasokSzama a vállalkozások száma (a mátrix sorainak száma)
     * @return a kitöltött [vállalkozás][hónap] mátrix
     */
    static int[][] adatokBekerese(int vallalkozasokSzama) {
        int[][] eredmenyek = new int[vallalkozasokSzama][HONAPOK_SZAMA];
        for (int v = 0; v < eredmenyek.length; v++) {
            System.out.println((v + 1) + ". vállalkozás havi eredményei (ezer Ft, veszteség negatív):");
            for (int h = 0; h < HONAPOK_SZAMA; h++) {
                // Integer.MIN_VALUE: bármilyen egész elfogadható (negatív is)
                eredmenyek[v][h] = egeszBekerese("  " + HONAPNEVEK[h] + ": ", Integer.MIN_VALUE);
            }
        }
        return eredmenyek;
    }

    /**
     * Kiszámolja egy vállalkozás éves összesített eredményét.
     *
     * @param haviEredmenyek egy vállalkozás 12 havi eredménye (a mátrix egy sora)
     * @return a 12 hónap összege
     */
    static int evesOsszeg(int[] haviEredmenyek) {
        int osszeg = 0;
        for (int eredmeny : haviEredmenyek) {
            osszeg += eredmeny;
        }
        return osszeg;
    }

    /**
     * Táblázatosan kilistázza a vállalkozások havi eredményeit és éves összesítésüket.
     *
     * @param eredmenyek a [vállalkozás][hónap] mátrix
     */
    static void haviEredmenyekListazasa(int[][] eredmenyek) {
        System.out.println("\nHavi eredmények (ezer Ft):");
        // Fejléc: a hónapnevek első 3 betűje
        System.out.printf("%-6s", "");
        for (String honap : HONAPNEVEK) {
            System.out.printf("%7s", honap.substring(0, 3));
        }
        System.out.printf("%9s%n", "Éves");
        for (int v = 0; v < eredmenyek.length; v++) {
            System.out.printf("%-6s", (v + 1) + ".");
            for (int eredmeny : eredmenyek[v]) {
                System.out.printf("%7d", eredmeny);
            }
            System.out.printf("%9d%n", evesOsszeg(eredmenyek[v]));
        }
    }

    /**
     * Kiírja minden vállalkozás eredményét egyetlen, adott hónapban.
     *
     * @param eredmenyek a [vállalkozás][hónap] mátrix
     * @param honap      a hónap sorszáma 1 és 12 között (nem index!)
     */
    static void veletlenHonapKiirasa(int[][] eredmenyek, int honap) {
        System.out.println("\nVéletlenszerűen kiválasztott hónap: " + honap + ". (" + HONAPNEVEK[honap - 1] + ")");
        for (int v = 0; v < eredmenyek.length; v++) {
            // a sorszámból 1-et levonva kapjuk az oszlopindexet
            System.out.println("  " + (v + 1) + ". vállalkozás: " + eredmenyek[v][honap - 1] + " ezer Ft");
        }
    }

    /**
     * Megkeresi és kiírja a legjobb éves eredményt elérő vállalkozást.
     * Holtverseny esetén az elsőt (legkisebb sorszámút) írja ki.
     *
     * @param eredmenyek a [vállalkozás][hónap] mátrix
     */
    static void legjobbVallalkozasKiirasa(int[][] eredmenyek) {
        int legjobbIndex = 0;
        for (int v = 1; v < eredmenyek.length; v++) {
            if (evesOsszeg(eredmenyek[v]) > evesOsszeg(eredmenyek[legjobbIndex])) {
                legjobbIndex = v;
            }
        }
        System.out.println("\nA legjobb éves eredményt a(z) " + (legjobbIndex + 1)
                + ". vállalkozás érte el: " + evesOsszeg(eredmenyek[legjobbIndex]) + " ezer Ft");
    }

    /**
     * Megszámolja egy vállalkozás veszteséges (negatív eredményű) hónapjait.
     *
     * @param haviEredmenyek egy vállalkozás 12 havi eredménye
     * @return a veszteséges hónapok száma
     */
    static int vesztesegesHonapok(int[] haviEredmenyek) {
        int darab = 0;
        for (int eredmeny : haviEredmenyek) {
            if (eredmeny < 0) {
                darab++;
            }
        }
        return darab;
    }

    /**
     * Vállalkozásonként kiírja a veszteséges hónapok számát.
     *
     * @param eredmenyek a [vállalkozás][hónap] mátrix
     */
    static void vesztesegesHonapokKiirasa(int[][] eredmenyek) {
        System.out.println("\nVeszteséges hónapok száma:");
        for (int v = 0; v < eredmenyek.length; v++) {
            System.out.println("  " + (v + 1) + ". vállalkozás: " + vesztesegesHonapok(eredmenyek[v]));
        }
    }
}
