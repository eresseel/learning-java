import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Az első grafikus felületünk tartalma: egy szövegmező (JTextField), egy gomb
 * (JButton) és egy címke (JLabel).
 * <p>
 * Eseményvezérelt működés: a program nem lineárisan fut le, hanem vár, és
 * akkor fut le egy kódrészlet, amikor a hozzá kötött esemény bekövetkezik
 * (itt: a gomb megnyomása).
 * <p>
 * A komponensek egy JPanel-en (konténer) vannak, nem közvetlenül a JFrame-en,
 * így a panel ablak nélkül (headless környezetben) is létrehozható és tesztelhető.
 */
public final class GuiPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // A komponensek mezők, hogy az eseménykezelő és a teszt is elérje őket.
    private final JTextField szovegMezo = new JTextField(15);
    private final JButton gomb = new JButton("Átmásol");
    private final JLabel cimke = new JLabel("(itt jelenik meg a szöveg)");

    /**
     * Felépíti a felületet és beköti az eseménykezelőt.
     */
    public GuiPanel() {
        // 1. Elrendezés (layout manager): a FlowLayout balról jobbra, sorban helyezi el a komponenseket.
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // 2. Komponensek felhelyezése a konténerre.
        add(new JLabel("Szöveg:"));
        add(szovegMezo);
        add(gomb);
        add(cimke);

        // 3. Eseménykezelő: a gomb ActionEvent-jére feliratkozunk egy lambdával.
        //    (Ez az ActionListener interfész egyetlen metódusának, az actionPerformed-nek a megvalósítása.)
        gomb.addActionListener(e -> masol());

        // 4. Kényelmi funkció: Enter a szövegmezőben ugyanazt teszi, mint a gomb.
        szovegMezo.addActionListener(e -> masol());
    }

    /**
     * A gomb eseménye: a címke felveszi a szövegmező szövegét, majd a szövegmező törlődik.
     */
    private void masol() {
        cimke.setText(szovegMezo.getText());
        szovegMezo.setText("");
        // A fókuszt visszaadjuk a szövegmezőnek, hogy rögtön írni lehessen.
        szovegMezo.requestFocusInWindow();
    }

    /** @return a szövegmező (teszteléshez) */
    JTextField getSzovegMezo() {
        return szovegMezo;
    }

    /** @return a gomb (teszteléshez) */
    JButton getGomb() {
        return gomb;
    }

    /** @return a címke (teszteléshez) */
    JLabel getCimke() {
        return cimke;
    }
}
