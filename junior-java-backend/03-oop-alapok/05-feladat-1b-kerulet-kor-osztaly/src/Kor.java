/**
 * Kör: egyetlen adata a sugár.
 * <p>
 * Azért hoztuk létre, hogy a kerületszámító metódus túltöltésénél a kört
 * ne egy logikai "jelző" paraméterrel kelljen megkülönböztetni a négyzettől
 * (mindkettőnek egyetlen double adata van), hanem saját típussal.
 */
public class Kor {

    private final double sugar;

    /**
     * @param sugar a kör sugara (pozitív szám)
     */
    public Kor(double sugar) {
        this.sugar = sugar;
    }

    /** @return a kör sugara */
    public double getSugar() {
        return sugar;
    }
}
