# Feladat III.A – autókereskedés XML beolvasása és kiírása

> Tananyag: Junior Java Backend fejlesztő VII., Feladat III.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Olvassuk be az `autokereskedes.xml` fájlt, az adatokat tároljuk adatszerkezetben.
- Adjunk a listához új objektumot, majd készítsünk belőle új XML fájlt.

A mintaadat saját készítésű, az eredeti `autokereskedes.xml` nem volt a tananyag része. Szerkezete:

```xml
<autokereskedes nev="Példa Autóház Kft.">
    <auto id="1">
        <marka>Toyota</marka>
        <tipus>Corolla</tipus>
        <evjarat>2019</evjarat>
        <ar>6490000</ar>
        <szin>ezüst</szin>
    </auto>
    ...
</autokereskedes>
```

## Mit mutat be?

- XML olvasás a JDK beépített **DOM** API-jával (`DocumentBuilderFactory` → `Document` → `getElementsByTagName`, `getAttribute`, `getTextContent`) – külső könyvtár (pl. JAXB) nélkül.
- XML írás: DOM fa építése (`createElement`, `setAttribute`, `appendChild`) és kiírása `Transformer`-rel, behúzással, UTF-8-ban.
- Biztonság: DOCTYPE tiltása a parseren (**XXE** – XML External Entity – támadás ellen), erre teszt is van.
- XML ↔ objektum leképezés `record`-okkal (`Auto`, `Autokereskedes`); a beolvasott listát új `ArrayList`-be másoljuk, hogy bővíthető legyen.
- Kivételek becsomagolása (`ParserConfigurationException`, `SAXException` → `IOException`).

## Fájlok

- `autokereskedes.xml` – a bemeneti mintaadat (4 autó).
- `src/main/java/hu/learning/junior/autokereskedes/Auto.java` – egy autó (`record`).
- `src/main/java/hu/learning/junior/autokereskedes/Autokereskedes.java` – a kereskedés neve + autólista.
- `src/main/java/hu/learning/junior/autokereskedes/AutoXml.java` – DOM olvasás és írás.
- `src/main/java/hu/learning/junior/autokereskedes/App.java` – beolvasás, új autó, kiírás.
- `src/test/resources/autokereskedes.xml` – a minta másolata a tesztekhez.
- `src/test/java/hu/learning/junior/autokereskedes/AutoXmlTest.java` – olvasás, oda-vissza alakítás (`@TempDir`), hibás XML, XXE.

## Futtatás

```bash
mvn -B -q verify
```

```bash
mvn -B -q compile exec:java
```

```text
Példa Autóház Kft. – beolvasott autók:
  Auto[id=1, marka=Toyota, tipus=Corolla, evjarat=2019, ar=6490000, szin=ezüst]
  Auto[id=2, marka=Suzuki, tipus=Vitara, evjarat=2021, ar=7990000, szin=piros]
  Auto[id=3, marka=Skoda, tipus=Octavia Combi, evjarat=2017, ar=4850000, szin=fehér]
  Auto[id=4, marka=Opel, tipus=Astra, evjarat=2015, ar=2990000, szin=kék]
Hozzáadva: Auto[id=5, marka=Dacia, tipus=Duster, evjarat=2022, ar=6990000, szin=zöld]
5 autó kiírva ide: .../04-feladat-3a-autokereskedes-xml/autokereskedes_uj.xml
```

Az `autokereskedes_uj.xml` generált fájl, a lecke `.gitignore`-ja kizárja a verziókezelésből.
Más fájlnevek: `mvn -B -q exec:java -Dexec.args="be.xml ki.xml"`.
