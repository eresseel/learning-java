package hu.learning.junior.idezetek;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * A Quotable idézet-API JSON válaszának feldolgozása.
 *
 * <p>A válasz szerkezete (rövidítve):</p>
 * <pre>
 * { "data": [ { "content": "...", "author": { "name": "Charles Dickens", ... } }, ... ] }
 * </pre>
 * A JSON-t a GSON fa-modelljével ({@link JsonParser} → {@link JsonObject})
 * járjuk be, így nem kell a teljes szerkezethez Java osztályt írni.
 */
public final class QuotableParser {

    /** Csak statikus metódus van, nem példányosítjuk. */
    private QuotableParser() {
    }

    /**
     * Kinyeri az idézeteket a JSON válaszból.
     *
     * @param json    a HTTP válasz törzse
     * @param maximum legfeljebb ennyi idézetet ad vissza
     * @return az idézetek (üres lista, ha nincs {@code data} tömb)
     */
    public static List<Idezet> feldolgoz(String json, int maximum) {
        List<Idezet> idezetek = new ArrayList<>();
        // 1. A szöveg fává alakítása; a gyökér egy JSON objektum.
        JsonObject gyoker = JsonParser.parseString(json).getAsJsonObject();
        if (!gyoker.has("data")) {
            return idezetek;
        }
        // 2. A "data" tömb elemeinek bejárása, amíg el nem érjük a maximumot.
        for (JsonElement elem : gyoker.getAsJsonArray("data")) {
            if (idezetek.size() >= maximum) {
                break;
            }
            JsonObject idezet = elem.getAsJsonObject();
            String szoveg = idezet.get("content").getAsString();
            String szerzo = idezet.getAsJsonObject("author").get("name").getAsString();
            idezetek.add(new Idezet(szerzo, szoveg, "quotable"));
        }
        return idezetek;
    }
}
