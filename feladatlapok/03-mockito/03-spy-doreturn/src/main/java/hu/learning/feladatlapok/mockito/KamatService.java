package hu.learning.feladatlapok.mockito;

/**
 * A napi kamat jóváírását végző szolgáltatás.
 * <p>
 * Függőségei: a {@link SzamlaRepository} (tárolás) és a {@link KamatKalkulator}
 * (számolás + kamatláb lekérdezés).
 */
public class KamatService {

    /** A számlák tárolója. */
    private final SzamlaRepository repository;
    /** A kamatszámító. */
    private final KamatKalkulator kalkulator;

    /**
     * Létrehozza a szolgáltatást.
     *
     * @param repository a számlák tárolója
     * @param kalkulator a kamatszámító
     */
    public KamatService(SzamlaRepository repository, KamatKalkulator kalkulator) {
        this.repository = repository;
        this.kalkulator = kalkulator;
    }

    /**
     * Jóváírja a számlán egy nap kamatát, és elmenti az új egyenleget.
     *
     * @param szamlaszam a számlaszám
     * @return a jóváírt kamat összege
     * @throws IllegalArgumentException ha nincs ilyen számla
     */
    public long kamatJovairas(String szamlaszam) {
        // 1. Számla betöltése.
        Szamla szamla = repository.findBySzamlaszam(szamlaszam);
        if (szamla == null) {
            throw new IllegalArgumentException("Nincs ilyen számla: " + szamlaszam);
        }
        // 2. Kamatláb lekérdezése (tesztben a spy-on felülírt metódus) és kamat számítása.
        double kamatlab = kalkulator.aktualisKamatlab();
        long kamat = kalkulator.napiKamat(szamla.egyenleg(), kamatlab);
        // 3. Új egyenleg mentése.
        repository.save(new Szamla(szamlaszam, szamla.egyenleg() + kamat));
        return kamat;
    }
}
