# Demo – OrangeHRM bejelentkezési teszt (Maven quickstart)

Maven `quickstart` archetípusból indított minimális Selenium + JUnit projekt, amely az OrangeHRM demo oldal bejelentkezését teszteli. Részletes leírás: [docs.md](docs.md).

## Technológiák

- Java 21 (`maven.compiler.release`)
- JUnit 6 (BOM), Selenium 4, WebDriverManager
- Maven Wrapper (a beállítások a projekt [`.mvn`](.mvn/) mappájában vannak)

## Futtatás

```bash
./mvnw clean test
```

## A projekt létrehozása

```bash
mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
```
