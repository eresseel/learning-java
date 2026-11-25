/**
 * D – függőség invertálás: az általános adatkezelő absztrakció.
 * A magasabb szintű kód (RendelesKezelo) ettől függ, nem egy konkrét adatbázistól.
 */
public interface AdatTarolo {

    /** @param adat a mentendő adat */
    void ment(String adat);

    /** @return az eddig mentett adatok száma */
    int darab();
}
