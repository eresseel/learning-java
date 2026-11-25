import java.text.Collator;
import java.util.Locale;

/**
 * Feladat VII.B: tíz magyar városnév ábécé szerinti növekvő sorrendbe
 * rendezése beszúrásos rendezéssel (a VII.A kiválasztásos rendezésétől eltérő
 * algoritmussal), majd kilistázása.
 * <p>
 * A szövegek összehasonlításához magyar nyelvi szabályokat ismerő
 * {@link Collator}-t használunk, mert a {@code String.compareTo} a karakterek
 * Unicode kódja szerint hasonlít, így az ékezetes betűket (pl. 'é') minden
 * ékezet nélküli betű mögé sorolná.
 */
public class App {

    /**
     * A program belépési pontja.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        String[] varosok = {"Budapest", "Debrecen", "Szeged", "Miskolc", "Pécs", "Győr",
            "Nyíregyháza", "Kecskemét", "Székesfehérvár", "Szombathely"};

        System.out.println("Eredeti sorrend:");
        listazas(varosok);

        // Magyar ábécérend szerinti összehasonlító. (A sima compareTo-val a
        // "Székesfehérvár" a "Szombathely" mögé kerülne, mert az 'é' kódja nagyobb
        // minden ékezet nélküli kisbetűénél.)
        Collator magyarRendezo = Collator.getInstance(Locale.of("hu", "HU"));
        beszurasosRendezes(varosok, magyarRendezo);

        System.out.println("\nÁbécé szerint növekvő sorrendben:");
        listazas(varosok);
    }

    /**
     * Helyben rendezi a tömböt növekvő sorrendbe, beszúrásos rendezéssel.
     * <p>
     * Működés: a tömb elejét rendezettnek tekintjük (kezdetben 1 elem). Minden
     * lépésben a következő elemet kivesszük, a rendezett rész nála "nagyobb"
     * elemeit eggyel jobbra toljuk, és a felszabadult helyre beszúrjuk – mint
     * amikor kártyalapokat rendezünk a kezünkben.
     *
     * @param tomb    a rendezendő szövegtömb (a metódus módosítja!)
     * @param rendezo a két szöveget összehasonlító Collator
     */
    static void beszurasosRendezes(String[] tomb, Collator rendezo) {
        for (int i = 1; i < tomb.length; i++) {
            String beszurando = tomb[i];
            int j = i - 1;
            // Amíg a bal oldali elem az ábécében a beszúrandó után következik
            // (compare pozitív), addig toljuk jobbra.
            while (j >= 0 && rendezo.compare(tomb[j], beszurando) > 0) {
                tomb[j + 1] = tomb[j];
                j--;
            }
            tomb[j + 1] = beszurando;
        }
    }

    /**
     * Sorszámozva kilistázza a tömb elemeit.
     *
     * @param tomb a kiírandó tömb
     */
    static void listazas(String[] tomb) {
        for (int i = 0; i < tomb.length; i++) {
            System.out.printf("%2d. %s%n", i + 1, tomb[i]);
        }
    }
}
