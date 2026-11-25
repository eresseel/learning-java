/**
 * Interfész bemutatása: a repülés képessége a Minden-hierarchián keresztbe
 * futó RepulInterface-be kerül.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. Minden objektum a Minden típusként kezelhető (osztályhierarchia szerinti polimorfizmus).
        Minden[] dolgok = {
                new Sas("Szirti sas"),
                new Vereb("Házi veréb"),
                new Auto("Családi autó"),
                new Repulo("Utasszállító"),
                new Labda("Strandlabda")
        };

        // 2. Végigmegyünk mindenen; repülni csak azt kérjük, aki implementálja az interfészt.
        System.out.println("=== Minden dolog ===");
        for (Minden m : dolgok) {
            if (m instanceof RepulInterface repulo) {
                // Mintaillesztés (pattern matching): az instanceof után azonnal
                // RepulInterface típusú változóként használható.
                System.out.print(m.bemutatkozik() + " – tud repülni: ");
                repulo.repul();
            } else {
                System.out.println(m.bemutatkozik() + " – nem tud repülni.");
            }
        }

        // 3. Az interfész is használható "gyűjtő típusként": csak repülők férnek bele.
        //    A hierarchia teljesen különböző ágairól jönnek, mégis egy tömbben vannak.
        RepulInterface[] repulok = {new Sas("Rétisas"), new Repulo("Vitorlázó"), new Labda("Röplabda")};
        System.out.println();
        System.out.println("=== Csak a repülők (RepulInterface tömb) ===");
        for (RepulInterface r : repulok) {
            r.repul();
        }

        // 4. Interfész konstans elérése (public static final).
        System.out.println();
        System.out.println("RepulInterface.maxSebesseg = " + RepulInterface.maxSebesseg);
    }
}
