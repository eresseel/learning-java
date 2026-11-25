package hu.learning.feladatlapok.bankszamla;

/**
 * Egy bankszámlát modellez: számlaszám, egyenleg (egész forint), napi kamat.
 *
 * <p>Az osztály TDD-vel (Test Driven Development) készül: minden új
 * viselkedéshez ELŐBB írtunk egy bukó tesztet (piros), aztán a lehető
 * legkevesebb kóddal zöldre hoztuk, végül rendbe tettük a kódot (refaktor).
 * Ez a 4. lépés állapota.</p>
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

    /**
     * Pénzt fizet be a számlára.
     *
     * @param osszeg a befizetett összeg, pozitív kell legyen
     * @throws IllegalArgumentException ha az összeg nem pozitív
     */
    public void befizetes(long osszeg) {
        // 1. Közös ellenőrzés (refaktor során kiemelt segédmetódus).
        pozitivOsszegEllenorzes(osszeg);
        // 2. Az egyenleg növelése.
        egyenleg += osszeg;
    }

    /**
     * Pénzt vesz fel a számláról.
     *
     * @param osszeg a felvett összeg, pozitív kell legyen
     * @throws IllegalArgumentException ha az összeg nem pozitív
     * @throws NincsFedezetException    ha nincs elég pénz a számlán
     */
    public void kifizetes(long osszeg) {
        // 1. Az összeg legyen pozitív.
        pozitivOsszegEllenorzes(osszeg);
        // 2. Fedezet ellenőrzése: mínuszba nem mehetünk.
        if (osszeg > egyenleg) {
            throw new NincsFedezetException(
                    "Nincs elegendő fedezet! Egyenleg: " + egyenleg + " Ft, kért összeg: " + osszeg + " Ft");
        }
        // 3. Az egyenleg csökkentése.
        egyenleg -= osszeg;
    }

    /**
     * Átutal egy összeget erről a számláról egy másik számlára.
     * Fedezethiány esetén egyik számla egyenlege sem változik.
     *
     * @param cel    a kedvezményezett számla
     * @param osszeg az átutalt összeg, pozitív kell legyen
     * @throws IllegalArgumentException ha a cél null, ugyanez a számla, vagy az összeg nem pozitív
     * @throws NincsFedezetException    ha nincs elég pénz a forrásszámlán
     */
    public void atutalas(Bankszamla cel, long osszeg) {
        // 1. A célszámla legyen megadva, és ne önmagunk.
        if (cel == null) {
            throw new IllegalArgumentException("A célszámla nem lehet null!");
        }
        if (cel == this || cel.szamlaszam.equals(szamlaszam)) {
            throw new IllegalArgumentException("Saját számlára nem lehet átutalni!");
        }
        // 2. Az összeg legyen pozitív.
        pozitivOsszegEllenorzes(osszeg);
        // 3. Levonás a forrásszámláról: ha nincs fedezet, a kifizetes()
        //    kivételt dob, és a célszámla jóváírása el sem indul.
        kifizetes(osszeg);
        // 4. Jóváírás a célszámlán.
        cel.befizetes(osszeg);
    }

    /**
     * Közös segédmetódus: az összeg legyen pozitív. A refaktor lépésben
     * emeltük ki, mert több művelet is ugyanezt ellenőrzi.
     *
     * @param osszeg az ellenőrzendő összeg
     * @throws IllegalArgumentException ha az összeg 0 vagy negatív
     */
    private static void pozitivOsszegEllenorzes(long osszeg) {
        if (osszeg <= 0) {
            throw new IllegalArgumentException("Az összegnek pozitívnak kell lennie: " + osszeg);
        }
    }
}
