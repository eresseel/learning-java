/**
 * Bemutató program a lecke elméleti diáihoz: primitív típusok, típuskonverzió,
 * operátorok és precedencia, konstans, hatókör, valamint a formázott kiírás.
 * <p>
 * Nem kér be adatot, csak lefut, és megmutatja az egyes nyelvi elemek viselkedését.
 */
public class App {

    /**
     * Osztályszintű konstans: a {@code final} kulcsszó miatt az értéke futás közben
     * nem változtatható. A konstansok nevét csupa nagybetűvel írjuk.
     */
    static final double PI = 3.141592653589793;

    /**
     * A program belépési pontja: sorban meghívja a témakörönkénti bemutató metódusokat.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        primitivTipusok();
        tipuskonverzio();
        operatorok();
        hatokorEsKonstans();
        formazottKiiras();
    }

    /**
     * A nyolc primitív típus bemutatása a legnagyobb/legkisebb értékükkel.
     */
    static void primitivTipusok() {
        System.out.println("=== Primitív típusok ===");
        // Logikai és karakter típus
        boolean igaz = true;
        char betu = 'Á'; // a char 2 bájtos Unicode karakter, ékezetes betű is lehet
        System.out.println("boolean: " + igaz + ", char: " + betu);

        // Egész típusok – a burkoló (wrapper) osztályok MIN_VALUE / MAX_VALUE konstansai
        // pontosan megadják az értéktartományt (ugyanaz, mint a dián lévő táblázat).
        System.out.println("byte:  " + Byte.MIN_VALUE + " … " + Byte.MAX_VALUE);
        System.out.println("short: " + Short.MIN_VALUE + " … " + Short.MAX_VALUE);
        System.out.println("int:   " + Integer.MIN_VALUE + " … " + Integer.MAX_VALUE);
        System.out.println("long:  " + Long.MIN_VALUE + " … " + Long.MAX_VALUE);

        // Valós típusok
        System.out.println("float max:  " + Float.MAX_VALUE);
        System.out.println("double max: " + Double.MAX_VALUE);

        // Túlcsordulás: ha egy int a maximumán túl nő, "körbefordul" a minimumra.
        int maxInt = Integer.MAX_VALUE;
        System.out.println("Integer.MAX_VALUE + 1 = " + (maxInt + 1) + "  (túlcsordulás!)");
        System.out.println();
    }

    /**
     * Implicit (bővítő) és explicit (szűkítő, kasztolás) konverzió, valamint
     * szövegből számmá alakítás beépített metódussal.
     */
    static void tipuskonverzio() {
        System.out.println("=== Típuskonverzió ===");
        // 1. Implicit konverzió: int -> long -> double, adatvesztés nem lehet,
        //    ezért a fordító magától elvégzi.
        int egesz = 42;
        long hosszu = egesz;
        double valos = hosszu;
        System.out.println("Implicit: int " + egesz + " -> long " + hosszu + " -> double " + valos);

        // 2. Explicit konverzió (kasztolás): (céltípus) érték.
        //    double -> int: a tizedesjegyek elvesznek (csonkolás, NEM kerekítés).
        double tort = 9.99;
        int csonkolt = (int) tort;
        System.out.println("Explicit: (int) " + tort + " = " + csonkolt);

        //    int -> byte: ha az érték nem fér bele, "furcsa" szám jön ki (adatvesztés).
        int nagy = 300;
        byte kicsi = (byte) nagy;
        System.out.println("Explicit: (byte) " + nagy + " = " + kicsi + "  (adatvesztés!)");

        //    char <-> int: a karakter mögött egy Unicode kódszám áll.
        char a = 'A';
        System.out.println("(int) 'A' = " + (int) a + ", (char) 66 = " + (char) 66);

        // 3. Sem implicit, sem explicit konverzió nem működik String -> int között,
        //    erre beépített metódus való.
        String szoveg = "123";
        int szam = Integer.parseInt(szoveg);
        System.out.println("Integer.parseInt(\"123\") + 1 = " + (szam + 1));
        System.out.println();
    }

