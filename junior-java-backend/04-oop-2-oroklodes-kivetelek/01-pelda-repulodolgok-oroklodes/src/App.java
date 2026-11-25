/**
 * Öröklődés és metódus-felülbírálás bemutatása a RepuloDolgok → Madar → Sas/Vereb hierarchián.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. Polimorfizmus: RepuloDolgok obj = new Madar(...);
        RepuloDolgok obj = new Madar("Egy madár", 30);
        obj.repul();

        // 2. Ős típusú tömb vegyes elemekkel.
        RepuloDolgok[] dolgok = {
                new RepuloDolgok("Papírsárkány"),   // az ős még példányosítható (nem absztrakt)
                new Madar("Galamb", 65),
                new Sas("Szirti sas"),
                new Vereb("Házi veréb")
        };

        // 3. Minden elemen ugyanazt hívjuk, de a felülbírálás miatt más-más fut le:
        //    - RepuloDolgok: az ős változata,
        //    - Madar: a Madar felülírt változata,
        //    - Sas: a Sas saját változata (ami a super-rel a Madarét is hívja),
        //    - Vereb: nem írta felül, ezért a Madar változatát örökli.
        System.out.println();
        for (RepuloDolgok d : dolgok) {
            d.repul();
        }

        // 4. Típusellenőrzés: minden Sas egyben Madar és RepuloDolgok is.
        System.out.println();
        RepuloDolgok sas = dolgok[2];
        System.out.println("A szirti sas Madar? " + (sas instanceof Madar));
        System.out.println("A papírsárkány Madar? " + (dolgok[0] instanceof Madar));
    }
}
