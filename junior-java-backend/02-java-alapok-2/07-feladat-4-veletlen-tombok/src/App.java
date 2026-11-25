import java.util.Random;

/**
 * Feladat IV.: két véletlen elemszámú (5–10) egydimenziós tömböt tölt fel
 * 1 és 50 közötti véletlenszámokkal, majd tömbönként kiírja az összes elemet,
 * az elemek összegét, a páros számok darabszámát, a legnagyobb elemet és az
 * első négyzetszámot.
 * <p>
 * Ez a "metódusok nélküli" változat: minden a main-ben van. Az ismétlődő
 * kódrészek miatt a Feladat V. ezt bontja metódusokra.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Random rnd = new Random();

        // 1. Két tömb véletlen elemszámmal. nextInt(min, max) a felső határt
        //    NEM tartalmazza, ezért 5 és 10 között (mindkettőt beleértve) 11 a felső határ.
        int[] elsoTomb = new int[rnd.nextInt(5, 11)];
        int[] masodikTomb = new int[rnd.nextInt(5, 11)];

        // 2. Feltöltés 1 és 50 közötti (mindkettőt beleértve) véletlenszámokkal.
        for (int i = 0; i < elsoTomb.length; i++) {
            elsoTomb[i] = rnd.nextInt(1, 51);
        }
        for (int i = 0; i < masodikTomb.length; i++) {
            masodikTomb[i] = rnd.nextInt(1, 51);
        }

        // 3. Az első tömb adatai.
        System.out.println("=== Első tömb (" + elsoTomb.length + " elem) ===");
        System.out.print("Elemek: ");
        for (int szam : elsoTomb) {
            System.out.print(szam + " ");
        }
        System.out.println();

        // 3/a. Összeg, páros darabszám és maximum egyetlen bejárással.
        //      A maximumot az első elemmel indítjuk (nem 0-val!), így bármilyen
        //      értékkészletre helyes.
        int osszeg = 0;
        int parosDarab = 0;
        int maximum = elsoTomb[0];
        for (int szam : elsoTomb) {
            osszeg += szam;
            if (szam % 2 == 0) {
                parosDarab++;
            }
            if (szam > maximum) {
                maximum = szam;
            }
        }
        System.out.println("Összeg: " + osszeg);
        System.out.println("Páros számok darabszáma: " + parosDarab);
        System.out.println("Legnagyobb elem: " + maximum);

        // 3/b. Első négyzetszám: keresés, amely az első találatnál megáll (break).
        //      Egy szám négyzetszám, ha a gyöke egész: gyök * gyök == szám.
        //      A -1 jelzi, hogy nem találtunk ilyet.
        int elsoNegyzetszam = -1;
        for (int szam : elsoTomb) {
            int gyok = (int) Math.sqrt(szam);
            if (gyok * gyok == szam) {
                elsoNegyzetszam = szam;
                break;
            }
        }
        if (elsoNegyzetszam == -1) {
            System.out.println("Első négyzetszám: nincs a tömbben");
        } else {
            System.out.println("Első négyzetszám: " + elsoNegyzetszam);
        }

        // 4. A második tömb adatai – ugyanaz a kód még egyszer. Ez az ismétlődés
        //    a jele annak, hogy metódusra lenne szükség (lásd Feladat V.).
        System.out.println("=== Második tömb (" + masodikTomb.length + " elem) ===");
        System.out.print("Elemek: ");
        for (int szam : masodikTomb) {
            System.out.print(szam + " ");
        }
        System.out.println();

        osszeg = 0;
        parosDarab = 0;
        maximum = masodikTomb[0];
        for (int szam : masodikTomb) {
            osszeg += szam;
            if (szam % 2 == 0) {
                parosDarab++;
            }
            if (szam > maximum) {
                maximum = szam;
            }
        }
        System.out.println("Összeg: " + osszeg);
        System.out.println("Páros számok darabszáma: " + parosDarab);
        System.out.println("Legnagyobb elem: " + maximum);

        elsoNegyzetszam = -1;
        for (int szam : masodikTomb) {
            int gyok = (int) Math.sqrt(szam);
            if (gyok * gyok == szam) {
                elsoNegyzetszam = szam;
                break;
            }
        }
        if (elsoNegyzetszam == -1) {
            System.out.println("Első négyzetszám: nincs a tömbben");
        } else {
            System.out.println("Első négyzetszám: " + elsoNegyzetszam);
        }
    }
}
