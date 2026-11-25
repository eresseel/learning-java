/**
 * Egy felhasználó: azonosító, jogosultság, név.
 */
public class Felhasznalo {

    private final int azonosito;
    private final Jogosultsag jogosultsag;
    private final String nev;

    /**
     * @param azonosito   egyedi azonosító
     * @param jogosultsag a jogosultság
     * @param nev         a felhasználó neve
     */
    public Felhasznalo(int azonosito, Jogosultsag jogosultsag, String nev) {
        this.azonosito = azonosito;
        this.jogosultsag = jogosultsag;
        this.nev = nev;
    }

    /**
     * Egy fájlsor ("azonosito;jogosultsag;nev") feldolgozása objektummá.
     * Statikus "gyártó" metódus: a feldolgozás logikája az osztálynál marad.
     *
     * @param sor a fájl egy sora
     * @return az új felhasználó
     * @throws IllegalArgumentException ha a sor formátuma vagy tartalma hibás
     *         (a NumberFormatException is ennek leszármazottja)
     */
    public static Felhasznalo sorbol(String sor) {
        String[] reszek = sor.split(";");
        if (reszek.length != 3) {
            throw new IllegalArgumentException("Hibás mezőszám: " + sor);
        }
        int id = Integer.parseInt(reszek[0].trim());
        Jogosultsag j = Jogosultsag.kodAlapjan(Integer.parseInt(reszek[1].trim()));
        return new Felhasznalo(id, j, reszek[2].trim());
    }

    /** @return az azonosító */
    public int getAzonosito() {
        return azonosito;
    }

    /** @return a jogosultság */
    public Jogosultsag getJogosultsag() {
        return jogosultsag;
    }

    /** @return a név */
    public String getNev() {
        return nev;
    }

    /** @return táblázatos sor */
    @Override
    public String toString() {
        return String.format("%5d  %-12s %s", azonosito, jogosultsag.getMegnevezes(), nev);
    }
}
