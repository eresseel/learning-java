import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Egy bolti polc, amely termékeket tárol tömbben, és implementálja az
 * Iterable interfészt – így bejárható a foreach (for (Termek t : polc)) szerkezettel.
 */
public class Polc implements Iterable<Termek> {

    private final Termek[] termekek;
    private int darab;

    /**
     * @param kapacitas a polc férőhelyeinek száma
     */
    public Polc(int kapacitas) {
        termekek = new Termek[kapacitas];
    }

    /**
     * @param t a polcra teendő termék
     * @return true, ha volt hely
     */
    public boolean feltesz(Termek t) {
        if (darab == termekek.length) {
            return false;
        }
        termekek[darab++] = t;
        return true;
    }

    /**
     * Az Iterable egyetlen kötelező metódusa: egy bejárót (Iterator) ad vissza.
     *
     * @return a polc elemeit sorban bejáró iterátor
     */
    @Override
    public Iterator<Termek> iterator() {
        // Névtelen belső osztály: helyben implementáljuk az Iterator interfészt.
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < darab;
            }

            @Override
            public Termek next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return termekek[index++];
            }
        };
    }
}
