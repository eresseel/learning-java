/**
 * Síkidomok kerületét számoló, statikus metódusokat tartalmazó segédosztály.
 * <p>
 * A metódusok túltöltöttek (overload): azonos a nevük, de a paraméterlistájuk
 * (darabszám vagy típus) eltér, és a fordító a paraméterek alapján választ.
 * <p>
 * A négyzet és a kör kerülete is egyetlen számból számolható, így a túltöltésnél
 * ütköznének. A korábbi megoldásban egy logikai paraméter döntötte el, melyik
 * számítás fusson, például:
 * <pre>
 *     static double kerulet(double ertek, boolean kor) { ... }   // régi megoldás
 * </pre>
 * (vagy az int/double típuskülönbség: kerulet(5) négyzet, kerulet(5.0) kör – ez is törékeny).
 * Most a kör saját típust (Kor) kapott, így a túltöltés tisztán a típus alapján működik.
 */
public class Kerulet {

    /**
     * Téglalap kerülete: K = 2 * (a + b).
     *
     * @param a az egyik oldal
     * @param b a másik oldal
     * @return a kerület
     */
    public static double kerulet(double a, double b) {
        return 2 * (a + b);
    }

    /**
     * Négyzet kerülete: K = 4 * a.
     *
     * @param a az oldal hossza
     * @return a kerület
     */
    public static double kerulet(double a) {
        return 4 * a;
    }

    /**
     * Háromszög kerülete: K = a + b + c.
     *
     * @param a az első oldal
     * @param b a második oldal
     * @param c a harmadik oldal
     * @return a kerület
     */
    public static double kerulet(double a, double b, double c) {
        return a + b + c;
    }

    /**
     * Kör kerülete: K = 2 * r * π.
     * A paraméter típusa Kor – ez különbözteti meg a négyzet kerulet(double) változatától.
     *
     * @param kor a kör objektum
     * @return a kerület
     */
    public static double kerulet(Kor kor) {
        return 2 * kor.getSugar() * Math.PI;
    }
}
