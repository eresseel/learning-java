/**
 * Egy személy névvel és életkorral.
 * <p>
 * Az objektum maga ellenőrzi az adatait, és hibás érték esetén kivételt dob;
 * a hiba kezelését viszont a hívóra (feldolgozóra) hagyja.
 */
public class Szemely {

    private final String nev;
    private int eletkor;

    /**
     * @param nev     a személy neve (nem lehet üres)
     * @param eletkor az életkor években
     * @throws IllegalArgumentException ha a név üres (futási idejű kivétel)
     * @throws ErvenytelenEletkorException ha az életkor érvénytelen (ellenőrzött kivétel)
     */
    public Szemely(String nev, int eletkor) throws ErvenytelenEletkorException {
        // 1. Futási idejű (unchecked) kivétel: a throws-ban nem kötelező felsorolni.
        if (nev == null || nev.isBlank()) {
            throw new IllegalArgumentException("A név nem lehet üres!");
        }
        this.nev = nev;
        // 2. Az ellenőrzött kivételt a setEletkor dobhatja – mi továbbadjuk (throws).
        setEletkor(eletkor);
    }

    /**
     * @param eletkor az új életkor
     * @throws ErvenytelenEletkorException ha 0-nál kisebb vagy 130-nál nagyobb
     */
    public final void setEletkor(int eletkor) throws ErvenytelenEletkorException {
        if (eletkor < 0 || eletkor > 130) {
            // A kivétel is egy objektum, amelyet létrehozunk és "eldobunk".
            throw new ErvenytelenEletkorException(eletkor);
        }
        this.eletkor = eletkor;
    }

    /** @return a személy adatai */
    @Override
    public String toString() {
        return nev + " (" + eletkor + " éves)";
    }
}
