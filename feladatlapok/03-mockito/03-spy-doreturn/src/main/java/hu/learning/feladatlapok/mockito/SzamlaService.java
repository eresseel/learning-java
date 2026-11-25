package hu.learning.feladatlapok.mockito;

/**
 * A bankszámla üzleti logikája: egyenleg lekérdezés, befizetés, kifizetés.
 * <p>
 * Az osztály két függősége a {@link SzamlaRepository} és az {@link AuditNaplo},
 * mindkettőt konstruktorban kapja meg (dependency injection). Emiatt tesztelhető:
 * a tesztben mindkettő helyére mockot adunk át, és ellenőrizzük a hívásokat.
 */
public class SzamlaService {

    /** A tárolási réteg, amitől a szolgáltatás függ. */
    private final SzamlaRepository repository;
    /** Az auditnapló, ahová a pénzmozgások kerülnek. */
    private final AuditNaplo naplo;

    /**
     * Létrehozza a szolgáltatást.
     *
     * @param repository a számlákat tároló réteg
     * @param naplo      az auditnapló
     */
    public SzamlaService(SzamlaRepository repository, AuditNaplo naplo) {
        this.repository = repository;
        this.naplo = naplo;
    }

    /**
     * Lekérdezi a számla egyenlegét.
     *
     * @param szamlaszam a számlaszám
     * @return az aktuális egyenleg
     * @throws IllegalArgumentException ha nincs ilyen számla
     */
    public long egyenlegLekerdezes(String szamlaszam) {
        return betolt(szamlaszam).egyenleg();
    }

    /**
     * Befizet a számlára, majd elmenti az új állapotot.
     *
     * @param szamlaszam a számlaszám
     * @param osszeg     a befizetett összeg (pozitív)
     * @return az új egyenleg
     * @throws IllegalArgumentException ha az összeg nem pozitív, vagy nincs ilyen számla
     */
    public long befizetes(String szamlaszam, long osszeg) {
        // 1. Ellenőrizzük a bemenetet: csak pozitív összeget lehet befizetni.
        if (osszeg <= 0) {
            throw new IllegalArgumentException("A befizetett összegnek pozitívnak kell lennie!");
        }
        // 2. Betöltjük a számlát a repositoryból (tesztben: a mock adja vissza).
        Szamla regi = betolt(szamlaszam);
        // 3. Új példányt készítünk a megnövelt egyenleggel, és elmentjük.
        Szamla uj = new Szamla(szamlaszam, regi.egyenleg() + osszeg);
        repository.save(uj);
        // 4. Naplózunk – a tesztben verify()-jal ellenőrizzük, hogy ez megtörtént.
        naplo.ir("BEFIZETES " + szamlaszam + " " + osszeg);
        return uj.egyenleg();
    }

    /**
     * Kifizet a számláról, ha van rá fedezet.
     *
     * @param szamlaszam a számlaszám
     * @param osszeg     a kifizetendő összeg (pozitív)
     * @return az új egyenleg
     * @throws IllegalArgumentException ha az összeg nem pozitív, vagy nincs ilyen számla
     * @throws FedezethianyException    ha az egyenleg kisebb, mint az összeg
     */
    public long kifizetes(String szamlaszam, long osszeg) {
        // 1. Bemenet ellenőrzése.
        if (osszeg <= 0) {
            throw new IllegalArgumentException("A kifizetett összegnek pozitívnak kell lennie!");
        }
        // 2. Számla betöltése és fedezet vizsgálata.
        Szamla regi = betolt(szamlaszam);
        if (regi.egyenleg() < osszeg) {
            // Elutasított művelet: naplózzuk, de NEM mentünk (verify(..., never())).
            naplo.ir("ELUTASITVA " + szamlaszam + " " + osszeg);
            throw new FedezethianyException("Nincs elegendő fedezet a(z) " + szamlaszam + " számlán!");
        }
        // 3. Új állapot mentése.
        Szamla uj = new Szamla(szamlaszam, regi.egyenleg() - osszeg);
        repository.save(uj);
        // 4. Sikeres kifizetés naplózása.
        naplo.ir("KIFIZETES " + szamlaszam + " " + osszeg);
        return uj.egyenleg();
    }

    /**
     * Betölti a számlát; ha nem létezik, kivételt dob.
     *
     * @param szamlaszam a számlaszám
     * @return a betöltött számla
     * @throws IllegalArgumentException ha nincs ilyen számla
     */
    private Szamla betolt(String szamlaszam) {
        Szamla szamla = repository.findBySzamlaszam(szamlaszam);
        // A repository null-t ad, ha nincs találat – ezt üzleti hibává alakítjuk.
        if (szamla == null) {
            throw new IllegalArgumentException("Nincs ilyen számla: " + szamlaszam);
        }
        return szamla;
    }
}
