import javax.swing.*;

public class App extends JFrame {
    public App() {
        super("Hello World!");
        setSize(350, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public static void main(String[] args) throws Exception {
        App sal = new App();
    }
}
