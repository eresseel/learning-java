import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {
        String names[] = {"Peter", "Patricia", "Hunter", "Sarah"};
        System.out.println("Eredeti szoveg: ");
        for (int i=0; i<names.length; i++) {
            System.out.println(i + ": " + names[i]);
        }
        Arrays.sort(names);
        System.out.println("Az uj sorrend: ");
        for(int i=0; i<names.length; i++) {
            System.out.println(i + ": " + names[i]);
        }
    }
}
