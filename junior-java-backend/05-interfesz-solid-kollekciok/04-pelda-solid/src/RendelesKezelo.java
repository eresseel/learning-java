/**
 * Üzleti logikai (BL) osztály. A tárolót kívülről kapja (konstruktoron át),
 * és csak az AdatTarolo interfészt ismeri – így a tároló cseréjekor
 * ezt az osztályt nem kell módosítani (függőség invertálás elve).
 */
public class RendelesKezelo {

    private final AdatTarolo tarolo;

    /**
     * @param tarolo bármilyen AdatTarolo megvalósítás
     */
    public RendelesKezelo(AdatTarolo tarolo) {
        this.tarolo = tarolo;
    }

    /**
     * Rendelés rögzítése: ellenőrzés (üzleti szabály), majd mentés a tárolóba.
     *
     * @param termek    a rendelt termék
     * @param mennyiseg a mennyiség
     */
    public void rendel(String termek, int mennyiseg) {
        if (mennyiseg <= 0) {
            System.out.println("  Érvénytelen mennyiség, a rendelés elutasítva: " + termek);
            return;
        }
        tarolo.ment(termek + " x " + mennyiseg);
    }

    /** @return a rögzített rendelések száma */
    public int rendelesekSzama() {
        return tarolo.darab();
    }
}
