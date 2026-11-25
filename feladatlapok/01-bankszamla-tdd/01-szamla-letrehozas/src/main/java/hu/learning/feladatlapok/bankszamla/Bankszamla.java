package hu.learning.feladatlapok.bankszamla;

/**
 * Egy bankszámlát modellez: számlaszám, egyenleg (egész forint), napi kamat.
 *
 * <p>Az osztály TDD-vel (Test Driven Development) készül: minden új
 * viselkedéshez ELŐBB írtunk egy bukó tesztet (piros), aztán a lehető
 * legkevesebb kóddal zöldre hoztuk, végül rendbe tettük a kódot (refaktor).
 * Ez a 1. lépés állapota.</p>
 */
public class Bankszamla {

    /** A számla azonosítója (szöveges, pl. "11773016-00000001"). */
    private final String szamlaszam;

    /** Az aktuális egyenleg egész forintban. */
    private long egyenleg;

    /** Napi kamatláb SZÁZALÉKBAN (pl. 0.01 = napi 0,01%). */
    private final double napiKamat;

    /**
     * Új számlát nyit 0 Ft egyenleggel.
     *
     * @param szamlaszam a számla azonosítója, nem lehet null vagy üres
     * @param napiKamat  napi kamatláb százalékban, nem lehet negatív
     * @throws IllegalArgumentException ha valamelyik paraméter érvénytelen
     */
    public Bankszamla(String szamlaszam, double napiKamat) {
        // 1. Ellenőrzés: üres számlaszámmal nem jöhet létre számla.
        if (szamlaszam == null || szamlaszam.isBlank()) {
            throw new IllegalArgumentException("A számlaszám nem lehet üres!");
        }
        // 2. Negatív kamatot a bank nem ad (és nem is von le).
        if (napiKamat < 0) {
            throw new IllegalArgumentException("A napi kamat nem lehet negatív!");
        }
        // 3. Mezők beállítása; az egyenleg kezdetben 0.
        this.szamlaszam = szamlaszam;
        this.napiKamat = napiKamat;
        this.egyenleg = 0;
    }

    /** @return a számlaszám */
    public String getSzamlaszam() {
        return szamlaszam;
    }

    /** @return az aktuális egyenleg forintban */
    public long getEgyenleg() {
        return egyenleg;
    }

    /** @return a napi kamatláb százalékban */
    public double getNapiKamat() {
        return napiKamat;
    }
}
