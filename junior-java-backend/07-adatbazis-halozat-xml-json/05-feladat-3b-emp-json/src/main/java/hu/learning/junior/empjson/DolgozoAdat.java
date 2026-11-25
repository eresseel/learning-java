package hu.learning.junior.empjson;

import java.math.BigDecimal;

/**
 * Egy dolgozó exportálandó adatai: név, munkakör, fizetés. A GSON a record
 * komponenseinek nevét használja JSON kulcsként ({@code "ename"},
 * {@code "job"}, {@code "sal"}).
 *
 * @param ename a dolgozó neve
 * @param job   munkakör
 * @param sal   fizetés
 */
public record DolgozoAdat(String ename, String job, BigDecimal sal) {
}
