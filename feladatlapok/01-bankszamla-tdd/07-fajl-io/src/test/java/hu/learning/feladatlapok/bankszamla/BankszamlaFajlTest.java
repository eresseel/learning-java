package hu.learning.feladatlapok.bankszamla;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * A fájlkezelő műveletek tesztjei. A {@code @TempDir} annotáció minden
 * teszthez egy friss, ideiglenes könyvtárat ad, amit a JUnit a teszt után
 * töröl – így a tesztek nem szemetelnek a projekt mappájába, és nem
 * függnek egymástól.
 */
@DisplayName("Bankszámla – fájlkezelés")
class BankszamlaFajlTest {

    /** Ideiglenes könyvtár, a JUnit tölti ki minden teszt előtt. */
    @TempDir
    Path mappa;

    @Test
    @DisplayName("A fájlnevek a feladat szerintiek")
    void fajlnevek() {
        Bankszamla szamla = new Bankszamla("111-222", 0.01);
        assertEquals("egyenleg111-222.txt", szamla.egyenlegFajl(mappa).getFileName().toString());
        assertEquals("tranzakcio2026-11-25.txt",
                Bankszamla.tranzakcioFajl(mappa, LocalDate.of(2026, 11, 25)).getFileName().toString());
    }

    @Test
    @DisplayName("Egyenleg betöltése fájlból")
    void egyenlegBetoltes() throws IOException {
        // Arrange: elkészítjük az egyenlegfájlt (sortöréssel a végén is működjön)
        Files.writeString(mappa.resolve("egyenleg111-222.txt"), "52000\n", StandardCharsets.UTF_8);
        Bankszamla szamla = new Bankszamla("111-222", 0.01);
        // Act
        szamla.egyenlegBetoltes(mappa);
        // Assert
        assertEquals(52000, szamla.getEgyenleg());
        assertEquals(List.of("BETOLTES: egyenleg: 52000 Ft"), szamla.getTranzakciok());
    }

    @Test
    @DisplayName("Hiányzó egyenlegfájl IOException-t (NoSuchFileException) dob")
    void hianyzoFajl() {
        Bankszamla szamla = new Bankszamla("999", 0.01);
        assertThrows(NoSuchFileException.class, () -> szamla.egyenlegBetoltes(mappa));
    }

    @Test
    @DisplayName("Hibás vagy negatív tartalmú egyenlegfájl IllegalArgumentException-t dob")
    void hibasTartalom() throws IOException {
        Bankszamla szamla = new Bankszamla("111-222", 0.01);
        Files.writeString(szamla.egyenlegFajl(mappa), "sok pénz");
        assertThrows(IllegalArgumentException.class, () -> szamla.egyenlegBetoltes(mappa));
        Files.writeString(szamla.egyenlegFajl(mappa), "-10");
        assertThrows(IllegalArgumentException.class, () -> szamla.egyenlegBetoltes(mappa));
        assertEquals(0, szamla.getEgyenleg());
    }

    @Test
    @DisplayName("Egyenleg kiírása, majd visszatöltése egy másik objektumba")
    void egyenlegKiirasEsVisszatoltes() throws IOException {
        // Arrange
        Bankszamla szamla = new Bankszamla("111-222", 0.01);
        szamla.befizetes(7500);
        // Act
        Path fajl = szamla.egyenlegKiiras(mappa);
        Bankszamla masik = new Bankszamla("111-222", 0.01);
        masik.egyenlegBetoltes(mappa);
        // Assert
        assertEquals("7500", Files.readString(fajl));
        assertEquals(7500, masik.getEgyenleg());
    }

    @Test
    @DisplayName("Tranzakciótörténet kiírása soronként a dátumos fájlba")
    void tranzakciokKiirasa() throws IOException {
        // Arrange
        Bankszamla szamla = new Bankszamla("111-222", 0.01);
        szamla.befizetes(1000);
        szamla.kifizetes(300);
        LocalDate datum = LocalDate.of(2026, 11, 25);
        // Act
        Path fajl = szamla.tranzakciokKiirasa(mappa, datum);
        // Assert
        assertEquals(mappa.resolve("tranzakcio2026-11-25.txt"), fajl);
        assertEquals(szamla.getTranzakciok(), Files.readAllLines(fajl, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Dátum nélküli változat a mai dátummal ír")
    void tranzakciokKiirasaMa() throws IOException {
        Bankszamla szamla = new Bankszamla("111-222", 0.01);
        szamla.befizetes(1);
        Path fajl = szamla.tranzakciokKiirasa(mappa);
        assertEquals("tranzakcio" + LocalDate.now() + ".txt", fajl.getFileName().toString());
    }
}
