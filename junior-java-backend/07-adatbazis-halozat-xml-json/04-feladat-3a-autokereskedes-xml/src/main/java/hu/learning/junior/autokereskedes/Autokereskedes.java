package hu.learning.junior.autokereskedes;

import java.util.List;

/**
 * Az autókereskedés: a neve és az autók listája (a teljes XML dokumentum
 * Java megfelelője).
 *
 * @param nev   a kereskedés neve (a gyökérelem {@code nev} attribútuma)
 * @param autok az autók
 */
public record Autokereskedes(String nev, List<Auto> autok) {
}
