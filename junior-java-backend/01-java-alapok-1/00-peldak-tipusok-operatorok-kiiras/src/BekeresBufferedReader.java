import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Adatbekérés a {@link BufferedReader} osztállyal (java.io csomag).
 * <p>
 * Önálló belépési ponttal rendelkezik: {@code java -cp out BekeresBufferedReader}.
 * A {@link BekeresScanner} párja: ugyanazt csinálja, de másképp.
 */
public class BekeresBufferedReader {

    /**
     * Bekér egy nevet és egy életkort, majd kiírja őket.
     *
     * @param args parancssori argumentumok (nem használjuk)
     * @throws IOException ha olvasás közben I/O hiba történik – a BufferedReader
     *                     kivételkezelésre kötelez, ezt itt a fejlécben jelezzük
     */
    public static void main(String[] args) throws IOException {
        // 1. Példányosítás: a System.in bájtfolyamot InputStreamReader alakítja
        //    karakterfolyammá, ezt pedig a BufferedReader puffereli.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // 2. Szöveg bekérése: readLine() egy teljes sort ad (vagy null-t a bemenet végén).
        System.out.print("Név: ");
        String nev = br.readLine();

        // 3. Szám bekérése: a BufferedReader csak szöveget olvas, számmá nekünk
        //    kell alakítani – hibás bemenetnél a parseInt NumberFormatException-t dob.
        System.out.print("Életkor: ");
        String korSzoveg = br.readLine();
        try {
            int kor = Integer.parseInt(korSzoveg.trim());
            System.out.println(nev + " " + kor + " éves.");
        } catch (NumberFormatException | NullPointerException e) {
            System.out.println("Nem egész számot adott meg.");
        }

        // 4. Lezárás.
        br.close();
    }
}
