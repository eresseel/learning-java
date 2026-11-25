import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;

/**
 * 6. lecke, Feladat I.A belépési pontja.
 */
public class App {

    /**
     * Megnyitja a főablakot. Ha nincs grafikus környezet (headless: pl. szerver,
     * CI, java -Djava.awt.headless=true), ablakot nem nyithatunk, ezért helyette
     * a panel működését ellenőrizzük programból.
     *
     * @param args parancssori argumentumok (nem használjuk)
     */
    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            onellenorzes();
            return;
        }
        // A Swing komponenseket az eseménykezelő szálon (Event Dispatch Thread) illik létrehozni.
        SwingUtilities.invokeLater(() -> new FoAblak().setVisible(true));
    }

    /**
     * Ablak nélküli ellenőrzés: beír a szövegmezőbe, "megnyomja" a gombot
     * (doClick), majd megnézi a címkét és a szövegmezőt.
     */
    private static void onellenorzes() {
        System.out.println("Nincs grafikus környezet (headless) – a panel önellenőrzése fut.");
        GuiPanel panel = new GuiPanel();
        panel.getSzovegMezo().setText("Helló, Swing!");
        panel.getGomb().doClick();
        boolean ok = panel.getCimke().getText().equals("Helló, Swing!")
                && panel.getSzovegMezo().getText().isEmpty();
        System.out.println("Címke: \"" + panel.getCimke().getText() + "\", szövegmező: \""
                + panel.getSzovegMezo().getText() + "\"");
        System.out.println(ok ? "OK – a gomb átmásolta és törölte a szöveget." : "HIBA – nem a várt állapot.");
    }
}
