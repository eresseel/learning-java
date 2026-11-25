package hu.learning.feladatlapok.mockito;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link SzamlaRepository} egyszerű, memóriában (HashMap-ben) tároló valódi megvalósítása.
 * <p>
 * Tesztben spy-ként használjuk: a valódi tárolás működik, közben a hívásokat
 * ugyanúgy ellenőrizhetjük {@code verify}-jal, mint egy mocknál.
 */
public class MemoriaSzamlaRepository implements SzamlaRepository {

    /** Számlaszám → számla leképezés. */
    private final Map<String, Szamla> szamlak = new HashMap<>();

    /** {@inheritDoc} */
    @Override
    public Szamla findBySzamlaszam(String szamlaszam) {
        return szamlak.get(szamlaszam);
    }

    /** {@inheritDoc} */
    @Override
    public void save(Szamla szamla) {
        // A put felülírja a korábbi állapotot ugyanazon a kulcson.
        szamlak.put(szamla.szamlaszam(), szamla);
    }

    /** {@inheritDoc} */
    @Override
    public boolean letezik(String szamlaszam) {
        return szamlak.containsKey(szamlaszam);
    }

    /** {@inheritDoc} */
    @Override
    public int szamlakSzama() {
        return szamlak.size();
    }
}
