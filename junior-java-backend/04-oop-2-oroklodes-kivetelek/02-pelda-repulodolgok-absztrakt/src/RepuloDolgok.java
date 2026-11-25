/**
 * Repülő dolgok – ABSZTRAKT ősosztály.
 * <p>
 * Egyetlen feladata, hogy közös őst adjon a Madar, Repulo és Labda osztályoknak,
 * és rájuk kényszerítse a repul() metódust. Önállóan nem példányosítható
 * (new RepuloDolgok(...) fordítási hiba), és a repul()-t sem tudja maga
 * megvalósítani, hiszen a madár, a repülőgép és a labda is másképp repül.
 */
public abstract class RepuloDolgok {

    protected String nev;

    /**
     * Az absztrakt osztálynak is lehet konstruktora: a leszármazottak
     * super(...) hívása futtatja (az ős rész "implicit példányosodik").
     *
     * @param nev a repülő dolog megnevezése
     */
    protected RepuloDolgok(String nev) {
        this.nev = nev;
    }

    /** @return a megnevezés */
    public String getNev() {
        return nev;
    }

    /**
     * Absztrakt metódus: az ős csak előírja, törzse nincs.
     * Minden nem absztrakt leszármazott köteles megvalósítani.
     */
    public abstract void repul();

    /**
     * Teljesen megvalósított (nem absztrakt) metódus az absztrakt osztályban.
     * Úgy használja a repul()-t, mintha már implementálva lenne – a fordító
     * "megelőlegezi", mert futáskor biztosan egy leszármazott objektumon hívódik.
     */
    public void bemutat() {
        System.out.print("[" + getClass().getSimpleName() + "] ");
        repul();
    }
}
