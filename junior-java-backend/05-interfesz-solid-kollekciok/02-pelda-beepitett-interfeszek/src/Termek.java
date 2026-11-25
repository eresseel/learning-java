/**
 * Termék névvel és árral, amely két beépített interfészt is implementál:
 * <ul>
 *   <li>Comparable – sorrend szerint összehasonlítható (compareTo()), itt ár szerint;</li>
 *   <li>Cloneable – klónozható (clone()).</li>
 * </ul>
 */
public class Termek implements Comparable<Termek>, Cloneable {

    private final String nev;
    private int ar;

    /**
     * @param nev a termék neve
     * @param ar  az ár forintban
     */
    public Termek(String nev, int ar) {
        this.nev = nev;
        this.ar = ar;
    }

    /** @return a termék neve */
    public String getNev() {
        return nev;
    }

    /** @return az ár forintban */
    public int getAr() {
        return ar;
    }

    /** @param ar az új ár forintban */
    public void setAr(int ar) {
        this.ar = ar;
    }

    /**
     * Természetes rendezés ár szerint növekvően.
     *
     * @param masik a másik termék
     * @return negatív, ha ez olcsóbb; 0, ha egyforma; pozitív, ha ez drágább
     */
    @Override
    public int compareTo(Termek masik) {
        // Integer.compare elkerüli a kivonásos megoldás túlcsordulási hibáját.
        return Integer.compare(this.ar, masik.ar);
    }

    /**
     * Másolatot készít a termékről. Az Object.clone() csak akkor működik,
     * ha az osztály implementálja a Cloneable jelölő interfészt.
     *
     * @return a termék másolata (új objektum)
     */
    @Override
    public Termek clone() {
        try {
            return (Termek) super.clone();
        } catch (CloneNotSupportedException e) {
            // Nem fordulhat elő, mert implementáljuk a Cloneable-t.
            throw new AssertionError("A klónozás nem sikerült", e);
        }
    }

    /** @return név és ár */
    @Override
    public String toString() {
        return nev + " (" + ar + " Ft)";
    }
}
