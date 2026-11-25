/**
 * Síkidom absztrakció (O, L, D elvek): minden síkidom meg tudja mondani a területét.
 * A területösszegző csak ezt az interfészt ismeri.
 */
public interface Sikidom {

    /** @return a síkidom területe */
    double terulet();

    /** @return a síkidom neve a kiíráshoz */
    String nev();
}
