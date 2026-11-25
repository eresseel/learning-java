package hu.learning.webshop.tesztautomatizalas.taf.core;

/**
 * A tananyag VI. fejezetében szereplő tesztállapotok: Passed / Failed / TAS error / Inconclusive.
 *
 * <p>A JUnit csak „sikeres / elbukott / kihagyott” állapotot ismer; a TAS-nak viszont az is
 * fontos, hogy egy bukás <em>valószínűleg</em> a SUT hibája (az ellenőrzés nem teljesült) vagy a
 * tesztkörnyezeté (a teszt el sem jutott az ellenőrzésig).</p>
 */
public enum TestOutcome {

    /** A teszt lefutott, minden ellenőrzés teljesült. */
    PASSED,
    /** Egy ellenőrzés (assertion) nem teljesült – feltételezhetően SUT-hiba (de lásd: hamis pozitív!). */
    FAILED,
    /** A teszt nem jutott el az ellenőrzésig: kivétel a TAS-ban vagy a környezetben (NPE, hálózat, konfiguráció). */
    TAS_ERROR,
    /** Az előfeltétel nem teljesült (pl. preflight), a teszt megszakadt – az eredmény nem eldönthető. */
    INCONCLUSIVE;

    /**
     * Első szintű, automatikus osztályozás a kivétel típusa alapján.
     *
     * @param failure a teszt kivétele; {@code null}, ha sikeres volt
     * @return a tesztállapot
     */
    public static TestOutcome classify(Throwable failure) {
        // 1. Nincs kivétel → sikeres
        if (failure == null) {
            return PASSED;
        }
        // 2. Megszakított teszt (Assumptions.abort / assumeTrue) → nem eldönthető
        if (failure instanceof org.opentest4j.TestAbortedException) {
            return INCONCLUSIVE;
        }
        // 3. Ellenőrzés nem teljesült (JUnit, AssertJ, Hamcrest, REST Assured mind AssertionError-t dob)
        if (failure instanceof AssertionError) {
            return FAILED;
        }
        // 4. Minden más: a teszt maga vagy a környezete hibázott
        return TAS_ERROR;
    }
}
