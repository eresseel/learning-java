package hu.learning.webshop.tesztautomatizalas.api;

/**
 * A ProblemDetail hibaválasz <b>tesztoldali</b> POJO-ja (record).
 *
 * <p>Szándékosan nem a szerver osztályát használjuk: ez a fogyasztó (kliens) nézőpontja.
 * A REST Assured alapértelmezett Jackson beállítása ismeretlen mezőnél hibát dob –
 * ha a szerver új mezőt ad a válaszhoz, ez a teszt jelezni fogja (korai figyelmeztetés).</p>
 *
 * @param type      a hibatípus URI-ja
 * @param title     rövid cím
 * @param status    HTTP státusz
 * @param detail    részletes üzenet
 * @param instance  a kérés útvonala
 * @param errorCode saját, géppel olvasható hibakód
 */
public record Problem(String type, String title, int status, String detail, String instance, String errorCode) {
}
