package hu.learning.junior.empjson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Az adatbázis-kapcsolat adatait (URL, felhasználó, jelszó) fogja össze, és
 * új {@link Connection}-t ad a {@link DriverManager} segítségével.
 *
 * <p>Az alapértékek a lecke mappájában lévő {@code docker-compose.yml}
 * MySQL konténeréhez illenek. Környezeti változókkal felülírhatók:
 * {@code DB_URL}, {@code DB_USER}, {@code DB_PASSWORD} – így a jelszót nem kell
 * a forráskódba égetni, ha másik szerverhez csatlakozunk.</p>
 */
public final class AdatbazisKapcsolat {

    /** Alapértelmezett JDBC URL: MySQL, localhost, 3307-es port, dml adatbázis. */
    public static final String ALAP_URL = "jdbc:mysql://localhost:3307/dml";

    /** Példányosítás tiltása: csak statikus segédmetódust tartalmaz. */
    private AdatbazisKapcsolat() {
    }

    /**
     * Új kapcsolatot nyit az adatbázishoz. A hívó felelőssége lezárni –
     * legegyszerűbben try-with-resources blokkal.
     *
     * @return nyitott adatbázis-kapcsolat
     * @throws SQLException ha a szerver nem érhető el vagy hibás a belépési adat
     */
    public static Connection kapcsolodas() throws SQLException {
        // 1. A beállítások kiolvasása (környezeti változó, ha nincs: alapérték).
        String url = kornyezetiVagyAlap("DB_URL", ALAP_URL);
        String felhasznalo = kornyezetiVagyAlap("DB_USER", "dml");
        String jelszo = kornyezetiVagyAlap("DB_PASSWORD", "dml");
        // 2. A DriverManager a classpath-on lévő driverek közül (mysql-connector-j)
        //    kiválasztja azt, amelyik érti a "jdbc:mysql:" URL-t.
        return DriverManager.getConnection(url, felhasznalo, jelszo);
    }

    /**
     * @param nev    a környezeti változó neve
     * @param alap   alapérték, ha a változó nincs beállítva vagy üres
     * @return a változó értéke vagy az alapérték
     */
    private static String kornyezetiVagyAlap(String nev, String alap) {
        String ertek = System.getenv(nev);
        return (ertek == null || ertek.isBlank()) ? alap : ertek;
    }
}
