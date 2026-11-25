import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;

/**
 * 6. lecke, Feladat I.B belépési pontja.
 */
public class App {

    /**
     * Megnyitja a főablakot, headless környezetben önellenőrzést futtat.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            onellenorzes();
            return;
        }
        SwingUtilities.invokeLater(() -> new FoAblak().setVisible(true));
    }

    /**
     * Ablak nélküli ellenőrzés: az I.A gomb működése, valamint az I.B élő
     * tükrözése beszúrás és törlés után.
     */
    private static void onellenorzes() {
        System.out.println("Nincs grafikus környezet (headless) – a panel önellenőrzése fut.");
        GuiPanel panel = new GuiPanel();

        // 1. I.A: gombnyomásra másol és töröl.
        panel.getSzovegMezo().setText("Helló");
        panel.getGomb().doClick();
        boolean okA = panel.getCimke().getText().equals("Helló") && panel.getSzovegMezo().getText().isEmpty();
        System.out.println((okA ? "OK  " : "HIBA") + " – I.A címke: \"" + panel.getCimke().getText() + "\"");

        // 2. I.B: a setText is a dokumentumot módosítja, így a listener azonnal lefut.
        panel.getEloSzovegMezo().setText("élő");
        boolean okB1 = panel.getEloCimke().getText().equals("élő");
        System.out.println((okB1 ? "OK  " : "HIBA") + " – I.B beírás után: \"" + panel.getEloCimke().getText() + "\"");

        // 3. I.B: utolsó karakter törlése (removeUpdate) – gépelés utánzása.
        try {
            panel.getEloSzovegMezo().getDocument().remove(2, 1);
        } catch (javax.swing.text.BadLocationException e) {
            System.out.println("HIBA – " + e.getMessage());
        }
        boolean okB2 = panel.getEloCimke().getText().equals("él");
        System.out.println((okB2 ? "OK  " : "HIBA") + " – I.B törlés után: \"" + panel.getEloCimke().getText() + "\"");
    }
}
