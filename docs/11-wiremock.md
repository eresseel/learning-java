[← Előző: 10-jackson.md](10-jackson.md)

---

# WireMock – HTTP mock szerver

**Dokumentáció:** <https://wiremock.org/docs/>  
**GitHub:** <https://github.com/wiremock/wiremock>

---

## `WireMockExtension`

JUnit kiegészítő: a teszt előtt elindítja a mock szervert, utána leállítja, és minden teszt után törli a stubokat. A `dynamicPort()` szabad portot választ, így nem ütközik más futó szolgáltatással.

```java
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RegisterExtension
static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

// A szerver alap URL-je (pl. "http://localhost:54321"):
String baseUrl = wireMock.baseUrl();
```

---

## `stubFor` – válasz programozása

Megmondja a mock szervernek, mit válaszoljon egy adott kérésre.

```java
import static com.github.tomakehurst.wiremock.client.WireMock.*;

// 200 OK JSON válasz:
wireMock.stubFor(get(urlEqualTo("/posts/1"))
        .willReturn(okJson("{\"id\":1,\"title\":\"Hello\"}")));

// 404 Not Found:
wireMock.stubFor(get(urlEqualTo("/posts/42"))
        .willReturn(aResponse().withStatus(404)));

// 500 Server Error:
wireMock.stubFor(get(urlEqualTo("/posts/1"))
        .willReturn(aResponse().withStatus(500)));

// Lassú válasz (3 másodperc késéssel):
wireMock.stubFor(get(urlEqualTo("/posts/1"))
        .willReturn(okJson("{\"title\":\"slow\"}").withFixedDelay(3000)));
```

---

## `Fault` – hálózati hibák szimulálása

Olyan hibákat szimuláció, amelyeket éles rendszernél szinte lehetetlen előidézni.

```java
import com.github.tomakehurst.wiremock.http.Fault;

// A szerver válasz közben megszakítja a kapcsolatot:
wireMock.stubFor(get(urlEqualTo("/posts/1"))
        .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

// Üres választ küld (nem érvényes HTTP válasz):
wireMock.stubFor(get(urlEqualTo("/posts/1"))
        .willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));
```

---

## `verify` – kérések ellenőrzése

Ellenőrzi, hogy a mock szerverhez valóban érkezett-e a várt kérés, és hányszor.

```java
import static com.github.tomakehurst.wiremock.client.WireMock.*;

// Pontosan egyszer hívták meg a /posts/1 végpontot:
wireMock.verify(1, getRequestedFor(urlEqualTo("/posts/1")));

// Legalább egyszer hívták:
wireMock.verify(moreThanOrExactly(1), getRequestedFor(urlEqualTo("/posts/1")));

// Soha nem hívták:
wireMock.verify(0, getRequestedFor(urlEqualTo("/posts/42")));
```

---

[ Következő: 12-parallel-threadlocal.md → ](12-parallel-threadlocal.md)