    /**
     * Aritmetikai, relációs, logikai, értékadó és egyéb operátorok, precedencia.
     */
    static void operatorok() {
        System.out.println("=== Operátorok ===");
        int a = 17;
        int b = 5;
        // Aritmetikai operátorok – figyelem: két int osztása egész osztás!
        System.out.println("a + b = " + (a + b) + ", a - b = " + (a - b) + ", a * b = " + (a * b));
        System.out.println("a / b = " + (a / b) + " (egész osztás), a % b = " + (a % b) + " (maradék)");
        System.out.println("(double) a / b = " + ((double) a / b));

        // Relációs operátorok – az eredmény mindig boolean
        System.out.println("a > b: " + (a > b) + ", a == b: " + (a == b) + ", a != b: " + (a != b));

        // Logikai operátorok: && (ÉS), || (VAGY), ! (NEM).
        // A && és || "rövidzáras": ha az első tag már eldönti az eredményt,
        // a második ki sem értékelődik.
        boolean pozitivEsParatlan = a > 0 && a % 2 == 1;
        System.out.println("a pozitív és páratlan: " + pozitivEsParatlan + ", !igaz = " + !true);

        // Értékadó operátorok: a += 3 ugyanaz, mint a = a + 3
        int c = 10;
        c += 3;   // 13
        c *= 2;   // 26
        c %= 7;   // 5
        System.out.println("c (10 += 3, *= 2, %= 7) = " + c);

        // Egyéb operátorok: ++ / -- (prefix és postfix), ?: (feltételes operátor)
        int i = 5;
        int x = i++; // előbb felhasználja (x = 5), aztán növel (i = 6)
        int y = ++i; // előbb növel (i = 7), aztán felhasználja (y = 7)
        System.out.println("i++ -> " + x + ", ++i -> " + y + ", i = " + i);
        String paritas = (a % 2 == 0) ? "páros" : "páratlan";
        System.out.println("a " + paritas + " (feltételes operátorral)");

        // Precedencia: a szorzás erősebb, mint az összeadás; zárójellel felülírható.
        System.out.println("2 + 3 * 4 = " + (2 + 3 * 4) + ", (2 + 3) * 4 = " + ((2 + 3) * 4));
        System.out.println();
    }

    /**
     * Változó deklaráció, definíció, blokkszintű hatókör és konstans használata.
     */
    static void hatokorEsKonstans() {
        System.out.println("=== Változók, hatókör, konstans ===");
        int szam;      // deklaráció: típus + név
        szam = 5;      // definíció: értékadás
        int masik = 7; // deklaráció és definíció egyszerre
        System.out.println("szam = " + szam + ", masik = " + masik);

        // Blokkszintű változó: csak a kapcsos zárójelen belül létezik.
        {
            int blokkValtozo = szam * 2;
            System.out.println("blokkon belül: blokkValtozo = " + blokkValtozo);
        }
        // Itt a blokkValtozo már NEM érhető el – ha használnánk, fordítási hiba lenne.

        // Konstans használata: a PI-t osztályszinten definiáltuk (final).
        double sugar = 2.0;
        System.out.println("Kör kerülete (r = 2): " + 2 * sugar * PI);
        // PI = 3; // <- fordítási hiba lenne: final változónak nem adhatunk új értéket
        System.out.println();
    }

    /**
     * Vezérlőkarakterek és a printf formázó karakterei.
     */
    static void formazottKiiras() {
        System.out.println("=== Kiírás, vezérlőkarakterek, printf ===");
        // print: nincs sortörés, println: van sortörés
        System.out.print("Ez egy sor, ");
        System.out.println("és itt folytatódik.");

        // Vezérlőkarakterek (escape szekvenciák): \n új sor, \t tabulátor,
        // \" idézőjel, \\ fordított perjel
        System.out.println("Név:\tKiss Péter\nIdézet:\t\"Hello\"\nÚtvonal:\tC:\\java");

        // printf formázó karakterek:
        //  %d egész, %f valós, %.2f két tizedesjegy, %s szöveg, %n sortörés,
        //  %8.2f legalább 8 karakter szélesen, jobbra igazítva, %-10s balra igazítva
        double ar = 1234.5678;
        System.out.printf("Egész: %d, valós: %f, két tizedes: %.2f%n", 42, ar, ar);
        System.out.printf("|%-10s|%8.2f|%n", "alma", ar);
        // A String.format ugyanígy működik, csak szöveget ad vissza kiírás helyett.
        String formazott = String.format("%05d", 42);
        System.out.println("Vezető nullákkal: " + formazott);
        System.out.format("A System.out.format ugyanaz, mint a printf: %.1f%n", ar);
    }
}
