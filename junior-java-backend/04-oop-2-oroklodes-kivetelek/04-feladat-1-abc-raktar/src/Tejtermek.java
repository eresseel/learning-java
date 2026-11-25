/**
 * Tejtermék: a Termek leszármazottja, plusz adata a zsírtartalom (%).
 */
public class Tejtermek extends Termek {

    private final double zsirtartalom;

    /**
     * @param megnevezes   a termék neve
     * @param cikkszam     a cikkszám
     * @param ar           az egységár forintban
     * @param darabszam    a darabszám
     * @param zsirtartalom a zsírtartalom százalékban (0–100)
     * @throws IllegalArgumentException ha bármelyik adat érvénytelen
     */
    public Tejtermek(String megnevezes, String cikkszam, int ar, int darabszam, double zsirtartalom) {
        // 1. Az ős konstruktora ellenőrzi és beállítja a közös adatokat.
        super(megnevezes, cikkszam, ar, darabszam);
        // 2. Utána a saját, specifikus adat ellenőrzése.
        if (zsirtartalom < 0 || zsirtartalom > 100) {
            throw new IllegalArgumentException("A zsírtartalom 0 és 100% között lehet (kapott: " + zsirtartalom + ")!");
        }
        this.zsirtartalom = zsirtartalom;
    }

    /** @return a zsírtartalom százalékban */
    public double getZsirtartalom() {
        return zsirtartalom;
    }

    /** @return "Tejtermék" */
    @Override
    public String kategoria() {
        return "Tejtermék";
    }

    /** @return a zsírtartalom szövegesen */
    @Override
    protected String extraInfo() {
        return "zsír: " + zsirtartalom + "%";
    }
}
