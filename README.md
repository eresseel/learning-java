# learning-java

Java tanulási jegyzetek és gyakorló projektek: a nyelvi alapoktól a Selenium/REST Assured tesztautomatizáláson át a Spring Boot integrációs tesztekig.

## Tartalom

| Mappa | Leírás | Java | Build |
|---|---|---|---|
| [java-programozas-1-3](java-programozas-1-3/) | Nyelvi alapok: operátorok, ciklusok, elágazások, enum (17 feladat) | bármely JDK | `javac` |
| [java-24-ora](java-24-ora/) | „Java 24 óra alatt” könyv példái: tömbök, osztályok, öröklődés, Swing (21 feladat) | bármely JDK | `javac` |
| [java-reprogramozas](java-reprogramozas/) | Videós tananyag: OOP, generikusok, kollekciók, lambdák, streamek, I/O, szálak | 21 | `javac` |
| [test-automation/selenium-learning](test-automation/selenium-learning/) | 17 egymásra épülő szint: JUnit, Selenium, POM, REST Assured, WireMock, párhuzamos futtatás | 21 | Maven |
| [test-automation/orangehrm-lepesrol-lepesre](test-automation/orangehrm-lepesrol-lepesre/) | OrangeHRM UI tesztautomatizálás 10 lépésben, Docker és GitHub Actions támogatással | 21 | Maven |
| [test-automation/demo](test-automation/demo/) | Minimális Maven quickstart Selenium bejelentkezési teszttel | 21 | Maven Wrapper |
| [spring-wiremock-demo](spring-wiremock-demo/) | Spring Boot 3 REST API, Testcontainers és WireMock integrációs tesztek | 21 | Maven |
| [docs](docs/00-index.md) | Kézikönyv a tesztprojektekben használt API-khoz (JUnit, Selenium, REST Assured, Hamcrest, Jackson, WireMock) | – | – |

## Egységes felépítés

- **Elnevezés:** minden mappa kebab-case, a sorrendet kétjegyű előtag adja (`01-hello-world`, `03-fejezet/07-modem`, `05-listazas`).
- Minden feladat / projekt saját `README.md`-vel rendelkezik: mit mutat be, milyen fájlokból áll, hogyan futtatható.
- A build nélküli feladatok szerkezete: `<feladat>/src/*.java`, fordítás `javac -d out src/*.java` paranccsal.
- A Maven projektek önállóan futtathatók a saját mappájukból: `mvn clean test` (vagy `./mvnw clean test`, ahol van wrapper).
- A szöveges fájlok UTF-8 kódolásúak, LF sorvégzéssel (lásd [.gitattributes](.gitattributes)); a build kimenetek (`target/`, `out/`) nincsenek verziókezelve.

## Egységes verziók

Minden Maven projekt Java 21-re fordít (`maven.compiler.release`), és ugyanazokat a verziókat használja:

| Függőség / eszköz | Verzió | Hol |
|---|---|---|
| JUnit (BOM) | 6.1.3 | `test-automation` |
| Selenium | 4.54.0 | `test-automation` |
| WebDriverManager | 6.4.0 | `test-automation` |
| REST Assured | 6.3.0 | `test-automation` |
| Jackson | 2.23.0 | `test-automation` |
| WireMock (standalone) | 3.13.2 | mindenhol |
| maven-compiler-plugin / maven-surefire-plugin | 3.16.0 / 3.6.1 | `test-automation` |
| Maven Wrapper disztribúció | 3.10.2 | `demo`, `orangehrm …/10-docker-ci-wrapper` |
| Spring Boot | 3.5.16 | `spring-wiremock-demo` |

A `spring-wiremock-demo` modulokban a JUnit, REST Assured és Testcontainers verzióját a Spring Boot BOM adja (Spring Boot 3.x-hez JUnit 5 tartozik), ezért ott nincs kézzel rögzítve.

Verzióemeléskor a fenti táblázatot és az érintett `pom.xml`-ek `<properties>` blokkját együtt kell frissíteni.

## Előfeltételek

- JDK 21 vagy újabb
- Maven 3.9+
- Google Chrome a Selenium tesztekhez
- Docker a `spring-wiremock-demo` Testcontainers alapú tesztjeihez

## Licenc

[MIT](LICENSE)
