import java.util.Scanner;

/**
 * Bekéri a felhasználó vezeték- és keresztnevét, majd egy sorban kiírja a
 * teljes nevét – vezérlőkarakterek (escape szekvenciák) használatával.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. Scanner példányosítása a standard bemenetre (java.util.Scanner import kell).
        Scanner sc = new Scanner(System.in);

        // 2. Vezetéknév bekérése. A print nem tesz sortörést, így a felhasználó
        //    a kérdés után, ugyanabban a sorban gépelhet.
        System.out.print("Vezetéknév: ");
        String vezeteknev = sc.hasNextLine() ? sc.nextLine().trim() : "";

        // 3. Keresztnév bekérése – a feladat szerint "új sorban".
        System.out.print("Keresztnév: ");
        String keresztnev = sc.hasNextLine() ? sc.nextLine().trim() : "";

        // 4. Kiírás vezérlőkarakterekkel:
        //    \n – új sor, \t – tabulátor, \" – idézőjel a szövegen belül.
        System.out.println("\n\tA felhasználó teljes neve: \"" + vezeteknev + " " + keresztnev + "\"\n");

        // 5. Az erőforrás lezárása.
        sc.close();
    }
}
