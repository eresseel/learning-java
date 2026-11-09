# 6. lépés – „regisztráció”: új rendszerfelhasználó létrehozása

## Cél
Vizsgakövetelmény **1. pont**: regisztráció. Az OrangeHRM demo nem kínál nyilvános önregisztrációt, ezért ennek megfelelője az, hogy a bejelentkezett Admin új rendszerfelhasználót hoz létre (**Admin → Add User**).

## Mi változott
- **Módosult:** `AdminPage.java`: kiegészült az űrlapkitöltéssel és a visszakereséssel (`openAddUserPage`, `registerNewUser`, `isUserCreatedSuccessfully`, `isUserPresentInList`).
- **Új:** `Test01_UserRegistrationTest.java`.

## Új fogalmak
- **Custom komponensek**: a User Role és a Status nem natív HTML `<select>`, hanem kattintásra nyíló lista. Ezért: kattintás a mezőre → megvárni az opciót (`role='option'`) → kattintás az opcióra.
- **Autocomplete**: az employee mezőbe beírunk egy betűt (`a`), megvárjuk a javaslatokat, és az **első valódi találatra** kattintunk. A „Searching…” és „No Records” pszeudo-opciókat kiszűrjük az XPath-ban.
- **Egyedi tesztadat**: `"Teszt_Elek_" + UUID első 4 karaktere`. Így a teszt újrafuttatható ütközés nélkül.
- **Label-alapú XPath**: `//label[text()='Username']/parent::div/following-sibling::div//input`. A címke szövege ritkábban változik, mint a generált class.
- **Index-alapú lokátor jelszóra**: az Add User űrlapon pontosan két `type='password'` mező van, így `(//input[@type='password'])[1]` és `[2]`.
- **Toast üzenet**: a sikeres mentést jelző felugró üzenet (`oxd-toast--success`).

## A teszt (TC-01)
1. Belépés, dashboard megvárása.
2. `openAddUserPage()`: az Add User űrlap megnyitása.
3. `registerNewUser("a", felhasználónév, jelszó)`: ESS, Enabled, az első employee, név, jelszó kétszer, mentés.
4. `assertTrue(isUserCreatedSuccessfully())`: a siker toast látható.
5. Vissza a listára, `isUserPresentInList(felhasználónév)`: a felhasználó **megtalálható a szűrt listában**.

A teszt tehát nem csak a toast üzenetre épít, hanem visszakeresi az új adatot is. Ez erősebb ellenőrzés.

## Futtatás

```bash
cd step06-regisztracio
mvn clean test
```

(JDK 17+, Google Chrome és internet kell. A Maven Wrapper csak a 10. lépéstől van a mappában; addig `mvn` kell, vagy másold át a 10. lépésből az `mvnw`, `mvnw.cmd` és `.mvn/` elemeket.)

## Kész, ha
A konzolon megjelenik a létrehozott felhasználónév, a teszt zöld.

## A védéshez
„Mivel önregisztráció nincs, az Admin általi felhasználólétrehozást teszteltem. Ezt a README és a docs is rögzíti.”
