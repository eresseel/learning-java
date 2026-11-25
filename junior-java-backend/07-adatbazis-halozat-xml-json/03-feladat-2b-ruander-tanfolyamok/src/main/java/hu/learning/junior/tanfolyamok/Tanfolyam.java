package hu.learning.junior.tanfolyamok;

/**
 * Egy tanfolyam a ruander.hu oldalról.
 *
 * @param megnevezes a tanfolyam neve
 * @param ar         bruttó ár forintban
 */
public record Tanfolyam(String megnevezes, int ar) {
}
