package hu.learning.junior.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object): az {@code emp}/{@code dept} táblák SQL
 * műveleteit egy helyre gyűjti, így a {@link App} nem tartalmaz SQL-t.
 *
 * <p>Minden lekérdezés {@link PreparedStatement}-tel készül: a felhasználótól
 * kapott értékek {@code ?} helyőrzőkbe kerülnek, így nincs SQL injection
 * veszély, és a driver gondoskodik a típuskonverzióról.</p>
 */
public class EmpDao {

    /** Az alap SELECT, amit a lekérdezések kiegészítenek WHERE feltétellel. */
    private static final String ALAP_SELECT =
            "SELECT e.EMPNO, e.ENAME, e.JOB, e.SAL, e.DEPTNO FROM emp e ";

    /** A használt (nyitott) adatbázis-kapcsolat; a lezárása a hívó dolga. */
    private final Connection kapcsolat;

    /**
     * @param kapcsolat nyitott adatbázis-kapcsolat
     */
    public EmpDao(Connection kapcsolat) {
        this.kapcsolat = kapcsolat;
    }

    /**
     * A. feladat: azok a dolgozók, akiknek a fizetése kisebb a megadott összegnél.
     *
     * @param osszeg a felső korlát (nem inkluzív)
     * @return a dolgozók fizetés szerint növekvő sorrendben
     * @throws SQLException adatbázis-hiba esetén
     */
    public List<Dolgozo> fizetesKevesebbMint(BigDecimal osszeg) throws SQLException {
        String sql = ALAP_SELECT + "WHERE e.SAL < ? ORDER BY e.SAL, e.ENAME";
        // try-with-resources: a PreparedStatement automatikusan lezárul.
        try (PreparedStatement ps = kapcsolat.prepareStatement(sql)) {
            // Az első (és egyetlen) ? helyére a felhasználó által megadott összeg kerül.
            ps.setBigDecimal(1, osszeg);
            return lista(ps);
        }
    }

    /**
     * B. feladat: a megadott azonosítójú vezető közvetlen beosztottjai.
     *
     * @param vezetoEmpno a vezető EMPNO-ja (az emp.MGR oszlopban szerepel)
     * @return a beosztottak név szerint rendezve (üres lista, ha nincs ilyen)
     * @throws SQLException adatbázis-hiba esetén
     */
    public List<Dolgozo> beosztottak(int vezetoEmpno) throws SQLException {
        String sql = ALAP_SELECT + "WHERE e.MGR = ? ORDER BY e.ENAME";
        try (PreparedStatement ps = kapcsolat.prepareStatement(sql)) {
            ps.setInt(1, vezetoEmpno);
            return lista(ps);
        }
    }

    /**
     * Megadja egy dolgozó nevét az azonosítója alapján (a B. feladat
     * kiírásához: kinek a beosztottait látjuk).
     *
     * @param empno a dolgozó azonosítója
     * @return a név, vagy {@code null}, ha nincs ilyen dolgozó
     * @throws SQLException adatbázis-hiba esetén
     */
    public String nevAzonositoAlapjan(int empno) throws SQLException {
        try (PreparedStatement ps = kapcsolat.prepareStatement("SELECT ENAME FROM emp WHERE EMPNO = ?")) {
            ps.setInt(1, empno);
            // A ResultSet is erőforrás, ezért ezt is try-with-resources zárja le.
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("ENAME") : null;
            }
        }
    }

    /**
     * C. feladat: a megadott nevű osztály dolgozói (JOIN a dept táblával).
     *
     * @param osztalyNev az osztály neve, pl. {@code ACCOUNTING}
     * @return az osztály dolgozói név szerint rendezve
     * @throws SQLException adatbázis-hiba esetén
     */
    public List<Dolgozo> osztalyDolgozoi(String osztalyNev) throws SQLException {
        String sql = ALAP_SELECT + "JOIN dept d ON d.DEPTNO = e.DEPTNO WHERE d.DNAME = ? ORDER BY e.ENAME";
        try (PreparedStatement ps = kapcsolat.prepareStatement(sql)) {
            ps.setString(1, osztalyNev);
            return lista(ps);
        }
    }

    /**
     * D. feladat: mindenki fizetését megemeli a megadott százalékkal,
     * EGY tranzakcióban: vagy minden sor módosul, vagy egyik sem.
     *
     * <p>FIGYELEM: ez valóban módosítja az adatbázist! Visszaállítás:
     * a lecke README-jében leírt módon az init script újrafuttatásával.</p>
     *
     * @param szazalek az emelés mértéke százalékban (pl. 10)
     * @return a módosított sorok száma
     * @throws SQLException adatbázis-hiba esetén (ekkor visszagörgetünk)
     */
    public int fizetesEmeles(BigDecimal szazalek) throws SQLException {
        // 1. Szorzó kiszámítása: 10% -> 1.10
        BigDecimal szorzo = BigDecimal.ONE.add(szazalek.movePointLeft(2));
        // 2. Az automatikus commit kikapcsolása: innentől mi döntjük el,
        //    mikor véglegesítünk (commit) vagy vonunk vissza (rollback).
        boolean eredetiAutoCommit = kapcsolat.getAutoCommit();
        kapcsolat.setAutoCommit(false);
        try (PreparedStatement ps = kapcsolat.prepareStatement("UPDATE emp SET SAL = ROUND(SAL * ?, 2)")) {
            ps.setBigDecimal(1, szorzo);
            // 3. executeUpdate: módosító utasítás, a módosított sorok számát adja vissza.
            int modositott = ps.executeUpdate();
            // 4. Siker: véglegesítés.
            kapcsolat.commit();
            return modositott;
        } catch (SQLException e) {
            // 5. Hiba: minden változás visszavonása, majd a hiba továbbadása.
            kapcsolat.rollback();
            throw e;
        } finally {
            // 6. A kapcsolat eredeti állapotának visszaállítása.
            kapcsolat.setAutoCommit(eredetiAutoCommit);
        }
    }

    /**
     * Közös segédmetódus: lefuttatja a lekérdezést és a ResultSet sorait
     * {@link Dolgozo} objektumokká alakítja.
     *
     * @param ps a paraméterekkel már feltöltött PreparedStatement
     * @return a dolgozók listája
     * @throws SQLException adatbázis-hiba esetén
     */
    private List<Dolgozo> lista(PreparedStatement ps) throws SQLException {
        List<Dolgozo> eredmeny = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            // A ResultSet kurzora az első sor ELŐTT áll; a next() lép a következőre,
            // és false-t ad, ha elfogytak a sorok.
            while (rs.next()) {
                eredmeny.add(new Dolgozo(
                        rs.getInt("EMPNO"),
                        rs.getString("ENAME"),
                        rs.getString("JOB"),
                        rs.getBigDecimal("SAL"),
                        rs.getInt("DEPTNO")));
            }
        }
        return eredmeny;
    }
}
