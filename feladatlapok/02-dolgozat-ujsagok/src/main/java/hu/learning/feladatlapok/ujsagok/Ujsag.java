package hu.learning.feladatlapok.ujsagok;

/**
 * Általános újság (sajtótermék) – az osztályhierarchia ősosztálya (Feladat I.).
 * <p>
 * Minden újságról tudjuk a megnevezését, a kiadóját és az árát (egész forint).
 * A {@link Napilap} és a {@link Hetilap} ebből származik le.
 * <p>
 * Az ellenőrzés (Feladat III.) „osztályszinten” történik: a statikus
 * {@code ellenoriz...} metódusokat a setterek és a konzolos bekérő
 * ({@link UjsagBekero}) is használja, így a szabály egyetlen helyen van leírva.
 */
public class Ujsag {

    /** Az áfa mértéke: az ár 5%-a. */
    public static final double AFA_KULCS = 0.05;

    private String megnevezes;
    private String kiado;
    private int ar;

    /**
     * Létrehoz egy ellenőrzött újság objektumot.
     * A konstruktor a settereket hívja, így érvénytelen adattal nem jöhet létre objektum.
     *
     * @param megnevezes az újság neve (nem lehet üres)
     * @param kiado      a kiadó neve (nem lehet üres)
     * @param ar         az ár forintban (nem lehet negatív)
     * @throws IllegalArgumentException ha valamelyik adat érvénytelen
     */
    public Ujsag(String megnevezes, String kiado, int ar) {
        // A setterek végzik az ellenőrzést – a konstruktor nem duplikálja a szabályokat.
        setMegnevezes(megnevezes);
        setKiado(kiado);
        setAr(ar);
    }

    // ---------------------------------------------------------------
    // Osztályszintű ellenőrző metódusok (Feladat III. és IX.)
    // ---------------------------------------------------------------

    /**
     * Ellenőrzi, hogy egy szöveges mező (megnevezés, kiadó) nem üres-e.
     *
     * @param ertek    a vizsgált szöveg
     * @param mezoNev  a mező neve a hibaüzenethez
     * @return a szóközöktől megtisztított érték
     * @throws IllegalArgumentException ha az érték null vagy üres
     */
    public static String ellenorizNemUres(String ertek, String mezoNev) {
        // A null és a csak szóközökből álló szöveg is „üresnek” számít.
        if (ertek == null || ertek.isBlank()) {
            throw new IllegalArgumentException("A(z) " + mezoNev + " nem lehet üres!");
        }
        return ertek.trim();
    }

    /**
     * Ellenőrzi, hogy az ár nem negatív-e.
     *
     * @param ar a vizsgált ár
     * @return ugyanaz az ár, ha érvényes
     * @throws IllegalArgumentException ha az ár negatív
     */
    public static int ellenorizAr(int ar) {
        if (ar < 0) {
            throw new IllegalArgumentException("Az ár nem lehet negatív! (megadott: " + ar + ")");
        }
        return ar;
    }

    // ---------------------------------------------------------------
    // Üzleti metódusok (Feladat I.)
    // ---------------------------------------------------------------

    /**
     * Kiszámítja a sajtótermék áfatartalmát: az ár 5%-a.
     *
     * @return az áfa forintban (tört érték is lehet)
     */
    public double afaTartalomSzamit() {
        return ar * AFA_KULCS;
    }

    /**
     * Kiszámolja a termék beszerzésének heti költségét: ár × heti példányszám.
     * <p>
     * A metódus {@code final}, tehát leszármazott NEM írhatja felül (ezt kéri a feladat).
     * Hogy mégis típusonként eltérő eredményt adjon, a változó részt a
     * {@link #hetiPeldanyszam()} „sablonmetódusba” szerveztük ki – azt a
     * {@link Napilap} felülírja. Ez a <em>Template Method</em> tervezési minta.
     *
     * @return a heti beszerzési költség forintban
     */
    public final int hetiArkalkulacio() {
        // A számítás menete rögzített, csak a példányszám függ a típustól.
        return ar * hetiPeldanyszam();
    }

    /**
     * Megadja, hány példányt kell hetente beszerezni.
     * Általános újságnál és hetilapnál ez 1; a napilap felülírja.
     *
     * @return a heti példányszám
     */
    protected int hetiPeldanyszam() {
        return 1;
    }

    /**
     * Megadja a típus nevét úgy, ahogy a CSV fájlban szerepel.
     * A leszármazottak felülírják.
     *
     * @return „Ujsag”
     */
    public String tipus() {
        return "Ujsag";
    }

    // ---------------------------------------------------------------
    // Getterek és ellenőrzött setterek (Feladat III.)
    // ---------------------------------------------------------------

    /** @return az újság megnevezése */
    public String getMegnevezes() {
        return megnevezes;
    }

    /**
     * Beállítja a megnevezést.
     *
     * @param megnevezes az új megnevezés
     * @throws IllegalArgumentException ha üres
     */
    public void setMegnevezes(String megnevezes) {
        this.megnevezes = ellenorizNemUres(megnevezes, "megnevezés");
    }

    /** @return a kiadó neve */
    public String getKiado() {
        return kiado;
    }

    /**
     * Beállítja a kiadót.
     *
     * @param kiado az új kiadó
     * @throws IllegalArgumentException ha üres
     */
    public void setKiado(String kiado) {
        this.kiado = ellenorizNemUres(kiado, "kiadó");
    }

    /** @return az ár forintban */
    public int getAr() {
        return ar;
    }

    /**
     * Beállítja az árat.
     *
     * @param ar az új ár forintban
     * @throws IllegalArgumentException ha negatív
     */
    public void setAr(int ar) {
        this.ar = ellenorizAr(ar);
    }

    /**
     * Az ősosztályban tárolt adatok szövegesen. A leszármazottak kiegészítik
     * a saját mezőikkel ({@code super.adatok() + ...}).
     *
     * @return a mezők „név: érték” párjai vesszővel elválasztva
     */
    protected String adatok() {
        return "megnevezés: " + megnevezes + ", kiadó: " + kiado + ", ár: " + ar + " Ft";
    }

    /**
     * Az összes ismert adat szöveges formában (listázáshoz).
     *
     * @return olvasható leírás, pl. „Ujsag [megnevezés: ..., kiadó: ..., ár: 990 Ft]”
     */
    @Override
    public String toString() {
        return tipus() + " [" + adatok() + "]";
    }
}
