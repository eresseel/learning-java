package hu.laci.learning.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A JSONPlaceholder "post" erőforrásának Java modellje (POJO / record).
 * <p>
 * A Jackson ebből készít JSON-t (kérés törzse), és ebbe olvassa vissza a JSON választ.
 * A record Java 16+ óta tömör módja az adatot hordozó, változtathatatlan osztálynak.
 *
 * @param userId a szerző azonosítója
 * @param id     a bejegyzés azonosítója (létrehozáskor null, a szerver adja)
 * @param title  a cím
 * @param body   a szöveg
 */
// NON_NULL: a null mezőket (pl. a létrehozáskor még nem létező id-t) nem küldjük el a JSON-ban.
@JsonInclude(JsonInclude.Include.NON_NULL)
// ignoreUnknown: ha a szerver olyan mezőt is küld, amit a modell nem ismer, ne dobjon hibát.
@JsonIgnoreProperties(ignoreUnknown = true)
public record Post(Integer userId, Integer id, String title, String body) {
}
