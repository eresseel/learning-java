# Feladat I. – Hello World!

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Feladat I. – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Írjunk egy programot, amely kiírja a konzolra: `Hello World!`

## Mit mutat be?

- A Java program minimális szerkezetét: osztály, `main` metódus mint belépési pont.
- A `System.out.println` használatát, a pontosvesszővel lezárt utasítást és a kapcsos zárójeles blokkokat.
- A munkafolyamatot: `.java` forrás → `javac` → `.class` bájtkód → JVM futtatja.

## Fájlok

- `src/App.java` – a program.

## Futtatás

A feladat mappájából:

```bash
javac -d out src/*.java
```

```bash
java -cp out App
```

Kimenet:

```text
Hello World!
```
