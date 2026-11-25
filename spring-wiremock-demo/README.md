# Spring Boot Integrációs Tesztelés – WireMock Demo

Három egymásra épülő Maven projekt, amelyek bemutatják az iparági standard integrációs
tesztelési technikákat Spring Boot 3.4 + Java 21 alkalmazásban.

---

## Projekt struktúra

```
spring-wiremock-demo/
├── 01-task-api/            ← Production kód: CRUD API, nincs teszt
├── 02-integration-tests/   ← Testcontainers + REST Assured IT tesztek
├── 03-wiremock-tests/      ← WireMock: külső HTTP API szimulálása
└── README.md               ← Ez a dokumentum
```

---

## 01 – Task API (production kód)

**Célja:** Clean, önálló Spring Boot CRUD alkalmazás, tesztelési függőség nélkül.

### Technológiák
| Réteg | Technológia |
|---|---|
| Web | Spring Boot Starter Web (Spring MVC) |
| Adatbázis | Spring Data JPA + PostgreSQL |
| Validáció | Jakarta Bean Validation (`@NotBlank`, `@Size`) |
| Hibakezelés | `@RestControllerAdvice` + RFC 7807 `ProblemDetail` |

### REST végpontok
| Metódus | URL | Leírás | Sikeres státusz |
|---|---|---|---|
| POST | `/api/tasks` | Task létrehozása | 201 Created + Location header |
| GET | `/api/tasks` | Összes task (opcionális `?status=` szűrő) | 200 OK |
| GET | `/api/tasks/{id}` | Egy task | 200 OK / 404 Not Found |
| PUT | `/api/tasks/{id}` | Task frissítése | 200 OK / 404 Not Found |
| DELETE | `/api/tasks/{id}` | Task törlése | 204 No Content / 404 Not Found |

### Tervezési döntések
- **DTO réteg** (`TaskRequest`, `TaskResponse`): az API leválik a belső modelltől
- **Record típusok** (Java 16+): immutable, boilerplate-mentes DTO-k
- **`@Transactional(readOnly = true)`** olvasó metódusoknál: Hibernate flush skip
- **`@PrePersist`**: `createdAt` automatikus kitöltése (nem klienstől jön)
- **ProblemDetail**: RFC 7807 szabványos JSON hibatörzs

### Futtatás (valódi PostgreSQL szükséges)
```bash
# Docker Compose a PostgreSQL-hez
docker run -d --name taskdb \
  -e POSTGRES_DB=taskdb \
  -e POSTGRES_USER=taskuser \
  -e POSTGRES_PASSWORD=taskpass \
  -p 5432:5432 postgres:16-alpine

cd 01-task-api
mvn spring-boot:run
```

---

## 02 – Integration Tests

**Célja:** A 01-es modul teljes API-jának integrációs tesztelése valódi PostgreSQL-lel.

### Technológiák
| Eszköz | Verzió | Szerep |
|---|---|---|
| JUnit 5 | (Spring Boot BOM) | Teszt keretrendszer |
| Testcontainers | 1.20.4 | PostgreSQL Docker konténer kezelése |
| REST Assured | 5.4.0 | HTTP kliens tesztekhez (valódi kérések) |
| maven-failsafe-plugin | (Spring Boot BOM) | IT tesztek futtatása `verify` fázisban |

### Testcontainers magyarázat

```java
@Container
@ServiceConnection
static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine");
```

- **`@Container`**: JUnit 5 extension kezeli a lifecycle-t (start/stop)
- **`static`**: egy konténer az összes teszt osztályhoz (gyorsabb; vs. instance field = minden osztályhoz új konténer)
- **`@ServiceConnection`** (Spring Boot 3.1+): automatikusan konfigurálja a DataSource URL-t, user-t, password-t → nincs szükség `@DynamicPropertySource`-ra

### REST Assured magyarázat

```java
given()                              // előfeltételek (header, body)
    .contentType(ContentType.JSON)
    .body(new TaskRequest("title", "desc"))
.when()                              // HTTP akció
    .post("/api/tasks")
.then()                              // assertek (Hamcrest)
    .statusCode(201)
    .body("title", equalTo("title"));
```

**Miért REST Assured és nem MockMvc?**
- REST Assured valódi HTTP kéréseket küld (`@SpringBootTest(RANDOM_PORT)`)
- A teljes HTTP stack tesztelődik: servlet filter, encoder, deserializer
- MockMvc: bypass-olja a HTTP réteget, gyorsabb de kevésbé realisztikus

### Teszt struktúra
```
AbstractIntegrationTest      ← közös konfig: PostgreSQL, REST Assured base URL
└── TaskApiIT
    ├── CreateTask            ← POST /api/tasks (201, 400 blank, 400 too long)
    ├── GetTaskById           ← GET /api/tasks/{id} (200, 404)
    ├── GetAllTasks           ← GET /api/tasks (üres lista, 2 task)
    ├── UpdateTask            ← PUT /api/tasks/{id} (200, 404)
    └── DeleteTask            ← DELETE /api/tasks/{id} (204, 404)
```

