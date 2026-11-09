# 06 – Űrlap és felhasználói folyamat

## Cél
Automatizálj egy egyszerű űrlapot: töltsd ki a mezőket, küldd el, majd ellenőrizd a visszajelzést.

## Feladat
A teszt adjon meg egy nevet és egy e-mail-címet. Érvényes adatoknál az oldal jelenítse meg a sikerüzenetet. Üres név esetén jelenjen meg validációs hiba.

## Mit tanulunk?
- `sendKeys` és `clear`;
- gombkattintás;
- kliensoldali űrlapvalidáció;
- explicit wait és az eredmény állapotának ellenőrzése.

## Futtatás
```bash
mvn clean test
```

## Továbbfejlesztési feladat
Adj hozzá e-mail-formátumra vonatkozó tesztet. Gondold végig, melyik ellenőrzést végezze a böngésző, és melyiket az alkalmazás backendje.
