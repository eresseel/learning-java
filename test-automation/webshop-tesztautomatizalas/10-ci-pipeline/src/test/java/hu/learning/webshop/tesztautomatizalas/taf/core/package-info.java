/**
 * <b>TAF 3. réteg – alapkódkönyvtárak (core libraries).</b>
 *
 * <p>Üzleti tudás nélküli, általános építőelemek: HTTP kliens ({@link
 * hu.learning.webshop.tesztautomatizalas.taf.core.ApiClient}), konfiguráció ({@link
 * hu.learning.webshop.tesztautomatizalas.taf.core.Config}), JSON-egyeztetés, külső szolgáltatás
 * szimulátora. Ez a réteg nem tudja, mi az a „kosár” – bármely REST API tesztelésére használható.</p>
 *
 * <p>Szabály: a core réteg csak a külső könyvtárakra (REST Assured, Jackson, WireMock) épülhet,
 * a felsőbb rétegekre (business, tests) soha nem hivatkozhat.</p>
 */
package hu.learning.webshop.tesztautomatizalas.taf.core;
