import java.util.List;

/**
 * A S.O.L.I.D. alapelvek szemléltetése a diasor példái alapján.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // S, O, L: a számolás és a kiírás külön osztályban; új síkidom (Haromszog)
        // a meglévő kód módosítása nélkül; minden síkidom behelyettesíthető.
        System.out.println("=== S / O / L – területösszegzés ===");
        List<Sikidom> sikidomok = List.of(new Teglalap(3, 4), new Kor(1), new Haromszog(6, 2));
        double osszeg = new TeruletOsszegzo().osszeg(sikidomok);
        new EredmenyKiiro().kiir(sikidomok, osszeg);

        // I: két kicsi interfész egy nagy helyett.
        System.out.println();
        System.out.println("=== I – interfész elkülönítés ===");
        Riport riport = new Riport("Havi forgalom");
        Szamla szamla = new Szamla("SZ-2024-001", 15000);
        System.out.println("  Riport HTML: " + riport.htmlbe());
        System.out.println("  Riport XML:  " + riport.xmlbe());
        System.out.println("  Számla XML:  " + szamla.xmlbe());
        // Mindkettő kezelhető XmlMentheto-ként:
        for (XmlMentheto x : List.of(riport, szamla)) {
            System.out.println("  XML export: " + x.xmlbe());
        }

        // D: ugyanaz a RendelesKezelo két különböző tárolóval.
        System.out.println();
        System.out.println("=== D – függőség invertálás ===");
        for (AdatTarolo tarolo : List.of(new MemoriaTarolo(), new KonzolTarolo())) {
            RendelesKezelo kezelo = new RendelesKezelo(tarolo);
            kezelo.rendel("alma", 3);
            kezelo.rendel("körte", 0);
            kezelo.rendel("szilva", 2);
            System.out.println("  Rögzített rendelések (" + tarolo.getClass().getSimpleName() + "): "
                    + kezelo.rendelesekSzama());
        }
    }
}
