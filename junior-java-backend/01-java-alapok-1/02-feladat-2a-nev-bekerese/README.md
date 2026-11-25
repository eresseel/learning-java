# Feladat II.A – Név bekérése és kiírása

> Tananyag: Junior Java Backend fejlesztő I. – Java alapok I. (JuniorJavaBackend_01.pdf), Feladat II.A – lásd a [lecke áttekintőjét](../README.md).

## Feladat

- Kérjük be a felhasználó vezetéknevét, majd új sorban a keresztnevét.
- Írjuk ki egy sorban a felhasználó teljes nevét.
- A kiíratás során használjunk vezérlőkaraktereket.

## Mit mutat be?

- Adatbekérés `Scanner`-rel (`nextLine()`), az erőforrás lezárása (`close()`).
- `print` (sortörés nélkül) és `println` (sortöréssel) különbsége.
- Vezérlőkarakterek: `\n` új sor, `\t` tabulátor, `\"` idézőjel a szövegben.
- Szövegek összefűzése a `+` operátorral.

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

Példa (a bemenet pipe-olva):

```bash
printf 'Kiss\nAnna\n' | java -cp out App
```

```text
Vezetéknév: Keresztnév: 
	A felhasználó teljes neve: "Kiss Anna"
```
