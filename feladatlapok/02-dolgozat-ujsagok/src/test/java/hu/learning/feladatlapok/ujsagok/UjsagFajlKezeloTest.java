package hu.learning.feladatlapok.ujsagok;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A CSV beolvasás és hozzáfűzés tesztjei (Feladat II. és X.).
 * A fájlműveletek a JUnit által adott ideiglenes mappában ({@code @TempDir}) futnak,
 * így a valódi ujsagok.csv nem változik.
 */
class UjsagFajlKezeloTest {

    /** Minden sortípusból a megfelelő osztály példánya jön létre. */
    @Test
    void sorbolUjsagTipusSzerint() {
        Ujsag u = UjsagFajlKezelo.sorbolUjsag("Ujsag - Tech Magazin - Bitlap Media Zrt. - 1490");
        Ujsag n = UjsagFajlKezelo.sorbolUjsag("Napilap - Reggeli Hirmondo - Fiktiv Kiado Kft. - 350 - 6");
        Ujsag h = UjsagFajlKezelo.sorbolUjsag("Hetilap - Kertbarat Het - Zold Lap Kft. - 690 - 4");

        assertEquals(Ujsag.class, u.getClass());
        assertEquals("Tech Magazin", u.getMegnevezes());
        assertEquals(1490, u.getAr());
        assertEquals(6, assertInstanceOf(Napilap.class, n).getHetiMegjelenes());
        assertEquals(4, assertInstanceOf(Hetilap.class, h).getHetNapja());
        assertEquals("Zold Lap Kft.", h.getKiado());
    }

    /** Ismeretlen típus esetén kivétel. */
    @Test
    void ismeretlenTipusKivetelt() {
        assertThrows(IllegalArgumentException.class,
                () -> UjsagFajlKezelo.sorbolUjsag("Folyoirat - X - Y - 100"));
    }

    /** Az objektum → CSV sor → objektum átalakítás oda-vissza ugyanazt adja. */
    @Test
    void csvSorOdaVissza() {
        Napilap eredeti = new Napilap("Esti Kronika", "Varosi Bt.", 300, 5);
        String sor = UjsagFajlKezelo.csvSor(eredeti);
        assertEquals("Napilap - Esti Kronika - Varosi Bt. - 300 - 5", sor);
        assertEquals(eredeti.toString(), UjsagFajlKezelo.sorbolUjsag(sor).toString());
    }

    /** Beolvasás tömbbe: az üres sorokat kihagyjuk. */
    @Test
    void beolvasTombbe(@TempDir Path mappa) throws IOException {
        // Arrange
        Path fajl = mappa.resolve("ujsagok.csv");
        Files.writeString(fajl, "Ujsag - A - B - 100\n\nHetilap - C - D - 200 - 1\n");
        // Act
        Ujsag[] tomb = UjsagFajlKezelo.beolvas(fajl);
        // Assert
        assertEquals(2, tomb.length);
        assertInstanceOf(Hetilap.class, tomb[1]);
    }

    /** Hozzáfűzéskor a régi adatok megmaradnak, és sortörés nélküli végű fájlnál sem ragad össze a sor. */
    @Test
    void hozzafuzMegtartjaARegiSorokat(@TempDir Path mappa) throws IOException {
        // Arrange – szándékosan sortörés NÉLKÜL végződő fájl.
        Path fajl = mappa.resolve("ujsagok.csv");
        Files.writeString(fajl, "Ujsag - A - B - 100");
        // Act
        UjsagFajlKezelo.hozzafuz(fajl, new Napilap("Uj Lap", "Uj Kiado", 250, 7));
        // Assert
        Ujsag[] tomb = UjsagFajlKezelo.beolvas(fajl);
        assertEquals(2, tomb.length);
        assertEquals("Uj Lap", tomb[1].getMegnevezes());
    }

    /** Kötőjeles mező nem írható a fájlba. */
    @Test
    void kotojelesMezoTiltott() {
        assertThrows(IllegalArgumentException.class,
                () -> UjsagFajlKezelo.ellenorizCsvMezo("Hi-Fi Magazin", "megnevezés"));
    }
}
