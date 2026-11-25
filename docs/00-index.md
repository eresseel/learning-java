# Import-leírások – áttekintő

A két projektben ([`test-automation/selenium-learning`](../test-automation/selenium-learning/) és [`test-automation/orangehrm-lepesrol-lepesre`](../test-automation/orangehrm-lepesrol-lepesre/)) használt külső könyvtárak és JUnit API-k leírása példakódokkal és dokumentációs linkekkel.

---

| Fájl | Témakör | Érintett lépések |
|------|---------|-----------------|
| [01-junit5-annotations.md](01-junit5-annotations.md) | `@Test`, `@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@Disabled`, `@DisplayName`, `@Tag`, `@Nested`, `@EnabledIfSystemProperty` | 01–17 |
| [02-junit5-assertions.md](02-junit5-assertions.md) | `assertEquals`, `assertTrue`, `assertFalse`, `assertNotNull`, `assertSame`, `assertThrows` | 01–17 |
| [03-junit5-parameterized.md](03-junit5-parameterized.md) | `@ParameterizedTest`, `@ValueSource`, `@CsvSource` | 03, 11 |
| [04-junit5-extensions.md](04-junit5-extensions.md) | `@ExtendWith`, `@RegisterExtension`, `AfterTestExecutionCallback`, `ExtensionContext` | 12, 15 |
| [05-selenium-core.md](05-selenium-core.md) | `WebDriver`, `ChromeDriver`, `ChromeOptions`, `By`, `WebElement`, `Alert`, `OutputType`, `TakesScreenshot` | 04–08, 13–17 |
| [06-selenium-waits.md](06-selenium-waits.md) | `WebDriverWait`, `ExpectedConditions` | 06–08, 13–17 |
| [07-selenium-advanced.md](07-selenium-advanced.md) | `Select`, `iframe` (`switchTo().frame`), új ablak kezelése | 13 |
| [08-restassured.md](08-restassured.md) | `given/when/then`, `RequestSpecification`, `Response`, `jsonPath()`, JSON séma validáció | 09–11, 17 |
| [09-hamcrest.md](09-hamcrest.md) | `equalTo`, `not`, `emptyOrNullString`, `containsString`, `everyItem`, `lessThan` | 09–11 |
| [10-jackson.md](10-jackson.md) | `ObjectMapper`, `JsonNode`, `@JsonIgnoreProperties`, `@JsonInclude` | 10–12, 17 |
| [11-wiremock.md](11-wiremock.md) | `WireMockExtension`, `stubFor`, `Fault`, `verify` | 12 |
| [12-parallel-threadlocal.md](12-parallel-threadlocal.md) | `ThreadLocal`, `junit-platform.properties`, `@Execution` | 16 |
| [13-webdrivermanager.md](13-webdrivermanager.md) | `WebDriverManager`, összehasonlítás a Selenium Manager-rel | OrangeHRM 01–10 |
| [14-selenium-extra.md](14-selenium-extra.md) | `Keys`, `JavascriptExecutor`, `TimeoutException` | OrangeHRM 01–10 |
| [15-junit5-assertions-class.md](15-junit5-assertions-class.md) | `Assertions` osztályszintű hívás, `@CsvFileSource` | OrangeHRM 01–10 |

---

## Gyors keresési útmutató

- **Hol van a `@Test`?** → [01-junit5-annotations.md](01-junit5-annotations.md)
- **Hol van az `assertEquals`?** → [02-junit5-assertions.md](02-junit5-assertions.md)
- **Hol van a `WebDriverWait`?** → [06-selenium-waits.md](06-selenium-waits.md)
- **Hol van a `given().when().then()`?** → [08-restassured.md](08-restassured.md)
- **Hol van a `stubFor`?** → [11-wiremock.md](11-wiremock.md)
- **Hol van a `ThreadLocal`?** → [12-parallel-threadlocal.md](12-parallel-threadlocal.md)
- **Hol van a `WebDriverManager`?** → [13-webdrivermanager.md](13-webdrivermanager.md)
- **Hol van a `Keys` / `JavascriptExecutor` / `TimeoutException`?** → [14-selenium-extra.md](14-selenium-extra.md)
- **Hol van a `@CsvFileSource`?** → [15-junit5-assertions-class.md](15-junit5-assertions-class.md)

---

[ Következő: 01-junit5-annotations.md → ](01-junit5-annotations.md)
