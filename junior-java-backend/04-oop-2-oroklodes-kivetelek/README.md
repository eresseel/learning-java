# 04 – OOP II.: öröklődés, absztrakt osztály, kivételkezelés

A 4. lecke (Botlik Béla: *Junior Java Backend fejlesztő IV. – OOP II.*) példái és feladatai: öröklődés, metódus-felülbírálás, polimorfizmus, absztrakt osztály, kivételkezelés, valamint az összefoglaló projektmunka (állatmenhely).

| Feladat | Téma |
| --- | --- |
| [Példa – RepuloDolgok öröklődés](01-pelda-repulodolgok-oroklodes/) | `extends`, `super`, felülbírálás több szinten („repülő dolgok” lánc 2. lépése) |
| [Példa – RepuloDolgok absztrakt osztály](02-pelda-repulodolgok-absztrakt/) | `abstract` osztály és metódus (a lánc 3. lépése) |
| [Példa – kivételkezelés](03-pelda-kivetelkezeles/) | try-catch-finally, terjedés, ellenőrzött/futási idejű, saját kivétel |
| [Feladat I – ABC raktárkezelés](04-feladat-1-abc-raktar/) | `Termek` → `Tejtermek`, `Pekaru`; validáció kivételekkel |
| [Feladat II – Állatmenhely](05-feladat-2-allatmenhely/) | összefoglaló projektmunka: absztrakt `Allat`, `final` `Kutya`/`Macska`, menü |

A „repülő dolgok” példalánc: [03 – Jármű polimorfizmus](../03-oop-alapok/03-pelda-jarmu-polimorfizmus/) → [04/01](01-pelda-repulodolgok-oroklodes/) → [04/02](02-pelda-repulodolgok-absztrakt/) → [05 – RepulInterface](../05-interfesz-solid-kollekciok/01-pelda-repul-interfesz/).

## Futtatás

Minden feladat build eszköz nélkül, a JDK-val futtatható (Java 21+). A feladat mappájában:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Az interaktív programok pipe-olt bemenettel is futtathatók; ha elfogy a bemenet, a program üzenettel kilép.
