/**
 * A 3. lecke diasorának Laptop példája: osztály, objektum (példány),
 * konstruktor, getter/setter és láthatósági szintek.
 */
public class App {

    /**
     * Belépési pont: létrehoz két laptop objektumot ugyanabból az osztályból,
     * és bemutatja, hogy minden példány önálló életet él.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. Példányosítás: <Osztály> <azonosító> = new <Osztály>(<paraméterek>);
        //    Ugyanaz a tervrajz (osztály), de két különböző értékekkel rendelkező objektum.
        Laptop ujlaptop = new Laptop("Samsung", "Series 9", "LTS001", 300000);
        Laptop ujlaptop2 = new Laptop("HP", "Pavilion", "N3W03EA", 350000);

        System.out.println("--- Két példány ugyanabból az osztályból ---");
        System.out.println(ujlaptop);
        System.out.println(ujlaptop2);

        // 2. Getterek: a privát mezőket csak így olvashatjuk.
        System.out.println();
        System.out.println("Az első laptop gyártója: " + ujlaptop.getGyarto());
        System.out.println("A második laptop ára: " + ujlaptop2.getAr() + " Ft");

        // 3. Setterek: az egyik objektum módosítása NEM hat a másikra.
        ujlaptop.setAr(279990);
        ujlaptop.setModell("Series 9 Plus");
        System.out.println();
        System.out.println("--- Az első laptop módosítása után ---");
        System.out.println(ujlaptop);
        System.out.println(ujlaptop2);

        // 4. A setter ellenőrzést is végez: a negatív árat elutasítja.
        ujlaptop2.setAr(-1000);
        System.out.println("A második laptop ára változatlan: " + ujlaptop2.getAr() + " Ft");

        // 5. Az ujlaptop.ar = 0; sor itt fordítási hibát adna, mert az ar mező private.

        // 6. Default konstruktor és láthatósági szintek.
        PeldaOsztaly pelda = new PeldaOsztaly();   // nincs megírt konstruktor, mégis működik
        System.out.println();
        System.out.println("PeldaOsztaly – package-private mező: " + pelda.csomagonBelul);
        System.out.println("PeldaOsztaly – protected mező (azonos csomag): " + pelda.leszarmazottaknak);
        System.out.println("PeldaOsztaly – public mező: " + pelda.mindenkinek);
        // A pelda.csakBelul fordítási hiba lenne (private), de a publikus metódus eléri:
        System.out.println("PeldaOsztaly – összeg (a private mezővel együtt): " + pelda.osszeg());
    }
}
