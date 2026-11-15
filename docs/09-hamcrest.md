# Hamcrest matcherek

**Dokumentáció:** <https://hamcrest.org/JavaHamcrest/javadoc/2.2/>  
**GitHub:** <https://github.com/hamcrest/JavaHamcrest>

A REST Assured a `then().body(...)` ellenőrzéseiben Hamcrest matchereket használ. Statikusan importálhatók az `org.hamcrest.Matchers` osztályból.

---

## `equalTo(value)`

Egyenlőséget ellenőriz (`equals()`-szel). A `body("id", equalTo(1))` a JSON `id` mezőjét hasonlítja.

```java
import static org.hamcrest.Matchers.equalTo;

.body("id", equalTo(1))
.body("title", equalTo("sunt aut facere repellat provident"))
```

---

## `not(matcher)`

Megfordítja bármely matcher eredményét.

```java
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

.body("title", not(emptyOrNullString()))   // nem üres és nem null
```

---

## `emptyOrNullString()`

Igaz, ha a szöveg null vagy üres (`""`). A `not(emptyOrNullString())` azt jelenti: nem üres és nem null.

```java
.body("body", not(emptyOrNullString()))
```

---

## `containsString(substring)`

Igaz, ha a szöveg tartalmazza a megadott részt.

```java
import static org.hamcrest.Matchers.containsString;

.contentType(containsString("application/json"))
```

---

## `everyItem(matcher)`

Egy lista minden elemére alkalmazza a megadott matchert.

```java
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.equalTo;

// Minden bejegyzés userId mezője egyenlő 1-gyel:
.body("userId", everyItem(equalTo(1)))
```

---

## `lessThan(value)`

Kisebb-mint összehasonlítás. Válaszidő ellenőrzésekor hasznos.

```java
import static org.hamcrest.Matchers.lessThan;

.time(lessThan(5000L))   // a válaszidő legfeljebb 5 másodperc
```

