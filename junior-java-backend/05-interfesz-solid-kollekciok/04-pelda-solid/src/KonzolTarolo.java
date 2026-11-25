/**
 * Egy másik konkrét adatkezelő (pl. egy másik "adatbázis"): csak kiírja és számol.
 * Lecserélhető a MemoriaTarolo-ra a RendelesKezelo módosítása nélkül.
 */
public class KonzolTarolo implements AdatTarolo {

    private int darab;

    /** @param adat a mentendő adat */
    @Override
    public void ment(String adat) {
        darab++;
        System.out.println("  [konzol] #" + darab + " " + adat);
    }

    /** @return az eddig "mentett" adatok száma */
    @Override
    public int darab() {
        return darab;
    }
}
