# 10. lépés – futtathatóság: Maven Wrapper, Docker, GitHub Actions (a kész projekt)

## Cél
Ugyanaz a tesztkód **több módon is futtatható** legyen, minden gépen és automatikusan is. Ez a lépés a tesztkódot nem változtatja, hanem a projekt köré épít infrastruktúrát. A végeredmény az eredeti zip tartalmának működő magja.

## Mi változott (csak új fájlok)
| Fájl | Szerepe |
|---|---|
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/maven-wrapper.properties` | Maven Wrapper: Maven telepítése nélkül futtatható (`only-script`, a Maven 3.9.9-et az első futáskor tölti le) |
| `Dockerfile`, `.dockerignore` | JDK 17 + Maven + Google Chrome image; `ENV DOCKER_CONTAINER=true` |
| `docker-compose.yml` | egy paranccsal indítható, a jelentések a hostra kimountolva |
| `.github/workflows/maven-tests.yml` | GitHub Actions workflow |
| `.gitignore`, `version.txt` | verziókezelési higiénia |

## Futtatási módok
```bash
./mvnw clean test                              # macOS / Linux, Maven telepítése nélkül
mvnw.cmd clean test                            # Windows
mvn clean test                                 # ha van Maven
HEADLESS=true ./mvnw clean test                # ablak nélkül
./mvnw test -Dtest=Test02_LoginTest            # egy tesztosztály
docker compose up --build                      # Docker
./mvnw test -Dsurefire.rerunFailingTestsCount=1  # egy ismétlés bukott tesztre (instabil demo szerver)
```

## Docker: két tudatos trükk
1. **Rétegcache**: előbb csak a `pom.xml` kerül be és fut a `dependency:go-offline`, csak utána a teljes forrás. Amíg a pom nem változik, a függőségek nem töltődnek le újra.
2. **`ENV DOCKER_CONTAINER=true`**: a `BaseTest` ezt olvassa, és headless Chrome-ot indít, mert a konténerben nincs grafikus felület.

A `docker-compose.yml` a `target/surefire-reports` és a `target/downloads` mappát kimountolja a hostra, különben a jelentések a konténerrel együtt elvesznének. A `platform: linux/amd64` azért kell, mert a Chrome Linuxra csak amd64-en érhető el (Apple Silicon gépen emuláció fut), a `shm_size: 2gb` a Chrome stabilitását szolgálja.

## GitHub Actions
Indul: push a `main`, `develop`, `feature/**`, `release/**`, `hotfix/**` ágakra és `v*.*.*` tagekre; pull request a `main` és `develop` ágra; kézzel (`workflow_dispatch`).

Lépések: checkout → JDK 17 (Temurin, Maven cache) → `chmod +x mvnw` → `./mvnw -B clean test` → a `target/surefire-reports/` feltöltése artifactként `if: always()` feltétellel, vagyis hibás futásnál is.

A `GITHUB_ACTIONS` környezeti változó miatt a BaseTest automatikusan headless módba kapcsol.

## Mi van még az eredeti zip-ben, ami ide nem került
`docs/` (fejezetekre bontott dokumentáció), `documentation/` (vezetői jelentés és jegyzőkönyv), `README.md`, `LICENSE`. Ezek dokumentációk, nem a működés részei.

## Kész, ha
Zöld lokális futás, a `docker compose up --build` lefut, és a GitHub Actions oldalon zöld futás látható artifacttal. **Védés előtt ezt nyisd meg és ellenőrizd.**

## A védéshez
„A tesztkódot nem kellett változtatnom: a BaseTest környezeti változóból dönti el, hogy headless módban fut-e. Ezért ugyanaz a kód fut IDE-ben, Dockerben és CI-ben.”
