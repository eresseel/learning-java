import java.util.Scanner;

/**
 * Bekér egy egyjegyű pozitív egész számot, és esetszétválasztással (switch)
 * eldönti, melyik kategóriába esik:
 * <ul>
 *   <li>1–3: "A szám nem nagyobb háromnál"</li>
 *   <li>4–6: "A szám 4 és 6 közötti"</li>
 *   <li>7–9: "A szám nagyobb hatnál"</li>
 *   <li>minden más: "Nem megfelelő értéket adott meg"</li>
 * </ul>
 * Ugyanennek az elágazásos megoldása a szomszédos 13-as mappában található.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Szám bekérése. Ha nem egész szám érkezik, egy érvénytelen értéket
        //    (0) használunk, így a switch default ága fog lefutni.
        System.out.print("Adjon meg egy egyjegyű pozitív egész számot: ");
        int szam = sc.hasNextInt() ? sc.nextInt() : 0;

        // 2. A switch csak konkrét értékeket (nem tartományokat) tud vizsgálni,
        //    ezért minden értéket fel kell sorolni. A szándékos "átesést"
        //    (fall-through) használjuk ki: az egymás alá írt, utasítás nélküli
        //    case címkék ugyanarra a kódrészre futnak, amíg break-et nem talál.
        switch (szam) {
            case 1:
            case 2:
            case 3:
                System.out.println("A szám nem nagyobb háromnál");
                break;
            case 4:
            case 5:
            case 6:
                System.out.println("A szám 4 és 6 közötti");
                break;
            case 7:
            case 8:
            case 9:
                System.out.println("A szám nagyobb hatnál");
                break;
            default:
                // minden más érték: 0, negatív, kétjegyű, vagy nem szám
                System.out.println("Nem megfelelő értéket adott meg");
                break;
        }

        // Java 14 óta ugyanez tömörebben, "nyilas" switch-csel, átesés és break nélkül:
        //   switch (szam) {
        //       case 1, 2, 3 -> System.out.println("A szám nem nagyobb háromnál");
        //       case 4, 5, 6 -> System.out.println("A szám 4 és 6 közötti");
        //       case 7, 8, 9 -> System.out.println("A szám nagyobb hatnál");
        //       default -> System.out.println("Nem megfelelő értéket adott meg");
        //   }

        sc.close();
    }
}
