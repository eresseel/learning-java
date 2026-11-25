import java.util.Scanner;

/**
 * Adatbekérés a {@link Scanner} osztállyal (java.util csomag).
 * <p>
 * Önálló belépési ponttal rendelkezik: {@code java -cp out BekeresScanner}.
 * Párja a {@link BekeresBufferedReader}, a kettőt érdemes összehasonlítani.
 */
public class BekeresScanner {

    /**
     * Bekér egy nevet és egy életkort, majd kiírja őket.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. Példányosítás: a Scanner a standard bemenetről (billentyűzet / pipe) olvas.
        Scanner sc = new Scanner(System.in);

        // 2. Szöveg bekérése: nextLine() a teljes sort adja vissza.
        System.out.print("Név: ");
        String nev = sc.nextLine();

        // 3. Szám bekérése: a Scanner közvetlenül tud számot olvasni (nextInt),
        //    a hasNextInt() előre megmondja, hogy szám következik-e.
        System.out.print("Életkor: ");
        if (sc.hasNextInt()) {
            int kor = sc.nextInt();
            System.out.println(nev + " " + kor + " éves.");
        } else {
            System.out.println("Nem egész számot adott meg.");
        }

        // 4. Lezárás: az erőforrás felszabadítása.
        sc.close();
    }
}
