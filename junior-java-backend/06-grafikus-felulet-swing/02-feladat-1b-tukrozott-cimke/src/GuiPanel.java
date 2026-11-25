import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A Feladat I.B felülete: az I.A vezérlői (szövegmező + gomb + címke), és
 * alattuk azok másolata, ahol az új címke FOLYAMATOSAN, gépelés közben
 * mutatja az új szövegmező tartalmát.
 * <p>
 * Az élő frissítéshez nem a gomb ActionEvent-jét, hanem a szövegmező
 * dokumentumának (Document) változásait figyeljük egy DocumentListener-rel.
 */
public final class GuiPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    // --- I.A rész: gombnyomásra másol ---
    private final JTextField szovegMezo = new JTextField(15);
    private final JButton gomb = new JButton("Átmásol");
    private final JLabel cimke = new JLabel("(itt jelenik meg a szöveg)");

    // --- I.B rész: élő tükrözés ---
    private final JTextField eloSzovegMezo = new JTextField(15);
    private final JLabel eloCimke = new JLabel(" ");

    /**
     * Felépíti a két sort és beköti az eseménykezelőket.
     */
    public GuiPanel() {
        // 1. A panel függőlegesen egymás alá rakja a sorokat (BoxLayout, Y tengely).
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 2. Első sor (I.A): saját FlowLayout-os alpanelen, kerettel és címmel.
        JPanel elsoSor = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        elsoSor.setBorder(BorderFactory.createTitledBorder("I.A – gombnyomásra"));
        elsoSor.add(new JLabel("Szöveg:"));
        elsoSor.add(szovegMezo);
        elsoSor.add(gomb);
        elsoSor.add(cimke);
        add(elsoSor);

        // 3. Második sor (I.B): a vezérlők másolata, gomb nélkül.
        JPanel masodikSor = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        masodikSor.setBorder(BorderFactory.createTitledBorder("I.B – folyamatosan"));
        masodikSor.add(new JLabel("Szöveg:"));
        masodikSor.add(eloSzovegMezo);
        masodikSor.add(new JLabel("Tükör:"));
        masodikSor.add(eloCimke);
        add(masodikSor);

        // 4. I.A eseménykezelő (változatlan).
        gomb.addActionListener(e -> masol());
        szovegMezo.addActionListener(e -> masol());

        // 5. I.B eseménykezelő: a DocumentListener interfész mindhárom metódusát
        //    meg kell valósítani (névtelen belső osztállyal), mindegyik ugyanazt teszi.
        eloSzovegMezo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                tukroz();   // karakter beszúrása
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                tukroz();   // karakter törlése
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                tukroz();   // formázás változása (egyszerű szövegmezőnél ritka)
            }
        });
    }

    /**
     * I.A: a címke felveszi a szövegmező szövegét, majd a szövegmező törlődik.
     */
    private void masol() {
        cimke.setText(szovegMezo.getText());
        szovegMezo.setText("");
        szovegMezo.requestFocusInWindow();
    }

    /**
     * I.B: az élő címke mindig a második szövegmező aktuális tartalmát mutatja.
     * Üres szövegnél egy szóközt írunk, hogy a címke ne essen össze nulla magasságúra.
     */
    private void tukroz() {
        String szoveg = eloSzovegMezo.getText();
        eloCimke.setText(szoveg.isEmpty() ? " " : szoveg);
    }

    /** @return az I.A szövegmező (teszteléshez) */
    JTextField getSzovegMezo() {
        return szovegMezo;
    }

    /** @return az I.A gomb (teszteléshez) */
    JButton getGomb() {
        return gomb;
    }

    /** @return az I.A címke (teszteléshez) */
    JLabel getCimke() {
        return cimke;
    }

    /** @return az I.B szövegmező (teszteléshez) */
    JTextField getEloSzovegMezo() {
        return eloSzovegMezo;
    }

    /** @return az I.B címke (teszteléshez) */
    JLabel getEloCimke() {
        return eloCimke;
    }
}
