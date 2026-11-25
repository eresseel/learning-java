import java.util.Vector;
import java.util.Collections;

public class App {
    String names[] = {"alma", "korte", "szilva", "piros", "kek", "sarga"};
    public App(String moreNames[]) {
        Vector<String> list = new Vector<String>();
        for (byte i = 0; i < names.length; i++) {
            list.add(names[i]);
        }
        for (byte i = 0; i < moreNames.length; i++) {
            list.add(moreNames[i]);
        }
        Collections.sort(list);
        for (String name : list) {
            System.out.println(name);
        }
    }
    public static void main(String[] args) throws Exception {
        String name[] = {"piroska", "pipacs"};
        App listener = new App(name);
    }
}
