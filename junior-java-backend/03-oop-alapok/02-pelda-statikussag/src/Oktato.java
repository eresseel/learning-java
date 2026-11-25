/**
 * Egy oktatót modellez, és közben bemutatja a statikusság (osztályszintűség)
 * minden formáját: statikus adattag, statikus metódus, statikus blokk
 * és statikus beágyazott osztály.
 */
public class Oktato {

    // --- Statikus adattagok: csak EGY létezik belőlük, akárhány példány van ---

    /** Minden oktatónál azonos érték, ezért elég osztályszinten tárolni. */
    static String intezmeny = "Ruander Oktatóközpont";

    /** Példánytól független számláló: hány Oktato objektum jött létre eddig. */
    static int oktatokSzama;

    // --- Statikus blokk: az osztály első használatakor, egyszer fut le ---
    static {
        // A statikus változók inicializálására használjuk.
        // A statikus mezők és blokkok a forráskódbeli sorrendjükben értékelődnek ki.
        oktatokSzama = 0;
        System.out.println("[statikus blokk 1] Az Oktato osztály betöltődött.");
    }

    /** Egy osztályban több statikus blokk is lehet – sorrendben futnak le. */
    static {
        System.out.println("[statikus blokk 2] Intézmény: " + intezmeny);
    }

    // --- Példányszintű (nem statikus) adattag: minden objektumnak saját ---
    private String oktatoNeve;

    /** Az oktató fő tantárgya – a beágyazott osztály példánya. */
    private Tantargy tantargy;

    /**
     * Létrehoz egy oktatót, és megnöveli az osztályszintű számlálót.
     *
     * @param oktatoNeve az oktató neve
     * @param tantargy   az oktató által tanított tantárgy
     */
    public Oktato(String oktatoNeve, Tantargy tantargy) {
        this.oktatoNeve = oktatoNeve;
        this.tantargy = tantargy;
        // A nem statikus elem (konstruktor) eléri a statikus mezőt.
        oktatokSzama++;
    }

    /**
     * Statikus metódus: példányosítás nélkül, az osztálynéven keresztül hívható
     * (Oktato.getOktatokSzama()). Nem éri el a nem statikus mezőket (pl. oktatoNeve)!
     *
     * @return az eddig létrehozott oktatók száma
     */
    public static int getOktatokSzama() {
        return oktatokSzama;
    }

    /**
     * Példánymetódus: eléri a saját (nem statikus) és a statikus mezőket is.
     *
     * @return az oktató bemutatkozó szövege
     */
    public String bemutatkozas() {
        return oktatoNeve + " (" + intezmeny + ") – " + tantargy;
    }

    /**
     * Statikus beágyazott osztály: csak az Oktato számára hasznos segítő osztály,
     * ezért vele együtt tartjuk. Normál (külső) osztály nem lehet statikus,
     * a beágyazott viszont igen. Kívülről: new Oktato.Tantargy(...).
     */
    static class Tantargy {

        private final String nev;
        private final int oraszam;

        /**
         * @param nev     a tantárgy neve
         * @param oraszam a tantárgy óraszáma
         */
        Tantargy(String nev, int oraszam) {
            this.nev = nev;
            this.oraszam = oraszam;
        }

        /** @return a tantárgy szöveges alakja */
        @Override
        public String toString() {
            return nev + ", " + oraszam + " óra";
        }
    }
}
