package hu.learning.feladatlapok.mockito;

/**
 * A számlák tárolásáért felelős réteg (pl. adatbázis) absztrakciója.
 * <p>
 * A {@link SzamlaService} csak ettől az interfésztől függ, ezért a tesztekben
 * valódi adatbázis helyett egy Mockito mockot adhatunk át neki.
 */
public interface SzamlaRepository {

    /**
     * Megkeresi a számlát a számlaszáma alapján.
     *
     * @param szamlaszam a keresett számlaszám
     * @return a megtalált számla, vagy {@code null}, ha nincs ilyen
     */
    Szamla findBySzamlaszam(String szamlaszam);

    /**
     * Elmenti (felülírja) a számla állapotát.
     *
     * @param szamla a mentendő számla
     */
    void save(Szamla szamla);

    /**
     * Megmondja, létezik-e a megadott számlaszámú számla.
     *
     * @param szamlaszam a vizsgált számlaszám
     * @return {@code true}, ha létezik
     */
    boolean letezik(String szamlaszam);

    /**
     * Visszaadja a tárolt számlák darabszámát.
     *
     * @return a számlák száma
     */
    int szamlakSzama();
}
