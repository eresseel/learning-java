package hu.learning.feladatlapok.mockito;

/**
 * Egy bankszámla pillanatnyi állapotát modellezi.
 * <p>
 * Record, tehát megváltoztathatatlan (immutable): ha változik az egyenleg,
 * a szolgáltatás egy ÚJ {@code Szamla} példányt hoz létre és azt menti el.
 *
 * @param szamlaszam a számla azonosítója (szöveges)
 * @param egyenleg   az aktuális egyenleg forintban (egész szám)
 */
public record Szamla(String szamlaszam, long egyenleg) {
}
