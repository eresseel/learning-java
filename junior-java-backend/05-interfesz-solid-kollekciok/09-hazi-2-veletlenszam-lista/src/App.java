import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 5. lecke, Házi feladat II: lista 30 darab 1 és 100 közötti véletlenszámmal,
 * és a hozzá tartozó lekérdezések, részlista, törlés.
 */
public class App {

    /** A lista elemszáma. */
    private static final int ELEMSZAM = 30;

    /**
     * Belépési pont. Ha első argumentumként egy számot adunk meg, azt a
     * véletlenszám-generátor kezdőértékének (seed) használja, így a futás megismételhető.
     *
     * @param args opcionálisan: a seed (egész szám)
     */
    public static void main(String[] args) {
        Random rnd = args.length > 0 ? new Random(Long.parseLong(args[0])) : new Random();

        // 0. Feltöltés: 30 db véletlenszám 1 és 100 között (a felső határ kizáró, ezért 101).
        List<Integer> lista = new ArrayList<>();
        for (int i = 0; i < ELEMSZAM; i++) {
            lista.add(rnd.nextInt(1, 101));
        }

        // 1. Kiírás tízesével, tabulátorral elválasztva.
        System.out.println("1. A lista elemei:");
        tizesevelKiir(lista);

        // 2. Megszámlálás: páros számok.
        System.out.println();
        System.out.println("2. Páros számok száma: " + parosDarab(lista));

        // 3. Eldöntés: van-e az elemszámmal (30) egyező értékű elem? A contains() ezt egy lépésben megmondja.
        System.out.println();
        System.out.println("3. Van-e " + lista.size() + " értékű elem? " + (lista.contains(lista.size()) ? "igen" : "nem"));

        // 4. Részlista a középső 10 elemből: 30 elemnél a 10–19. index (a subList felső határa kizáró).
        int kezdet = (lista.size() - 10) / 2;
        List<Integer> kozepso = lista.subList(kezdet, kezdet + 10);
        // Elválasztójel csak az elemek KÖZÉ kerül: az utolsó után nem.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kozepso.size(); i++) {
            if (i > 0) {
                sb.append('-');
            }
            sb.append(kozepso.get(i));
        }
        System.out.println();
        System.out.println("4. A középső 10 elem: " + sb);

        // 5. Minden 20 alatti páratlan szám törlése. Bejárás közben csak az iterátor
        //    remove() metódusával szabad törölni (különben ConcurrentModificationException).
        Iterator<Integer> it = lista.iterator();
        int toroltDb = 0;
        while (it.hasNext()) {
            int szam = it.next();
            if (szam < 20 && szam % 2 != 0) {
                it.remove();
                toroltDb++;
            }
        }
        // (Ugyanez egy sorban: lista.removeIf(x -> x < 20 && x % 2 != 0);)
        System.out.println();
        System.out.println("5. A 20 alatti páratlan számok törlése (" + toroltDb + " db) után megmaradt elemek:");
        for (int szam : lista) {
            System.out.println(szam);
        }
        System.out.println("Páros számok száma a törlés után: " + parosDarab(lista));
    }

    /**
     * Kiírja a lista elemeit soronként tízesével, tabulátorral elválasztva.
     *
     * @param lista a kiírandó lista
     */
    private static void tizesevelKiir(List<Integer> lista) {
        for (int i = 0; i < lista.size(); i++) {
            System.out.print(lista.get(i));
            // Minden 10. elem után sortörés, egyébként tabulátor.
            System.out.print((i + 1) % 10 == 0 ? "\n" : "\t");
        }
        // Ha az elemszám nem osztható 10-zel, lezárjuk az utolsó sort.
        if (lista.size() % 10 != 0) {
            System.out.println();
        }
    }

    /**
     * Megszámolja a páros elemeket.
     *
     * @param lista a vizsgált lista
     * @return a páros elemek száma
     */
    private static int parosDarab(List<Integer> lista) {
        int db = 0;
        for (int szam : lista) {
            if (szam % 2 == 0) {
                db++;
            }
        }
        return db;
    }
}
