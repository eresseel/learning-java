/**
 * Polimorfizmus (többalakúság) bemutatása a Jarmu–Auto–Repulo hierarchián.
 * <p>
 * Ez az első lépése a leckéken átívelő "repülő dolgok" példaláncnak:
 * a 4. leckében a RepuloDolgok absztrakt osztály, az 5. leckében a
 * RepulInterface interfész folytatja.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. Polimorfizmus első jelentése: ős típusú változó tárolhat leszármazott objektumot.
        //    Jarmu val = new Auto(...);  Jarmu val = new Repulo(...);
        Jarmu val = new Auto("Családi autó");
        val.inditas();
        val = new Repulo("Utasszállító");
        val.inditas();

        // 2. Ezért az ős típusú tömbben vegyesen tárolhatunk autókat és repülőket.
        Jarmu[] jarmuvek = {
                new Auto("Taxi"),
                new Repulo("Sportrepülő"),
                new Jarmu("Általános jármű"),
                new Auto("Teherautó")
        };

        // 3. Polimorfizmus második jelentése: ugyanaz a hívás (inditas()),
        //    de mindig a tényleges objektum saját megvalósítása fut le.
        System.out.println();
        System.out.println("--- Minden jármű indítása egy ciklusban ---");
        for (Jarmu j : jarmuvek) {
            j.inditas();
        }
    }
}
