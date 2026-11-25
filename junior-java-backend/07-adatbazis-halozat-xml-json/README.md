# 07 – Adatbázis-kezelés (JDBC), hálózatkezelés, XML és JSON

> Tananyag: Junior Java Backend fejlesztő VII. (Adatbáziskezelés – JDBC, JPA; Hálózatkezelés; XML, JSON)

A lecke témái:

- **JDBC** (Java Database Connectivity): alacsony szintű adatbázis-elérés natív SQL-lel. A lánc: Java alkalmazás → JDBC API → DriverManager → JDBC driver (itt: `mysql-connector-j`) → adatbázis-szerver.
- **JPA** (Java Persistence API): JDBC-re épülő ORM szabvány (Hibernate, EclipseLink, OpenJPA), lekérdező nyelve a JPQL. Ebben a leckében csak elméletként szerepel, a feladatok JDBC-vel készülnek.
- **Hálózatkezelés**: szerver–kliens kommunikáció socketeken, TCP/UDP; a legfontosabb protokollok HTTP, FTP, POP, SMTP. A feladatok a JDK `java.net.http.HttpClient`-jét használják.
- **XML és JSON**: szöveges adatcsere-formátumok; a JSON tömörebb.

## Feladatok

| Feladat | Téma |
|---|---|
| [Feladat I. – JDBC konzolos alkalmazás](01-feladat-1-jdbc/) | `PreparedStatement`, try-with-resources, `ResultSet`, tranzakció (commit/rollback) |
| [Feladat II.A – Dickens és Shaw idézetei](02-feladat-2a-idezetek/) | `HttpClient`, JSON API (GSON fa-modell), tartalék forrás |
| [Feladat II.B – ruander.hu tanfolyamok](03-feladat-2b-ruander-tanfolyamok/) | Weboldal letöltése, HTML feldolgozás regexszel, mentett mintaoldalas teszt |
| [Feladat III.A – autókereskedés XML](04-feladat-3a-autokereskedes-xml/) | DOM olvasás/írás (`javax.xml`), XXE elleni védelem |
| [Feladat III.B – emp tábla JSON-ba](05-feladat-3b-emp-json/) | JDBC + GSON, JSON fájl írása |
| [Kiegészítés – TCP echo szerver/kliens](06-socket-echo/) | `ServerSocket`, `Socket`, stream-ek (elméleti demó) |

## Az adatbázis (Feladat I. és III.B)

A feladatok a „SQL alapok” modul klasszikus **dept/emp** tábláit használják a
`dml` adatbázisban. Az eredeti `DEPT_EMP_MYSQL.sql` nem volt a tananyag része,
ezért saját script készült: [`db/01-dept-emp.sql`](db/01-dept-emp.sql)
(4 osztály: ACCOUNTING, RESEARCH, SALES, OPERATIONS; 14 dolgozó).

A MySQL 8 szerver Dockerben fut ([`docker-compose.yml`](docker-compose.yml)):

```bash
docker compose up -d          # indítás (első induláskor lefut az init script)
docker compose ps             # állapot: "healthy" kell legyen
docker compose down           # leállítás (az adatok megmaradnak a volume-ban)
docker compose down -v        # leállítás + adatok törlése (következő induláskor újra létrejön)
```

| Beállítás | Érték |
|---|---|
| Gép, port | `localhost:3307` (a konténerben 3306) |
| Adatbázis | `dml` |
| Felhasználó / jelszó | `dml` / `dml` (csak helyi gyakorláshoz!) |
| JDBC URL | `jdbc:mysql://localhost:3307/dml` |

A programok a `DB_URL`, `DB_USER`, `DB_PASSWORD` környezeti változókkal másik
szerverre is irányíthatók.

Az eredeti adatok visszaállítása (pl. a 10%-os béremelés után), leállítás nélkül:

```bash
docker compose exec -T db mysql -udml -pdml dml < db/01-dept-emp.sql
```

Kézi lekérdezés a konténerben:

```bash
docker compose exec db mysql -udml -pdml dml -e "SELECT * FROM emp"
```

## Közös futtatási útmutató

Minden feladat külön Maven projekt (Java 21). A projekt mappájából:

```bash
mvn -B -q verify              # fordítás + egységtesztek
mvn -B -q compile exec:java   # a program futtatása (a függőségekkel együtt)
```

Paraméter átadása: `mvn -B -q exec:java -Dexec.args="..."`.
A Feladat I. és III.B előtt indítsd el az adatbázist (`docker compose up -d`
ebben a mappában). A II.A és II.B internetkapcsolatot igényel, de az
egységtesztjeik mentett mintákon futnak, így offline is zöldek.
