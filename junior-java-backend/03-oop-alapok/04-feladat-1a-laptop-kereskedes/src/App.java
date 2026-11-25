/**
 * 3. lecke, Feladat I.A: a diasoron példaként szereplő laptopkereskedés
 * adatszerkezetének megvalósítása és kipróbálása.
 */
public class App {

    /**
     * Belépési pont: felépít egy kereskedést, feltölti laptopokkal,
     * majd lekérdezéseket futtat rajta.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        // 1. A kereskedés objektum létrehozása.
        LaptopKereskedes bolt = new LaptopKereskedes("Pelda Laptop Kft.");

        // 2. Laptopok példányosítása (az első kettő a diasor adatai) és felvétele.
        bolt.felvesz(new Laptop("Samsung", "Series 9", "LTS001", 300000));
        bolt.felvesz(new Laptop("HP", "Pavilion", "N3W03EA", 350000));
        bolt.felvesz(new Laptop("Lenovo", "ThinkPad", "LEN-T14", 420000));
        bolt.felvesz(new Laptop("Dell", "Latitude", "DL-5440", 389000));

        // 3. Az ismétlődő cikkszámot a kereskedés elutasítja.
        boolean siker = bolt.felvesz(new Laptop("Asus", "ZenBook", "LTS001", 310000));
        System.out.println("Ismétlődő cikkszám felvétele sikeres? " + siker);
        System.out.println();

        // 4. Listázás és összesítések.
        bolt.listaz();
        System.out.printf("Készlet összértéke: %,d Ft%n", bolt.osszErtek());
        System.out.println("Legdrágább: " + bolt.legdragabb());

        // 5. Keresés cikkszám alapján, majd akció alkalmazása a talált objektumon.
        Laptop talalt = bolt.keres("n3w03ea");
        if (talalt != null) {
            talalt.akcio(10);
            System.out.println();
            System.out.println("10% akció után: " + talalt);
        }
        System.out.println("Nem létező cikkszám keresése: " + bolt.keres("XYZ"));
    }
}
