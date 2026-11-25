/**
 * A cég egy szolgáltatása: megnevezés és ár.
 */
public class Szolgaltatas implements CsvMentheto {

    private final String megnevezes;
    private final int ar;

    /**
     * @param megnevezes a szolgáltatás neve
     * @param ar         az ár forintban
     */
    public Szolgaltatas(String megnevezes, int ar) {
        this.megnevezes = megnevezes;
        this.ar = ar;
    }

    /** @return a CSV fejléc */
    @Override
    public String csvFejlec() {
        return String.join(ELVALASZTO, "Megnevezés", "Ár");
    }

    /** @return a szolgáltatás adatai CSV sorként */
    @Override
    public String csvSor() {
        return csvMezo(megnevezes) + ELVALASZTO + ar;
    }

    /** @return olvasható alak */
    @Override
    public String toString() {
        return megnevezes + " – " + ar + " Ft";
    }
}
