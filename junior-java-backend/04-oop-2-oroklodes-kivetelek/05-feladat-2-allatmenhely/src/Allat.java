/**
 * Általános állat a menhelyen – ABSZTRAKT ős, így önmagában nem jöhet létre,
 * csak kutya vagy macska.
 * <p>
 * Közös adatok: név (nem lehet üres) és súly (minimum 2 kg).
 * Előírja a leszármazottaknak a napiEtelAdag() metódust.
 */
public abstract class Allat {

    /** A megengedett legkisebb súly kilogrammban. */
    public static final double MIN_SULY_KG = 2.0;

    private final String nev;
    private final double sulyKg;

    /**
     * @param nev    az állat neve (nem lehet üres)
     * @param sulyKg a súly kilogrammban (legalább 2 kg)
     * @throws IllegalArgumentException ha a név üres vagy a súly 2 kg alatti
     */
    protected Allat(String nev, double sulyKg) {
        // 1. Név ellenőrzése: üres vagy csak szóközből álló név nem fogadható el.
        if (nev == null || nev.isBlank()) {
            throw new IllegalArgumentException("A név nem lehet üres!");
        }
        // 2. Súly ellenőrzése: legalább 2 kg.
        if (sulyKg < MIN_SULY_KG) {
            throw new IllegalArgumentException("A súly legalább " + MIN_SULY_KG + " kg kell legyen!");
        }
        this.nev = nev.trim();
        this.sulyKg = sulyKg;
    }

    /** @return az állat neve */
    public String getNev() {
        return nev;
    }

    /** @return a súly kilogrammban */
    public double getSulyKg() {
        return sulyKg;
    }

    /**
     * Kiszámolja a napi ételadagot a súly alapján. Minden állatfaj másképp
     * számol, ezért az ős csak előírja (absztrakt metódus).
     *
     * @return a napi ételadag kilogrammban
     */
    public abstract double napiEtelAdag();

    /**
     * A faj-specifikus adatok szövegesen – a leszármazottak adják meg.
     *
     * @return pl. "marmagasság: 45 cm"
     */
    protected abstract String sajatAdatok();

    /**
     * Az állat összes adata egy sorban, a napi ételadaggal a végén.
     *
     * @return a listázáshoz használt sor
     */
    @Override
    public String toString() {
        return String.format("%-7s %-12s %6.1f kg, %-22s napi ételadag: %.3f kg",
                getClass().getSimpleName(), nev, sulyKg, sajatAdatok() + ",", napiEtelAdag());
    }
}
