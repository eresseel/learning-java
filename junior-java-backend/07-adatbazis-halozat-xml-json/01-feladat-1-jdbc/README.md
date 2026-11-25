# Feladat I. – JDBC konzolos alkalmazás (dept/emp)

> Tananyag: Junior Java Backend fejlesztő VII., Feladat I. – lásd a [lecke áttekintőjét](../README.md).

## Feladat

Konzolos alkalmazás, amely a `dml` adatbázis dept/emp tábláin dolgozik:

- **A.** Listázzuk ki azokat a dolgozókat (név, fizetés), akiknek a fizetése kisebb, mint a felhasználó által megadott összeg.
- **B.** Listázzuk ki a felhasználó által megadott azonosítójú vezető beosztottjait (név).
- **C.** Listázzuk ki az ACCOUNTING (számviteli) osztály dolgozóit.
- **D.** Kapjon mindenki 10%-os fizetésemelést.

## Mit mutat be?

- Kapcsolódás: `DriverManager.getConnection(url, user, jelszó)` – a classpath-on lévő MySQL driver (`mysql-connector-j`) automatikusan regisztrálódik.
- **`PreparedStatement`** `?` helyőrzőkkel: a felhasználói bemenet nem kerül bele az SQL szövegébe → nincs SQL injection.
- **try-with-resources**: a `Connection`, `PreparedStatement`, `ResultSet` mind `AutoCloseable`, automatikusan lezárulnak.
- `ResultSet` bejárása `while (rs.next())`, oszlopok olvasása név szerint.
- **Tranzakció** (D): `setAutoCommit(false)` → `executeUpdate` → `commit()`, hiba esetén `rollback()`.
- DAO minta: az SQL egy osztályban (`EmpDao`), a menü (`App`) csak hívja.
- Pénzösszegekhez `BigDecimal` (a `double` kerekítési hibái miatt).

## Fájlok

- `pom.xml` – Maven projekt, függőség: `com.mysql:mysql-connector-j`.
- `src/main/java/hu/learning/junior/jdbc/AdatbazisKapcsolat.java` – kapcsolat nyitása (URL/felhasználó/jelszó, környezeti változóval felülírható).
- `src/main/java/hu/learning/junior/jdbc/Dolgozo.java` – egy dolgozó adatai (`record`).
- `src/main/java/hu/learning/junior/jdbc/EmpDao.java` – az A–D feladat SQL műveletei.
- `src/main/java/hu/learning/junior/jdbc/App.java` – konzolos menü.
- Az adatbázis: [`../docker-compose.yml`](../docker-compose.yml), [`../db/01-dept-emp.sql`](../db/01-dept-emp.sql).

## Futtatás

Előbb az adatbázis (a lecke mappájában, egy szinttel feljebb):

```bash
cd .. && docker compose up -d && cd 01-feladat-1-jdbc
```

```bash
mvn -B -q compile exec:java
```

Pipe-olt bemenettel (A: 1000 alatti fizetés, B: 7698-as vezető, C, kilépés):

```bash
printf 'a\n1000\nb\n7698\nc\n0\n' | mvn -B -q exec:java
```

Példa kimenet (részlet):

```text
Választás: Összeg: --- Fizetés < 1000 (2 fő) ---
 7369  SMITH      CLERK          800.00
 7900  JAMES      CLERK          950.00
Választás: Vezető azonosítója (EMPNO, pl. 7698): --- BLAKE (7698) beosztottjai (5 fő) ---
 7499  ALLEN      SALESMAN      1600.00
 7900  JAMES      CLERK          950.00
 7654  MARTIN     SALESMAN      1250.00
 7844  TURNER     SALESMAN      1500.00
 7521  WARD       SALESMAN      1250.00
Választás: --- ACCOUNTING osztály dolgozói (3 fő) ---
 7782  CLARK      MANAGER       2450.00
 7839  KING       PRESIDENT     5000.00
 7934  MILLER     CLERK         1300.00
```

### D. – béremelés: FIGYELEM, módosítja az adatokat!

A D menüpont megerősítést kér (`i`), és **ténylegesen módosítja** az `emp`
táblát; minden futtatás újabb 10%-ot ad:

```bash
printf 'd\ni\nc\n0\n' | mvn -B -q exec:java
```

```text
FIGYELEM: ez módosítja az adatbázist! Biztosan? (i/n): 14 dolgozó fizetése emelkedett 10%-kal.
--- ACCOUNTING osztály dolgozói (3 fő) ---
 7782  CLARK      MANAGER       2695.00
 7839  KING       PRESIDENT     5500.00
 7934  MILLER     CLERK         1430.00
```

Az eredeti adatok visszaállítása (a lecke mappájából):

```bash
docker compose exec -T db mysql -udml -pdml dml < db/01-dept-emp.sql
```

Ugyanerre az adatbázisra épül a [Feladat III.B](../05-feladat-3b-emp-json/) is.
