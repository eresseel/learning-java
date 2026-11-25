/**
 * A cég egy kapcsolata (ügyfél, partner): név, cím, e-mail, telefon.
 */
public class Kapcsolat implements CsvMentheto {

    private final String nev;
    private final String cim;
    private final String email;
    private final String telefon;

    /**
     * @param nev     a kapcsolat neve
     * @param cim     a postai cím
     * @param email   az e-mail cím
     * @param telefon a telefonszám
     */
    public Kapcsolat(String nev, String cim, String email, String telefon) {
        this.nev = nev;
        this.cim = cim;
        this.email = email;
        this.telefon = telefon;
    }

    /** @return a CSV fejléc */
    @Override
    public String csvFejlec() {
        return String.join(ELVALASZTO, "Név", "Cím", "Email", "Telefon");
    }

    /** @return a kapcsolat adatai CSV sorként */
    @Override
    public String csvSor() {
        return String.join(ELVALASZTO, csvMezo(nev), csvMezo(cim), csvMezo(email), csvMezo(telefon));
    }

    /** @return olvasható alak */
    @Override
    public String toString() {
        return nev + " | " + cim + " | " + email + " | " + telefon;
    }
}
