[← Előző: 11-wiremock.md](11-wiremock.md)

---

# Párhuzamos futtatás és `ThreadLocal`

**JUnit 5 párhuzamosság:** <https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution>  
**Java ThreadLocal:** <https://docs.oracle.com/en/java/docs/api/java.base/java/lang/ThreadLocal.html>

---

## `ThreadLocal<T>`

Olyan változó, amelynek minden szál számára **külön értéke** van. Az egyik szál nem látja és nem módosíthatja a másik szál értékét. Párhuzamos teszteknél ezt használjuk a WebDriver példányok elkülönítésére.

```java
import org.openqa.selenium.WebDriver;

public final class DriverFactory {

    // Statikus mező, de szálanként külön értékkel.
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void removeDriver() {
        // Fontos: a szálkészletben a szálak újrahasznosulnak.
        // A remove() nélkül a következő teszt a korábbi értéket kapná.
        DRIVER.remove();
    }
}
```

---

## `junit-platform.properties`

A JUnit 5 párhuzamos futtatását a `src/test/resources/junit-platform.properties` fájl konfigurálja.

```properties
# Párhuzamos futtatás bekapcsolása:
junit.jupiter.execution.parallel.enabled=true

# Tesztmetódusok és osztályok is párhuzamosan futhatnak:
junit.jupiter.execution.parallel.mode.default=concurrent
junit.jupiter.execution.parallel.mode.classes.default=concurrent

# Egyszerre legfeljebb 3 szál fut:
junit.jupiter.execution.parallel.config.strategy=fixed
junit.jupiter.execution.parallel.config.fixed.parallelism=3
```

---

## Párhuzamosság kikapcsolása egy osztályra

Ha egy osztály tesztjei szekvenciálisan kell fussanak (pl. közös erőforrást módosítanak), a `@Execution` annotáció jelzi ezt.

```java
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.SAME_THREAD)
class SequentialTest {
    // Ezek a tesztek egymás után futnak, még párhuzamos módban is.
}
```

---

[ Következő: 13-webdrivermanager.md → ](13-webdrivermanager.md)
