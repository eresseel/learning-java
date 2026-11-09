# 04 – Első Selenium-teszt

## Cél
Indíts Chrome böngészőt, nyiss meg egy helyi HTML-fájlt, és ellenőrizd az oldal címét.

## Feladat
A teszt:
1. indítsa el a Chrome-ot;
2. nyissa meg a `src/test/resources/site/index.html` fájlt;
3. ellenőrizze az oldal címét és főcímét;
4. minden esetben zárja be a böngészőt.

## Fontos
A `@AfterEach` gondoskodik a böngésző bezárásáról akkor is, ha az assertion elbukik. A `file:` URL helyi fájlt nyit meg, ezért nincs szükség külön webszerverre.

## Futtatás
```bash
mvn clean test
```

## Hibakeresés
Ha a Chrome nem indul el, ellenőrizd a Chrome telepítését, a JDK 17-et és a hálózati hozzáférést, amelyre a Selenium Managernek szüksége lehet a driver letöltéséhez.

## Továbbfejlesztési feladat
Ellenőrizd azt is, hogy az oldal egyedi `data-testid="welcome-message"` eleme látható.
