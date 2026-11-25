package hu.learning.junior.empjson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Az {@code emp} tábla lekérdezése JDBC-vel (ugyanaz a technika, mint a
 * Feladat I.-ben), az eredmény {@link DolgozoAdat} lista.
 */
public class EmpLekerdezo {

    /** A nyitott kapcsolat; lezárása a hívó dolga. */
    private final Connection kapcsolat;

    /**
     * @param kapcsolat nyitott adatbázis-kapcsolat
     */
    public EmpLekerdezo(Connection kapcsolat) {
        this.kapcsolat = kapcsolat;
    }

    /**
     * Lekéri az összes dolgozó nevét, munkakörét és fizetését.
     *
     * @return a dolgozók név szerint rendezve
     * @throws SQLException adatbázis-hiba esetén
     */
    public List<DolgozoAdat> osszesDolgozo() throws SQLException {
        List<DolgozoAdat> lista = new ArrayList<>();
        // Paraméter nélküli lekérdezés is mehet PreparedStatement-tel; mindkét
        // erőforrást a try-with-resources zárja le (fordított sorrendben).
        try (PreparedStatement ps = kapcsolat.prepareStatement("SELECT ENAME, JOB, SAL FROM emp ORDER BY ENAME");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new DolgozoAdat(rs.getString("ENAME"), rs.getString("JOB"), rs.getBigDecimal("SAL")));
            }
        }
        return lista;
    }
}
