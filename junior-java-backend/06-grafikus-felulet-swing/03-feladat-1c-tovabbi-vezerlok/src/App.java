import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

/**
 * 6. lecke, Feladat I.C belépési pontja.
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
     * Ablak nélküli ellenőrzés: az I.A és I.B működése, majd az I.C vezérlők
     * értékeinek beállítása programból, és az események, összegzés ellenőrzése.
     */
    private static void onellenorzes() {
        System.out.println("Nincs grafikus környezet (headless) – a panel önellenőrzése fut.");
        GuiPanel p = new GuiPanel();

        // 1. I.A és I.B (az előző lépésekből).
        p.getSzovegMezo().setText("Helló");
        p.getGomb().doClick();
        ellenoriz("I.A gomb", p.getCimke().getText().equals("Helló") && p.getSzovegMezo().getText().isEmpty());
        p.getEloSzovegMezo().setText("élő");
        try {
            p.getEloSzovegMezo().getDocument().remove(2, 1);
        } catch (BadLocationException e) {
            System.out.println("HIBA – " + e.getMessage());
        }
        ellenoriz("I.B tükrözés", p.getEloCimke().getText().equals("él"));

        // 2. I.C: minden vezérlő eseményét kiváltjuk, és nézzük az állapotsort.
        p.getEletkorSpinner().setValue(30);
        ellenoriz("JSpinner esemény (" + p.getAllapotCimke().getText() + ")",
                p.getAllapotCimke().getText().endsWith("életkor = 30"));
        p.getHirlevelCheckBox().doClick();
        ellenoriz("JCheckBox esemény (" + p.getAllapotCimke().getText() + ")",
                p.getAllapotCimke().getText().endsWith("hírlevél = igen"));
        p.getNyelvLista().setSelectedIndices(new int[]{0, 4});
        ellenoriz("JList esemény (" + p.getAllapotCimke().getText() + ")",
                p.getAllapotCimke().getText().endsWith("nyelvek = [Java, SQL]"));
        p.getVarosCombo().setSelectedItem("Szeged");
        ellenoriz("JComboBox esemény (" + p.getAllapotCimke().getText() + ")",
                p.getAllapotCimke().getText().endsWith("város = Szeged"));

        // 3. Összegzés gomb.
        p.getOsszegzoGomb().doClick();
        String vart = "30 éves, Szeged, nyelvek: Java, SQL, hírlevél: igen";
        ellenoriz("Összegzés (" + p.getOsszegzoCimke().getText() + ")", p.getOsszegzoCimke().getText().equals(vart));
    }

    /**
     * Kiírja egy ellenőrzés eredményét.
     *
     * @param nev a vizsgált funkció
     * @param ok  sikeres-e
     */
    private static void ellenoriz(String nev, boolean ok) {
        System.out.println((ok ? "OK   – " : "HIBA – ") + nev);
    }
}
