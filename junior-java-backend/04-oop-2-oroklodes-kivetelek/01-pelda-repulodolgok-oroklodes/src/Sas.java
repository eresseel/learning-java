/**
 * Sas: a Madar leszármazottja, amely ÚJRA felülírja a repul() metódust.
 */
public class Sas extends Madar {

    /**
     * @param nev a sas neve
     */
    public Sas(String nev) {
        super(nev, 220);
    }

    /**
     * A sas vitorlázva repül; a super.repul() hívással az ős (Madar)
     * változatát is lefuttatjuk, majd kiegészítjük.
     */
    @Override
    public void repul() {
        super.repul();
        System.out.println(nev + " a termikekben vitorlázik, alig mozdítja a szárnyát.");
    }
}
