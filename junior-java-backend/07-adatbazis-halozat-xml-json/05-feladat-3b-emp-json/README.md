# Feladat III.B – az emp tábla JSON fájlba (JDBC + GSON)

> Tananyag: Junior Java Backend fejlesztő VII., Feladat III.B – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kapcsolódjunk a `dml` adatbázishoz, kérjük le az `emp` tábla adatait (`ename`, `job`, `sal`).
- Tároljuk adatszerkezetben (lista).
- Készítsünk belőle kimeneti JSON állományt a **GSON** külső könyvtárral.

Ez a feladat a [Feladat I.](../01-feladat-1-jdbc/) adatbázisára és JDBC technikájára épül
(ugyanaz a Docker konténer és ugyanaz a kapcsolódási mód).

## Mit mutat be?

- JDBC lekérdezés `PreparedStatement` + `ResultSet` bejárással, try-with-resources (két erőforrás egy blokkban).
- Külső könyvtár (GSON) használata Mavenből: `GsonBuilder().setPrettyPrinting()`, `toJson(objektum, Writer)`.
- A GSON a `record` komponenseinek nevét használja JSON kulcsként, és vissza is tudja olvasni (`fromJson`).
- Tesztelhetőség: a JSON-készítés adatbázis nélkül, kézzel összerakott listán tesztelhető (`@TempDir`).

## Fájlok

- `pom.xml` – függőségek: `mysql-connector-j`, `gson`.
- `src/main/java/hu/learning/junior/empjson/AdatbazisKapcsolat.java` – kapcsolat (ugyanaz, mint a Feladat I.-ben).
- `src/main/java/hu/learning/junior/empjson/DolgozoAdat.java` – `record (ename, job, sal)`.
- `src/main/java/hu/learning/junior/empjson/EmpLekerdezo.java` – JDBC lekérdezés.
- `src/main/java/hu/learning/junior/empjson/JsonExport.java` – JSON szöveg / fájl készítése GSON-nal.
- `src/main/java/hu/learning/junior/empjson/App.java` – belépési pont.
- `src/test/java/hu/learning/junior/empjson/JsonExportTest.java` – adatbázis nélküli teszt.

## Futtatás

Előbb az adatbázis: `docker compose up -d` a lecke mappájában.

```bash
mvn -B -q verify
```

```bash
mvn -B -q compile exec:java                          # -> emp.json
mvn -B -q exec:java -Dexec.args="target/emp.json"    # másik kimeneti fájl
```

Példa kimenet:

```text
14 dolgozó kiírva ide: .../05-feladat-3b-emp-json/emp.json
```

Az `emp.json` eleje:

```json
[
  {
    "ename": "ADAMS",
    "job": "CLERK",
    "sal": 1100.00
  },
  {
    "ename": "ALLEN",
    "job": "SALESMAN",
    "sal": 1600.00
  },
  ...
]
```

Az `emp.json` generált fájl, a lecke `.gitignore`-ja kizárja a verziókezelésből.
