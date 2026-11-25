/**
 * Az állatmenhely nyilvántartása: legfeljebb 20 állatot tárol egy tömbben,
 * felvételi sorrendben (a 0. indexen a legrégebben felvett állat van).
 */
public class Menhely {

    /** A menhely befogadóképessége. */
    public static final int KAPACITAS = 20;

    private final Allat[] allatok = new Allat[KAPACITAS];
    /** A tárolt állatok száma (egyben az első szabad index). */
    private int darab;

    /** @return true, ha nincs több szabad hely */
    public boolean isTele() {
        return darab == KAPACITAS;
    }

    /** @return true, ha nincs állat a menhelyen */
    public boolean isUres() {
        return darab == 0;
    }

    /** @return a tárolt állatok száma */
    public int getDarab() {
        return darab;
    }

    /**
     * Állat felvétele a tároló végére.
     *
     * @param allat a felveendő állat
     * @throws IllegalStateException ha a menhely megtelt
     */
    public void felvesz(Allat allat) {
        if (isTele()) {
            throw new IllegalStateException("A menhely megtelt, további állat nem vehető fel!");
        }
        allatok[darab] = allat;
        darab++;
    }

    /** Kiírja az összes állat összes adatát, sorszámozva. */
    public void listaz() {
        for (int i = 0; i < darab; i++) {
            // A toString() polimorf: Kutya és Macska saját adatai és ételadagja jelenik meg.
            System.out.printf("%2d. %s%n", i + 1, allatok[i]);
        }
    }

    /**
     * Menhely felszabadítása: a legrégebben felvett állat (0. index) kikerül,
     * a többi eggyel előrébb csúszik, így a sorrend megmarad.
     *
     * @return a kikerült állat
     * @throws IllegalStateException ha nincs állat a menhelyen
     */
    public Allat felszabadit() {
        if (isUres()) {
            throw new IllegalStateException("A menhely üres, nincs kit kiadni!");
        }
        // 1. Megjegyezzük a legrégebbi állatot.
        Allat kikerult = allatok[0];
        // 2. A többi elemet eggyel balra toljuk.
        for (int i = 1; i < darab; i++) {
            allatok[i - 1] = allatok[i];
        }
        // 3. Az utolsó, immár duplikált helyet kiürítjük, és csökkentjük a darabszámot.
        allatok[darab - 1] = null;
        darab--;
        return kikerult;
    }
}
