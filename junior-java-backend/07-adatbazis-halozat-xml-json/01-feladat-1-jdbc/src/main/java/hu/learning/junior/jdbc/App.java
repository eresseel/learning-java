package hu.learning.junior.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Feladat I. – konzolos menü a {@code dml} adatbázis dept/emp tábláihoz.
 *
 * <ul>
 *   <li>A – dolgozók, akiknek a fizetése kisebb a megadott összegnél</li>
 *   <li>B – egy vezető beosztottjai</li>
 *   <li>C – az ACCOUNTING osztály dolgozói</li>
 *   <li>D – mindenki 10%-os fizetésemelést kap (módosítja az adatbázist!)</li>
 * </ul>
 *
 * <p>A program pipe-olt bemenettel is használható; a bemenet végén (EOF)
 * rendben kilép.</p>
 */
public class App {

    /** A konzolbemenet; egyetlen példány a teljes programra. */
    private static final Scanner BE = new Scanner(System.in);

    /**
     * Belépési pont: kapcsolódik az adatbázishoz, majd menüt futtat.
     *
     * @param args nem használt
     */
    public static void main(String[] args) {
        // 1. Kapcsolat nyitása try-with-resources-szal: a program végén
        //    (vagy kivétel esetén is) automatikusan lezárul.
        try (Connection kapcsolat = AdatbazisKapcsolat.kapcsolodas()) {
            EmpDao dao = new EmpDao(kapcsolat);
            System.out.println("Kapcsolódva: " + kapcsolat.getMetaData().getURL());
            // 2. Menüciklus, amíg a felhasználó ki nem lép.
            menu(dao);
        } catch (SQLException e) {
            // 3. Gyakori ok: nem fut a konténer (docker compose up -d).
            System.err.println("Adatbázis-hiba: " + e.getMessage());
            System.err.println("Fut az adatbázis? Indítás: docker compose up -d (a lecke mappájában)");
            System.exit(1);
        }
    }

    /**
     * A menü kiírása és a választott művelet végrehajtása ciklusban.
     *
     * @param dao az adatelérési objektum
     * @throws SQLException adatbázis-hiba esetén
     */
    private static void menu(EmpDao dao) throws SQLException {
        while (true) {
            System.out.println();
            System.out.println("A) Fizetés kisebb, mint ...   B) Vezető beosztottjai");
            System.out.println("C) ACCOUNTING dolgozói       D) 10% fizetésemelés   0) Kilépés");
            String valasztas = sor("Választás: ");
            // null = elfogyott a bemenet (EOF) -> kilépés.
            if (valasztas == null || valasztas.equals("0")) {
                System.out.println("Viszlát!");
                return;
            }
            // switch kifejezés a menüpontokra (kis- és nagybetű is jó).
            switch (valasztas.toUpperCase()) {
                case "A" -> feladatA(dao);
                case "B" -> feladatB(dao);
                case "C" -> kiir("ACCOUNTING osztály dolgozói", dao.osztalyDolgozoi("ACCOUNTING"));
                case "D" -> feladatD(dao);
                default -> System.out.println("Ismeretlen menüpont: " + valasztas);
            }
        }
    }

    /**
     * A. feladat: összeg bekérése, majd a nála kisebb fizetésűek listázása.
     *
     * @param dao az adatelérési objektum
     * @throws SQLException adatbázis-hiba esetén
     */
    private static void feladatA(EmpDao dao) throws SQLException {
        String szoveg = sor("Összeg: ");
        if (szoveg == null) {
            return;
        }
        try {
            // A BigDecimal pontosan kezeli a tizedes összegeket (pl. 1250.50).
            BigDecimal osszeg = new BigDecimal(szoveg.replace(',', '.'));
            kiir("Fizetés < " + osszeg, dao.fizetesKevesebbMint(osszeg));
        } catch (NumberFormatException e) {
            System.out.println("Nem szám: " + szoveg);
        }
    }

    /**
     * B. feladat: vezető azonosítójának bekérése, majd a beosztottak listázása.
     *
     * @param dao az adatelérési objektum
     * @throws SQLException adatbázis-hiba esetén
     */
    private static void feladatB(EmpDao dao) throws SQLException {
        String szoveg = sor("Vezető azonosítója (EMPNO, pl. 7698): ");
        if (szoveg == null) {
            return;
        }
        try {
            int empno = Integer.parseInt(szoveg);
            String vezeto = dao.nevAzonositoAlapjan(empno);
            if (vezeto == null) {
                System.out.println("Nincs ilyen azonosítójú dolgozó: " + empno);
                return;
            }
            kiir(vezeto + " (" + empno + ") beosztottjai", dao.beosztottak(empno));
        } catch (NumberFormatException e) {
            System.out.println("Nem egész szám: " + szoveg);
        }
    }

    /**
     * D. feladat: megerősítés után 10%-os emelés egy tranzakcióban.
     *
     * @param dao az adatelérési objektum
     * @throws SQLException adatbázis-hiba esetén
     */
    private static void feladatD(EmpDao dao) throws SQLException {
        String valasz = sor("FIGYELEM: ez módosítja az adatbázist! Biztosan? (i/n): ");
        if (valasz != null && valasz.equalsIgnoreCase("i")) {
            int db = dao.fizetesEmeles(BigDecimal.TEN);
            System.out.println(db + " dolgozó fizetése emelkedett 10%-kal.");
        } else {
            System.out.println("Megszakítva, nem történt módosítás.");
        }
    }

    /**
     * Táblázatszerűen kiírja a dolgozók listáját.
     *
     * @param cim      a lista címe
     * @param dolgozok a kiírandó dolgozók
     */
    private static void kiir(String cim, List<Dolgozo> dolgozok) {
        System.out.println("--- " + cim + " (" + dolgozok.size() + " fő) ---");
        for (Dolgozo d : dolgozok) {
            // printf: igazított oszlopok (%-10s = balra igazított, 10 széles szöveg).
            System.out.printf("%5d  %-10s %-10s %10s%n", d.empno(), d.ename(), d.job(), d.sal());
        }
    }

    /**
     * Kiír egy kérdést és beolvas egy sort.
     *
     * @param kerdes a kiírandó szöveg
     * @return a beolvasott, levágott sor, vagy {@code null}, ha elfogyott a bemenet
     */
    private static String sor(String kerdes) {
        System.out.print(kerdes);
        if (!BE.hasNextLine()) {
            System.out.println();
            return null;
        }
        return BE.nextLine().strip();
    }
}
