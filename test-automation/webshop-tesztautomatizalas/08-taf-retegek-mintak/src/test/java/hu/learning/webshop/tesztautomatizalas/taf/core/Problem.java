package hu.learning.webshop.tesztautomatizalas.taf.core;

/**
 * Az RFC 9457 ProblemDetail hibaválasz tesztoldali modellje (+ a webshop {@code errorCode} mezője).
 *
 * <p>Az RFC szabvány, nem webshop-specifikus – ezért a core rétegben a helye.</p>
 *
 * @param type      a hibatípus URI-ja
 * @param title     rövid cím
 * @param status    HTTP státusz
 * @param detail    részletes üzenet
 * @param instance  a kérés útvonala
 * @param errorCode géppel olvasható hibakód
 */
public record Problem(String type, String title, int status, String detail, String instance, String errorCode) {
}
