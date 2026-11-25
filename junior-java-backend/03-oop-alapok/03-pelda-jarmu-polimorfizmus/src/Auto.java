/**
 * Autó: a Jarmu specializált leszármazottja (minden autó egy speciális jármű).
 */
public class Auto extends Jarmu {

    /**
     * @param nev az autó megnevezése
     */
    public Auto(String nev) {
        // Az ős konstruktorát a super(...) hívással futtatjuk le.
        super(nev);
    }

    /**
     * Az autó a saját módján indul: felülírjuk (override) az ős metódusát.
     */
    @Override
    public void inditas() {
        System.out.println(nev + ": elfordítjuk a kulcsot, a motor beindul, sebességbe tesszük.");
    }
}
