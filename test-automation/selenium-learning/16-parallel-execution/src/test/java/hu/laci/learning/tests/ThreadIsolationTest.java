package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.DriverFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Bizonyítja a szálak elkülönítését: a ThreadLocal-ból kapott böngésző pontosan az a példány,
 * amelyet ehhez a teszthez indítottunk, még akkor is, ha közben más tesztek más szálakon futnak.
 */
class ThreadIsolationTest extends BaseTest {

    @Test
    void shouldReturnOwnDriverForCurrentThread() {
        assertNotNull(DriverFactory.getDriver());
        // assertSame: ugyanaz az objektum (referencia-egyenlőség), nem csak egyenlő tartalmú.
        assertSame(driver, DriverFactory.getDriver());
    }
}
