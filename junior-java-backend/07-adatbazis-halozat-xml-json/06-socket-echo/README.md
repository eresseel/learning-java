# Kiegészítés – TCP echo szerver és kliens (socketek)

> Tananyag: Junior Java Backend fejlesztő VII., Hálózatkezelés I. (elméleti demó, nem számozott feladat) – lásd a [lecke áttekintőjét](../README.md).

## Feladat

A lecke elmélete szerint a hálózati kommunikáció szerver–kliens alapú, a
résztvevők **socketeken** keresztül kapcsolódnak (TCP/UDP). Ez a kis demó ezt
mutatja meg a legegyszerűbb formában:

- A szerver egy TCP porton figyel, és minden kapott sort `ECHO: ` előtaggal visszaküld; a `VEGE` sorra elköszön és bontja a kapcsolatot.
- A kliens a konzolon beírt sorokat elküldi, és kiírja a választ.

## Mit mutat be?

- `ServerSocket` (port lefoglalása, `accept()` – blokkoló várakozás a kliensre) és `Socket` (a kapcsolat egy vége).
- Bájtfolyamok (`InputStream`/`OutputStream`) becsomagolása soronkénti karakterfolyammá (`BufferedReader`, `PrintWriter` autoFlush-sal, UTF-8).
- try-with-resources több erőforrással.
- Teszt valódi TCP kapcsolaton: 0-s port (az OS választ szabad portot), szerver külön szálon, `@Timeout`.

## Fájlok

- `src/main/java/hu/learning/junior/socket/EchoSzerver.java` – a szerver.
- `src/main/java/hu/learning/junior/socket/EchoKliens.java` – a kliens.
- `src/test/java/hu/learning/junior/socket/EchoSzerverTest.java` – szerver–kliens teszt.

## Futtatás

```bash
mvn -B -q verify
```

Két terminálban. Első terminál – szerver (alapértelmezett port: 5000):

```bash
mvn -B -q compile exec:java
```

Második terminál – kliens:

```bash
mvn -B -q exec:java -Dexec.mainClass=hu.learning.junior.socket.EchoKliens -Dexec.args="localhost 5000"
```

Pipe-olt bemenettel:

```bash
printf 'Szia\nVEGE\n' | mvn -B -q exec:java -Dexec.mainClass=hu.learning.junior.socket.EchoKliens
```

```text
Kapcsolódva: localhost/127.0.0.1:5000 (kilépés: VEGE)
ECHO: Szia
Viszlát!
```

A szerver Ctrl+C-vel állítható le.
