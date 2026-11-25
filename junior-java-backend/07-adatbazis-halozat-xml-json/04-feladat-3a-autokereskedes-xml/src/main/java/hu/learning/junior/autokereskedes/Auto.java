package hu.learning.junior.autokereskedes;

/**
 * Egy eladó autó adatai, az XML egy {@code <auto>} elemének megfelelően.
 *
 * @param id      egyedi azonosító (az {@code id} attribútum)
 * @param marka   gyártó
 * @param tipus   típus
 * @param evjarat gyártási év
 * @param ar      ár forintban
 * @param szin    szín
 */
public record Auto(int id, String marka, String tipus, int evjarat, int ar, String szin) {
}
