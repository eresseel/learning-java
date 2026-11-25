/**
 * <b>Rendszertesztek telepítés után</b> (Failsafe, {@code *IT}).
 *
 * <p>Ezek a tesztek nem indítanak Spring kontextust: egy már futó (telepített) SUT-ot hívnak
 * a {@code webshop.baseUrl} / {@code WEBSHOP_BASE_URL} címen, feketedobozos módon. Ugyanazokat
 * a TAF rétegeket (üzleti lépések, ApiClient, preflight, eredmény-osztályozás) használják, mint
 * a build-szakasz tesztjei – csak a környezet más (V. fejezet: tesztszintek a CI/CD-ben).</p>
 */
package hu.learning.webshop.tesztautomatizalas.systemtests;
