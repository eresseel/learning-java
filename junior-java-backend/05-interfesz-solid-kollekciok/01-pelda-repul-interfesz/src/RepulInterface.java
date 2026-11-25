/**
 * A repülés képességét leíró interfész.
 * <p>
 * Az interfész az OO hierarchián kívül álló formai megkötés: aki implementálja,
 * köteles megvalósítani a repul() metódust. Így a hierarchia különböző ágain
 * lévő osztályok (Sas, Vereb, Repulo, Labda) közös típusként kezelhetők,
 * pedig legközelebbi közös ősük, a Minden, nem kényszerítheti rájuk a repülést.
 * <p>
 * Megjegyzés: a diasoron szereplő "interface RepulInterface() { ... }" írásmódban
 * a zárójel elírás – interfésznév után nem áll zárójel.
 */
public interface RepulInterface {

    /**
     * Konstans: interfészben minden mező automatikusan public static final.
     * Az érték a diasor példájából való (mértékegységet a diasor nem ad meg).
     */
    int maxSebesseg = 300000;

    /**
     * Repülés – interfészben a metódus automatikusan public abstract.
     */
    void repul();
}
