/**
 * Az első Java program: kiírja a konzolra, hogy "Hello World!".
 * <p>
 * Minden Java program legalább egy osztályból áll; a futás a
 * {@code public static void main(String[] args)} metódusnál indul.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (itt nem használjuk)
     */
    public static void main(String[] args) {
        // A System.out.println a zárójelben megadott szöveget írja ki,
        // majd sortörést tesz utána. Minden utasítást pontosvessző zár.
        System.out.println("Hello World!");
    }
}
