package hu.learning.junior.empjson;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Feladat III.B – az {@code emp} tábla adatainak (ename, job, sal)
 * lekérdezése JDBC-vel, tárolása listában, majd kiírása JSON fájlba GSON-nal.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args opcionálisan a kimeneti fájl neve (alapértelmezés: {@code emp.json})
     */
    public static void main(String[] args) {
        Path kimenet = Path.of(args.length > 0 ? args[0] : "emp.json");
        // 1. Lekérdezés: a kapcsolat a blokk végén lezárul.
        List<DolgozoAdat> dolgozok;
        try (Connection kapcsolat = AdatbazisKapcsolat.kapcsolodas()) {
            dolgozok = new EmpLekerdezo(kapcsolat).osszesDolgozo();
        } catch (SQLException e) {
            System.err.println("Adatbázis-hiba: " + e.getMessage());
            System.err.println("Fut az adatbázis? Indítás: docker compose up -d (a lecke mappájában)");
            System.exit(1);
            return;
        }
        // 2. JSON fájl írása.
        try {
            new JsonExport().fajlba(dolgozok, kimenet);
        } catch (IOException e) {
            System.err.println("Fájlírási hiba: " + e.getMessage());
            System.exit(2);
        }
        // 3. Visszajelzés.
        System.out.println(dolgozok.size() + " dolgozó kiírva ide: " + kimenet.toAbsolutePath());
    }
}
