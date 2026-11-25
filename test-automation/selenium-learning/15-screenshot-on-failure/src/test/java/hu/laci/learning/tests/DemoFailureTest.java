package hu.laci.learning.tests;

import hu.laci.learning.BaseTest;
import hu.laci.learning.pages.ContactPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * SZÁNDÉKOSAN elbukó demóteszt, a képernyőkép-készítés kipróbálásához.
 * <p>
 * Alapértelmezetten nem fut le (a pom.xml-ben a demoFailure értéke false), mert egy mindig piros teszt
 * zavarná a buildet. Bekapcsolás: {@code mvn clean test -DdemoFailure=true}
 * A hibás futás után nézd meg a {@code target/screenshots} mappát.
 */
@EnabledIfSystemProperty(named = "demoFailure", matches = "true")
class DemoFailureTest extends BaseTest {

    @Test
    void shouldFailOnPurposeToProduceScreenshot() {
        ContactPage page = new ContactPage(driver).open();

        page.enterName("Laci").enterEmail("laci@example.com").submit();

        // Szándékosan rossz elvárás: a tényleges üzenet "Form submitted for Laci".
        assertEquals("This text is wrong on purpose", page.getStatusMessage());
    }
}
