/**
 * A valós világ "laptop" objektumának egyszerűsített (absztrahált) modellje.
 * <p>
 * A diasor 3+1 lépéses VV → OO folyamatának eredménye: a laptop rengeteg
 * tulajdonságából csak azokat tartjuk meg, amelyek egy laptopkereskedés
 * programja szempontjából fontosak: gyártó, modell, cikkszám, ár.
 * <p>
 * Az osztály felépítése a tananyag ajánlását követi:
 * adattagok → konstruktor(ok) → getter/setter → egyéb metódusok.
 */
public class Laptop {

    // --- Adattagok (mezők, field-ek) ---
    // Mind private: kívülről csak a getter/setter metódusokon keresztül érhetők el
    // (ez az adatrejtés kritériumelem gyakorlati alkalmazása).
    private String gyarto;
    private String modell;
    private String cikkszam;
    private int ar;

    /**
     * Paraméteres konstruktor: a példányosításkor (new) fut le,
     * és beállítja az objektum kezdőértékeit.
     *
     * @param gyarto   a gyártó neve (pl. "Samsung")
     * @param modell   a modell neve (pl. "Series 9")
     * @param cikkszam a kereskedés belső cikkszáma
     * @param ar       az ár forintban
     */
    public Laptop(String gyarto, String modell, String cikkszam, int ar) {
        // A this kulcsszó az aktuális objektumot jelöli: így különböztetjük meg
        // a mezőt (this.gyarto) az azonos nevű paramétertől (gyarto).
        this.gyarto = gyarto;
        this.modell = modell;
        this.cikkszam = cikkszam;
        // Az árat már a setteren keresztül állítjuk, így az ellenőrzés egy helyen van.
        setAr(ar);
    }

    // --- Getterek: a mezők olvasása ---

    /** @return a gyártó neve */
    public String getGyarto() {
        return gyarto;
    }

    /** @return a modell neve */
    public String getModell() {
        return modell;
    }

    /**
     * A cikkszámhoz csak getter tartozik, setter nem: így a cikkszám
     * kívülről csak olvasható (a létrehozás után nem módosítható).
     *
     * @return a cikkszám
     */
    public String getCikkszam() {
        return cikkszam;
    }

    /** @return az ár forintban */
    public int getAr() {
        return ar;
    }

    // --- Setterek: a mezők írása, ellenőrzött módon ---

    /**
     * Beállítja az árat. A setter előnye, hogy kontrollálhatjuk az írás
     * mikéntjét: negatív árat nem fogadunk el.
     *
     * @param ar az új ár forintban (nem lehet negatív)
     */
    public void setAr(int ar) {
        // Érvénytelen érték esetén nem módosítjuk a mezőt, csak figyelmeztetünk.
        // (Kivételt dobni majd a 4. leckében tanulunk.)
        if (ar < 0) {
            System.out.println("Figyelem: az ár nem lehet negatív, a módosítás elmarad.");
            return;
        }
        this.ar = ar;
    }

    /**
     * Beállítja a modell nevét.
     *
     * @param modell az új modellnév
     */
    public void setModell(String modell) {
        this.modell = modell;
    }

    // --- Egyéb metódusok ---

    /**
     * Visszaadja a laptop adatait olvasható formában.
     * A toString() felülírásával a System.out.println(laptop) is szépen ír ki.
     *
     * @return a laptop adatai egy sorban
     */
    @Override
    public String toString() {
        return gyarto + " " + modell + " (" + cikkszam + "): " + ar + " Ft";
    }
}
