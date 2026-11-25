package hu.learning.feladatlapok.mockito;

/**
 * Az {@link AuditNaplo} valódi megvalósítása, amely a konzolra ír.
 * <p>
 * Tesztben spy-ként használva a {@code doNothing().when(spy).ir(...)}
 * kikapcsolja ezt a mellékhatást (nem szemeteli tele a teszt kimenetét).
 */
public class KonzolNaplo implements AuditNaplo {

    /** Ennyi bejegyzést írt ki ténylegesen (a valódi logika része). */
    private int kiirtBejegyzesek;

    /** {@inheritDoc} */
    @Override
    public void ir(String uzenet) {
        // Mellékhatás: kiírás a konzolra + számláló növelése.
        System.out.println("[AUDIT] " + uzenet);
        kiirtBejegyzesek++;
    }

    /**
     * Visszaadja a ténylegesen kiírt bejegyzések számát.
     *
     * @return a kiírt bejegyzések száma
     */
    public int getKiirtBejegyzesek() {
        return kiirtBejegyzesek;
    }
}
