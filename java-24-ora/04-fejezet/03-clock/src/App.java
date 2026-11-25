import java.awt.*;
import javax.swing.*;

public class App extends JFrame {
    public App() {
        super("Clock");
        setSize(225, 125);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout flo = new FlowLayout();
        setLayout(flo);
        ClockPanel time = new ClockPanel();
        add(time);
        setVisible(true);
    }

    public static void main(String[] arguments) {
        App clock = new App();
    }
}
