package hu.laci.learning;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

/**
 * Az UI tesztek közös ősosztálya, párhuzamos futtatásra felkészítve.
 * <p>
 * A böngészőt a {@link DriverFactory} hozza létre szálanként. A JUnit alapértelmezése szerint minden
 * tesztmetódushoz új tesztosztály-példány készül, ezért a {@code driver} példánymező önmagában is
 * szálbiztos lenne. A ThreadLocal akkor lesz igazán fontos, ha a böngészőt statikus segédkódból
 * is el kell érni (pl. naplózás, képernyőkép-készítés), és nem akarjuk mindenhova átadni paraméterként.
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.createDriver();
        // A szál neve megmutatja, hogy a tesztek tényleg különböző szálakon futnak.
        System.out.println("START " + Thread.currentThread().getName() + " -> " + getClass().getSimpleName());
    }

    @AfterEach
    public void tearDown() {
        System.out.println("END   " + Thread.currentThread().getName() + " -> " + getClass().getSimpleName());
        DriverFactory.quitDriver();
    }
}
