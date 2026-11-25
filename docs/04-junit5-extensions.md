[← Előző: 03-junit5-parameterized.md](03-junit5-parameterized.md)

---

# JUnit extension modell

**Dokumentáció:** <https://junit.org/junit5/docs/current/user-guide/#extensions>

---

## `@ExtendWith`

Egy kiegészítőt (extension) kapcsol egy osztályhoz vagy metódushoz. Az annotáció öröklődik, ezért elég az ősosztályra tenni: minden leszármazott teszt megkapja.

```java
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ScreenshotOnFailureExtension.class)
public class BaseTest {
    // Minden leszármazott tesztnél elbukáskor képernyőkép készül.
}
```

---

## `@RegisterExtension`

Egy kiegészítőt **példányként** regisztrál (nem csak osztályként). Akkor kell, ha a kiegészítőnek konfigurációs paraméterei vannak, amelyeket programozottan adunk meg. `static` mezőként az osztály összes tesztje között egyszer indul el.

```java
import org.junit.jupiter.api.extension.RegisterExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class ServiceTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
}
```

---

## `AfterTestExecutionCallback`

Egy callback-interfész, amelyet a kiegészítőnek kell implementálnia. Akkor hívódik meg, amikor a tesztmetódus **lefutott**, de az `@AfterEach` metódusok még nem. Ez azért fontos, mert a `TestWatcher` túl késő: ott a böngésző már be lehet zárva.

```java
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        // Csak elbukott tesztnél csinálunk valamit.
        if (context.getExecutionException().isEmpty()) return;

        // A tesztosztály példányából elérjük a WebDriver-t.
        Object instance = context.getRequiredTestInstance();
        // ... képernyőkép készítése ...
    }
}
```

---

## `ExtensionContext`

A kiegészítő számára rendelkezésre álló kontextusobjektum: a teszt osztályát, metódusát, példányát és a kivételt adja vissza.

```java
// A teszt neve (naplózáshoz):
String testName = context.getRequiredTestMethod().getName();

// Az osztály neve:
String className = context.getRequiredTestClass().getSimpleName();

// A tesztosztály példánya (pl. a driver eléréséhez):
Object instance = context.getRequiredTestInstance();

// A kivétel, ha a teszt elbukott:
context.getExecutionException().ifPresent(ex -> System.out.println(ex.getMessage()));
```

---

[ Következő: 05-selenium-core.md → ](05-selenium-core.md)
