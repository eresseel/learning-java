# Projekt Dokumentáció

## Projekt Áttekintés

Ez egy Java alapú Selenium webes tesztelő projekt, amely az OrangeHRM demo oldalának bejelentkező funkcióját teszteli. A projekt Maven-alapú és JUnit5 keretrendszerrel működik.

## Projekt Struktúra

```
my-app/
├── pom.xml                 # Maven konfigurációs fájl
├── README.md               # Projekt leírás
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── mycompany/
│   │               └── app/
│   │                   └── LoginPage.java  # Bejelentkezési oldal elemek és műveletek
│   └── test/
│       └── java/
│           └── com/
│               └── mycompany/
│                   └── app/
│                       ├── BaseTest.java     # Alap teszt osztály, böngésző inicializálás és leállítás
│                       └── tests/
│                           └── LoginTest.java  # Bejelentkezési teszt
```

## Fájlok Részletezése

### pom.xml
- Maven projekt konfiguráció
- Java 27 kompatibilitás
- JUnit5 és Selenium függőségek
- WebDriverManager függőség a böngésző driverek automatikus kezeléséhez

### LoginPage.java
- A bejelentkező oldal elemeket és műveleteket kezelő osztály
- Locatorok: felhasználónév mező, jelszó mező, bejelentkezés gomb, hibaüzenet
- Metódusok:
  - `navigateTo()`: Látogassa meg a bejelentkező oldalt
  - `enterUsername()`: Felhasználónév mező kitöltése
  - `enterPassword()`: Jelszó mező kitöltése
  - `clickLogin()`: Bejelentkezés gomb kattintása
  - `getErrorMessage()`: Hibaüzenet lekérése

### BaseTest.java
- Alap teszt osztály, amely inicializálja a Chrome böngészőt
- @BeforeEach: Böngésző beállítások és indítás
- @AfterEach: Böngésző bezárása és leállítása
- ChromeOptions konfiguráció:
  - Teljes képernyős megjelenítés
  - Valós böngésző User-Agent szimulálása (headless elkerülése)
  - Nyelv beállítás (en-US)

### LoginTest.java
- Egy egyszerű bejelentkezési teszt
- Tesztelési lépések:
  1. Látogassa meg a bejelentkező oldalt
  2. Adjon meg felhasználónevet és jelszót
  3. Kattintson a bejelentkezés gombra
  4. Ellenőrizze, hogy az "Invalid credentials" hibaüzenet jelenik-e meg

## Függőségek

- Selenium WebDriver 4.52.0
- WebDriverManager 6.4.0
- JUnit5 (junit-jupiter-api, junit-jupiter-params, junit-jupiter-engine)
- Maven Surefire Plugin 3.6.1