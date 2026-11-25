/**
 * A statikusság bemutatása az Oktato osztály segítségével.
 */
public class App {

    /**
     * Belépési pont.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        System.out.println("Program indul – az Oktato osztályt még nem használtuk.");

        // 1. Statikus mező elérése példányosítás nélkül, az osztálynéven át.
        //    Ez az első hivatkozás, ekkor futnak le a statikus blokkok.
        System.out.println("Intézmény: " + Oktato.intezmeny);
        System.out.println("Oktatók száma induláskor: " + Oktato.getOktatokSzama());

        // 2. Statikus beágyazott osztály példányosítása a külső osztály nevével.
        Oktato.Tantargy java = new Oktato.Tantargy("Java alapok", 40);
        Oktato.Tantargy oop = new Oktato.Tantargy("OOP", 32);

        // 3. Példányok létrehozása – mindegyik növeli a KÖZÖS számlálót.
        Oktato elso = new Oktato("Kiss Anna", java);
        Oktato masodik = new Oktato("Nagy Péter", oop);
        System.out.println(elso.bemutatkozas());
        System.out.println(masodik.bemutatkozas());
        System.out.println("Oktatók száma: " + Oktato.getOktatokSzama());

        // 4. A statikus mező módosítása MINDEN példányra hat, hiszen csak egy van belőle.
        Oktato.intezmeny = "Ruander Oktatóközpont (Budapest)";
        System.out.println("Az intézmény átnevezése után:");
        System.out.println(elso.bemutatkozas());
        System.out.println(masodik.bemutatkozas());
    }
}
