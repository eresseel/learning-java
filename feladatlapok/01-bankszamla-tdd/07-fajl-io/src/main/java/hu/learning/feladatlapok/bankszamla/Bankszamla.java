package hu.learning.feladatlapok.bankszamla;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Egy bankszámlát modellez: számlaszám, egyenleg (egész forint), napi kamat és a tranzakciók szöveges listája.
 *
 * <p>Az osztály TDD-vel (Test Driven Development) készül: minden új
 * viselkedéshez ELŐBB írtunk egy bukó tesztet (piros), aztán a lehető
 * legkevesebb kóddal zöldre hoztuk, végül rendbe tettük a kódot (refaktor).
 * Ez a 7. lépés állapota.</p>
 */
public class Bankszamla {

    /** Az egyenlegfájl nevének előtagja: "egyenleg" + számlaszám + ".txt". */
    public static final String EGYENLEG_FAJL_ELOTAG = "egyenleg";

    /** A tranzakciós fájl nevének előtagja: "tranzakcio" + dátum + ".txt". */
    public static final String TRANZAKCIO_FAJL_ELOTAG = "tranzakcio";

    /** A számla azonosítója (szöveges, pl. "11773016-00000001"). */
    private final String szamlaszam;

    /** Az aktuális egyenleg egész forintban. */
    private long egyenleg;

    /** Napi kamatláb SZÁZALÉKBAN (pl. 0.01 = napi 0,01%). */
    private final double napiKamat;

    /** A számlán végzett műveletek szöveges naplója (időrendben). */
    private final ArrayList<String> tranzakciok = new ArrayList<>();

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
     * Visszaadja a tranzakciós napló MÁSOLATÁT, hogy kívülről ne lehessen
     * belepiszkálni a számla belső állapotába (egységbezárás).
     *
     * @return a tranzakciók csak olvasható listája
     */
    public List<String> getTranzakciok() {
        return List.copyOf(tranzakciok);
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
        // 3. Naplózás.
        naploz("BEFIZETES: +" + osszeg + " Ft, egyenleg: " + egyenleg + " Ft");
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
        // 4. Naplózás.
        naploz("KIFIZETES: -" + osszeg + " Ft, egyenleg: " + egyenleg + " Ft");
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
        // 3. Levonás a forrásszámláról – a fedezetet az ellenőrzés után,
        //    de MÉG a jóváírás előtt nézzük meg, így hiba esetén egyik
        //    számla sem változik.
        if (osszeg > egyenleg) {
            throw new NincsFedezetException(
                    "Nincs elegendő fedezet az átutaláshoz! Egyenleg: " + egyenleg + " Ft");
        }
        egyenleg -= osszeg;
        naploz("ATUTALAS KI: -" + osszeg + " Ft -> " + cel.szamlaszam + ", egyenleg: " + egyenleg + " Ft");
        // 4. Jóváírás a célszámlán (ugyanaz az osztály, ezért a privát mezőit is elérjük).
        cel.egyenleg += osszeg;
        cel.naploz("ATUTALAS BE: +" + osszeg + " Ft <- " + szamlaszam + ", egyenleg: " + cel.egyenleg + " Ft");
    }

    /**
     * Jóváírja egy nap kamatát: kamat = egyenleg * napiKamat / 100,
     * egész forintra kerekítve (Math.round). 0 Ft-os kamatot nem könyvelünk.
     *
     * @return a jóváírt kamat forintban
     */
    public long napiKamatJovairas() {
        // 1. A kamat kiszámítása, kerekítés egész forintra.
        long kamat = Math.round(egyenleg * napiKamat / 100.0);
        // 2. Ha van mit jóváírni, növeljük az egyenleget és naplózunk.
        if (kamat > 0) {
            egyenleg += kamat;
            naploz("KAMAT: +" + kamat + " Ft, egyenleg: " + egyenleg + " Ft");
        }
        // 3. Visszaadjuk az összeget (a teszt és a hívó is ellenőrizheti).
        return kamat;
    }

