package hu.learning.feladatlapok.mockito;

/**
 * Valódi (nem interfész) osztály a napi kamat kiszámítására.
 * <p>
 * A {@link #napiKamat(long, double)} tiszta számolás, tesztben is nyugodtan futhat.
 * Az {@link #aktualisKamatlab()} viszont a "valóságban" egy külső rendszerből
 * (pl. a jegybank szolgáltatásából) kérdezné le a kamatlábat – ezt tesztben
 * NEM akarjuk valóban meghívni. Ez a tipikus spy-helyzet: a logika nagy része
 * fusson, de egy metódust felülírunk.
 */
public class KamatKalkulator {

    /**
     * Kiszámolja egy nap kamatát az éves kamatlábból (365 napos évvel, kerekítve).
     *
     * @param egyenleg     a számla egyenlege
     * @param evesKamatlab az éves kamatláb tizedes törtként (pl. 0.0365 = 3,65%)
     * @return a napi kamat forintban
     */
    public long napiKamat(long egyenleg, double evesKamatlab) {
        return Math.round(egyenleg * evesKamatlab / 365);
    }

    /**
     * Lekérdezi az aktuális éves kamatlábat egy külső rendszerből.
     * <p>
     * A példában nincs valódi kapcsolat, ezért mindig kivételt dob – így a
     * tesztben azonnal kiderül, ha véletlenül a valódi metódus futott le.
     *
     * @return az éves kamatláb
     * @throws IllegalStateException mindig, mert nincs külső kapcsolat
     */
    public double aktualisKamatlab() {
        throw new IllegalStateException("Nincs kapcsolat a kamatláb-szolgáltatással!");
    }
}
