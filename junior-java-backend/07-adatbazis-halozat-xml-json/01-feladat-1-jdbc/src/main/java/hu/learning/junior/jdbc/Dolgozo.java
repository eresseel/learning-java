package hu.learning.junior.jdbc;

import java.math.BigDecimal;

/**
 * Az {@code emp} tábla egy sorának (egy dolgozónak) a programban használt
 * adatai. A {@code record} tömör, megváltoztathatatlan adathordozó osztály:
 * a konstruktort, gettereket, {@code equals}/{@code hashCode}/{@code toString}
 * metódusokat a fordító generálja.
 *
 * @param empno  a dolgozó azonosítója (EMPNO)
 * @param ename  a dolgozó neve (ENAME)
 * @param job    munkakör (JOB)
 * @param sal    fizetés (SAL) – pénzösszeghez {@link BigDecimal}, nem double!
 * @param deptno osztály azonosítója (DEPTNO)
 */
public record Dolgozo(int empno, String ename, String job, BigDecimal sal, int deptno) {
}