    /**
     * Megadja az egyenlegfájl elérési útját a megadott mappában:
     * {@code "egyenleg" + szamlaszam + ".txt"}.
     *
     * @param mappa a könyvtár, ahol a fájl van / lesz
     * @return a fájl teljes útvonala
     */
    public Path egyenlegFajl(Path mappa) {
        return mappa.resolve(EGYENLEG_FAJL_ELOTAG + szamlaszam + ".txt");
    }

    /**
     * Megadja a tranzakciós fájl útvonalát: {@code "tranzakcio" + datum + ".txt"},
     * ahol a dátum ISO formátumú (pl. 2026-11-25).
     *
     * @param mappa a könyvtár
     * @param datum a nap, amelyhez a fájl tartozik
     * @return a fájl teljes útvonala
     */
    public static Path tranzakcioFajl(Path mappa, LocalDate datum) {
        return mappa.resolve(TRANZAKCIO_FAJL_ELOTAG + datum + ".txt");
    }

    /**
     * Betölti az egyenleget a {@code egyenleg<szamlaszam>.txt} fájlból.
     * A fájl egyetlen egész számot tartalmaz.
     *
     * @param mappa a könyvtár, ahol az egyenlegfájl van
     * @throws IOException              ha a fájl nem olvasható
     * @throws IllegalArgumentException ha a fájl tartalma nem érvényes, nemnegatív egész szám
     */
    public void egyenlegBetoltes(Path mappa) throws IOException {
        // 1. A teljes fájl beolvasása (kis fájl, egy sor), szóközök levágása.
        String tartalom = Files.readString(egyenlegFajl(mappa), StandardCharsets.UTF_8).strip();
        // 2. Szöveg -> szám konverzió; hibás tartalom esetén érthető üzenet.
        long betoltott;
        try {
            betoltott = Long.parseLong(tartalom);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Hibás egyenlegfájl tartalom: '" + tartalom + "'", e);
        }
        if (betoltott < 0) {
            throw new IllegalArgumentException("A betöltött egyenleg nem lehet negatív: " + betoltott);
        }
        // 3. Beállítás és naplózás.
        egyenleg = betoltott;
        naploz("BETOLTES: egyenleg: " + egyenleg + " Ft");
    }

    /**
     * Kiírja az aktuális egyenleget a {@code egyenleg<szamlaszam>.txt} fájlba
     * (a régi tartalmat felülírja).
     *
     * @param mappa a cél könyvtár
     * @return a megírt fájl útvonala
     * @throws IOException ha a fájl nem írható
     */
    public Path egyenlegKiiras(Path mappa) throws IOException {
        // Files.writeString alapértelmezésben létrehozza / felülírja a fájlt.
        return Files.writeString(egyenlegFajl(mappa), Long.toString(egyenleg), StandardCharsets.UTF_8);
    }

    /**
     * Kiírja a tranzakciótörténetet a {@code tranzakcio<datum>.txt} fájlba,
     * soronként egy tranzakcióval. A dátumot paraméterként kapja, így
     * tesztelhető (nem függ a rendszeróra aktuális értékétől).
     *
     * @param mappa a cél könyvtár
     * @param datum a fájlnévbe kerülő dátum
     * @return a megírt fájl útvonala
     * @throws IOException ha a fájl nem írható
     */
    public Path tranzakciokKiirasa(Path mappa, LocalDate datum) throws IOException {
        // Files.write sorlistát ír, minden elem után sortöréssel.
        return Files.write(tranzakcioFajl(mappa, datum), tranzakciok, StandardCharsets.UTF_8);
    }

    /**
     * Kényelmi változat: a mai dátummal írja ki a tranzakciótörténetet.
     *
     * @param mappa a cél könyvtár
     * @return a megírt fájl útvonala
     * @throws IOException ha a fájl nem írható
     */
    public Path tranzakciokKiirasa(Path mappa) throws IOException {
        return tranzakciokKiirasa(mappa, LocalDate.now());
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

    /**
     * Egy bejegyzést fűz a tranzakciós naplóhoz.
     *
     * @param bejegyzes a tranzakció szöveges leírása
     */
    private void naploz(String bejegyzes) {
        tranzakciok.add(bejegyzes);
    }
}
