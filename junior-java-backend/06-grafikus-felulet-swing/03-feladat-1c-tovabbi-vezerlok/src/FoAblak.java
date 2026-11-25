import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * A program főablaka (frame): egy JFrame, amelyre a GuiPanel tartalmát tesszük.
 */
public final class FoAblak extends JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Létrehozza és beállítja az ablakot.
     */
    public FoAblak() {
        super("Első GUI-s programom – Feladat I.C");
        // Az ablak bezárásakor a program is álljon le (alapból csak elrejtené az ablakot).
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(new GuiPanel());
        // A pack() a komponensek ideális méretéhez igazítja az ablakot.
        pack();
        // Képernyő közepére helyezés.
        setLocationRelativeTo(null);
    }
}
