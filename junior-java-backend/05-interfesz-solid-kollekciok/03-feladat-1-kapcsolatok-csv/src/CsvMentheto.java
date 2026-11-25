/**
 * Közös interfész mindenhez, ami CSV formátumban menthető.
 * <p>
 * A Kapcsolat és a Szolgaltatas két teljesen különböző osztály, közös ősük
 * nincs (az Object-en kívül). Az interfész biztosítja, hogy mindkettő
 * ugyanúgy, egységesen menthető legyen, és a mentést végző kód csak ezt
 * az absztrakciót ismerje, ne a konkrét osztályokat.
 */
public interface CsvMentheto {

    /** A CSV mezőelválasztó karaktere (a magyar Excel a pontosvesszőt várja). */
    String ELVALASZTO = ";";

    /**
     * @return a CSV fájl fejléce (oszlopnevek)
     */
    String csvFejlec();

    /**
     * @return az objektum adatai egy CSV sorként
     */
    String csvSor();

    /**
     * Alapértelmezett (default) metódus: implementációval rendelkezik, minden
     * implementáló osztály megkapja. Egy mezőt alakít CSV-biztossá: ha
     * elválasztót, idézőjelet vagy sortörést tartalmaz, idézőjelek közé teszi.
     *
     * @param mezo a mező értéke
     * @return a CSV-be írható alak
     */
    default String csvMezo(String mezo) {
        if (mezo.contains(ELVALASZTO) || mezo.contains("\"") || mezo.contains("\n")) {
            return "\"" + mezo.replace("\"", "\"\"") + "\"";
        }
        return mezo;
    }
}
