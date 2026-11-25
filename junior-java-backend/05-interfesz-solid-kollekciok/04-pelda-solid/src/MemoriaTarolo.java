import java.util.ArrayList;
import java.util.List;

/**
 * Egy konkrét adatkezelő: memóriában (listában) tárol.
 */
public class MemoriaTarolo implements AdatTarolo {

    private final List<String> adatok = new ArrayList<>();

    /** @param adat a mentendő adat */
    @Override
    public void ment(String adat) {
        adatok.add(adat);
        System.out.println("  [memória] mentve: " + adat);
    }

    /** @return a tárolt elemek száma */
    @Override
    public int darab() {
        return adatok.size();
    }
}
