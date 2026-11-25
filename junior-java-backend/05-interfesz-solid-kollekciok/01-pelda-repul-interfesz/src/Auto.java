/**
 * Autó: jármű, de NEM repül, ezért nem implementálja a RepulInterface-t.
 * Jól mutatja, miért nem írhatná elő a közös ős (Minden vagy Jarmuvek) a repülést.
 */
public class Auto extends Jarmuvek {

    /**
     * @param nev az autó megnevezése
     */
    public Auto(String nev) {
        super(nev);
    }
}