### Futtatás
```bash
cd 02-integration-tests
mvn verify          # failsafe futtatja az *IT.java teszteket
mvn test            # csak unit tesztek (itt nincs)
```

---

## 03 – WireMock Tests

**Célja:** Külső HTTP REST API hívás tesztelése WireMock mock szerverrel.

### Architektúra

```
[REST Assured]
      │  POST /api/tasks
      ▼
[Spring Boot App : RANDOM_PORT]
      │  taskRepository.save()
      ▼
[PostgreSQL konténer]  ← Testcontainers
      │
      │  notificationClient.sendTaskCreatedNotification()
      ▼
[WireMock Server]      ← Szimulálja a külső notification API-t
```

### Miért WireMock?

| Forgatókönyv | WireMock nélkül | WireMock-kal |
|---|---|---|
| Külső API 500-at ad | Nem reprodukálható | `withStatus(500)` |
| Timeout | Hálózattól függ | `withFixedDelay(3000)` |
| Offline környezet (CI) | Test fail | Izolált, mindig fut |
| Request body ellenőrzés | Nehézkes | `matchingJsonPath()` |

### Technológiák
| Eszköz | Verzió | Szerep |
|---|---|---|
| WireMock | 3.9.2 | HTTP mock szerver |
| RestClient | Spring 6 | Külső API hívás (a régi RestTemplate modern utódja) |

### WireMock konfiguráció

```java
// 1. Szerver indítása dinamikus porton
static WireMockServer wireMockServer = new WireMockServer(
        WireMockConfiguration.wireMockConfig().dynamicPort()
);

// 2. URL átadása a Spring context-nek
@DynamicPropertySource
static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("notification.service.url", wireMockServer::baseUrl);
}

// 3. Stub beállítása (minden tesztben)
wireMockServer.stubFor(
    post(urlEqualTo("/notifications/tasks"))
        .willReturn(aResponse().withStatus(200))
);

// 4. Kérés ellenőrzése
wireMockServer.verify(1, postRequestedFor(urlEqualTo("/notifications/tasks")));
```

### Tesztelt forgatókönyvek

#### Sikeres értesítés
- `200 OK` → task létrejön, `201 Created`
- Request body ellenőrzés: `taskId`, `event=TASK_CREATED`, `title`

#### 5xx resilience
- `500 Internal Server Error` → task MÉGIS létrejön
- `503 Service Unavailable` → task MÉGIS létrejön
- Visszatérő 500 (3 task) → mind a 3 task létrejön

#### Timeout
- `fixedDelay(2000ms)` → lassú, de sikeres (alapértelmezett timeout nélkül)
- `fixedDelay(3000ms)` + 1s timeout konfig → timeout hiba elnyelt, task létrejön
- `chunkedDribbleDelay` → read timeout szimulálása

#### Stub nélkül (404)
- WireMock 404-et ad (nincs stub) → task MÉGIS létrejön

### Resilience minta

```java
// NotificationClient.java
try {
    restClient.post()
        .uri("/notifications/tasks")
        .body(...)
        .retrieve()
        .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
            throw new ExternalServiceException("HTTP " + res.getStatusCode());
        })
        .toBodilessEntity();
    return true;
} catch (ExternalServiceException | RestClientException e) {
    log.warn("Notification failed: {}", e.getMessage());
    return false;  // ← task MÉGIS létrejön
}
```

**Best-effort értesítés**: ha a külső API hibát ad, a task mentése nem gördül vissza.
Garantált kézbesítéshez az **Outbox Pattern** kellene (DB-be írás + aszinkron küldés).

### Futtatás
```bash
cd 03-wiremock-tests
mvn verify
```

---

## Teljes tesztcsomag futtatása

```bash
# 02-es modul
cd 02-integration-tests && mvn verify

# 03-as modul
cd 03-wiremock-tests && mvn verify
```

**Előfeltétel:** Docker fut a gépen (Testcontainers szükséges).

---

## Verziók

| Technológia | Verzió |
|---|---|
| Java | 21 (LTS) |
| Spring Boot | 3.4.1 |
| Testcontainers | 1.20.4 |
| REST Assured | 5.4.0 |
| WireMock | 3.9.2 |
| PostgreSQL image | 16-alpine |

---

## Következő lépések

1. **Outbox Pattern** – garantált at-least-once értesítés küldés
2. **Resilience4j** – circuit breaker, retry, rate limiter a NotificationClient-hez
3. **WireMock stateful scenarios** – állapotgép szimulálása (pl. retry után sikerül)
4. **Contract testing (Pact)** – consumer-driven contract tesztek a notification API-val
5. **Spring Cloud Contract** – WireMock stub generálás OpenAPI specből
