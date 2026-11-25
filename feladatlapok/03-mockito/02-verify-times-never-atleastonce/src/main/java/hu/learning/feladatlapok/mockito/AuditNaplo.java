package hu.learning.feladatlapok.mockito;

/**
 * Auditnapló: minden pénzmozgást és elutasított műveletet rögzít.
 * <p>
 * Void metódusú függőség – a tesztekben a visszatérési értéke nem érdekes,
 * csak az, hogy MEGHÍVTÁK-e és milyen paraméterrel. Ezt {@code verify(...)}-val ellenőrizzük.
 */
public interface AuditNaplo {

    /**
     * Egy bejegyzést ír a naplóba.
     *
     * @param uzenet a naplóbejegyzés szövege
     */
    void ir(String uzenet);
}
