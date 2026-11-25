/**
 * Absztrakt osztály bemutatása: RepuloDolgok → Madar (Sas, Vereb), Repulo, Labda.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. new RepuloDolgok("valami") itt fordítási hibát adna: absztrakt osztály nem példányosítható.
        //    Gyűjtő típusként viszont használható (polimorfizmus).
        RepuloDolgok[] dolgok = {
                new Madar("Galamb"),
                new Sas("Szirti sas"),
                new Vereb("Házi veréb"),
                new Repulo("Utasszállító", 11000),
                new Labda("Strandlabda")
        };

        // 2. Az ős nem absztrakt bemutat() metódusa hívja az absztrakt repul()-t,
        //    ami mindig a tényleges leszármazott megvalósítását futtatja.
        for (RepuloDolgok d : dolgok) {
            d.bemutat();
        }
    }
}
