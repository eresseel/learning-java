import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A Feladat I.C felülete: az I.A és I.B sorai, és alattuk egy harmadik rész
 * további vezérlőkkel: JSpinner (számléptető), JCheckBox (jelölőnégyzet),
 * JList (lista) és JComboBox (legördülő lista).
 * <p>
 * Minden vezérlőnek más típusú eseménye van:
 * JSpinner → ChangeListener, JCheckBox → ItemListener,
 * JList → ListSelectionListener, JComboBox → ActionListener.
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

    // --- I.C rész: további vezérlők ---
    /** Számléptető 0 és 120 között, 18-as kezdőértékkel, 1-es lépésközzel. */
    private final JSpinner eletkorSpinner = new JSpinner(new SpinnerNumberModel(18, 0, 120, 1));
    private final JCheckBox hirlevelCheckBox = new JCheckBox("Hírlevelet kérek");
    private final JList<String> nyelvLista = new JList<>(new String[]{"Java", "Python", "C#", "JavaScript", "SQL", "Kotlin"});
    private final JComboBox<String> varosCombo = new JComboBox<>(new String[]{"Budapest", "Debrecen", "Szeged", "Pécs", "Győr"});
    private final JButton osszegzoGomb = new JButton("Összegzés");
    /** Az utolsó eseményt mutató állapotsor. */
    private final JLabel allapotCimke = new JLabel("Utolsó esemény: –");
    /** Az összegzés eredménye. */
    private final JLabel osszegzoCimke = new JLabel(" ");

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

        // 3/b. Harmadik rész (I.C): további vezérlők két sorban.
        add(harmadikResz());

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

        // 6. I.C eseménykezelők: mindegyik vezérlő a saját eseménytípusával jelez.
        eletkorSpinner.addChangeListener(e -> esemeny("életkor = " + eletkorSpinner.getValue()));
        hirlevelCheckBox.addItemListener(e -> esemeny("hírlevél = " + (hirlevelCheckBox.isSelected() ? "igen" : "nem")));
        nyelvLista.addListSelectionListener(e -> {
            // A kijelölés közben több esemény is jön; csak a véglegeset (nem "igazítás közbenit") írjuk ki.
            if (!e.getValueIsAdjusting()) {
                esemeny("nyelvek = " + nyelvLista.getSelectedValuesList());
            }
        });
        varosCombo.addActionListener(e -> esemeny("város = " + varosCombo.getSelectedItem()));
        osszegzoGomb.addActionListener(e -> osszegez());
    }

    /**
     * Összeállítja az I.C részt: egy keretes panel két sorral és az eredménycímkékkel.
     *
     * @return a kész panel
     */
    private JPanel harmadikResz() {
        JPanel resz = new JPanel();
        resz.setLayout(new BoxLayout(resz, BoxLayout.Y_AXIS));
        resz.setBorder(BorderFactory.createTitledBorder("I.C – további vezérlők"));

        // Első sor: számléptető, jelölőnégyzet, legördülő lista.
        JPanel sor1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        sor1.add(new JLabel("Életkor:"));
        sor1.add(eletkorSpinner);
        sor1.add(hirlevelCheckBox);
        sor1.add(new JLabel("Város:"));
        sor1.add(varosCombo);
        resz.add(sor1);

        // Második sor: lista görgethető keretben (JScrollPane), több elem is kijelölhető (Ctrl+kattintás).
        nyelvLista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        nyelvLista.setVisibleRowCount(4);
        JPanel sor2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        sor2.add(new JLabel("Nyelvek:"));
        sor2.add(new JScrollPane(nyelvLista));
        sor2.add(osszegzoGomb);
        resz.add(sor2);

        // Eredménysorok.
        JPanel sor3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        sor3.add(allapotCimke);
        resz.add(sor3);
        JPanel sor4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        sor4.add(osszegzoCimke);
        resz.add(sor4);
        return resz;
    }

    /**
     * Kiírja az utolsó esemény leírását az állapotsorba.
     *
     * @param leiras az esemény szöveges leírása
     */
    private void esemeny(String leiras) {
        allapotCimke.setText("Utolsó esemény: " + leiras);
    }

    /**
     * Összegzés gomb: kiolvassa az összes I.C vezérlő aktuális értékét.
     * Figyeljük meg a szigorú típuskezelést: a JSpinner értéke Object, amit
     * Integer-ré kell alakítani; a JList kijelölése List&lt;String&gt;.
     */
    private void osszegez() {
        int eletkor = (Integer) eletkorSpinner.getValue();
        boolean hirlevel = hirlevelCheckBox.isSelected();
        List<String> nyelvek = nyelvLista.getSelectedValuesList();
        String varos = (String) varosCombo.getSelectedItem();
        osszegzoCimke.setText(String.format("%d éves, %s, nyelvek: %s, hírlevél: %s",
                eletkor, varos, nyelvek.isEmpty() ? "nincs kijelölve" : String.join(", ", nyelvek),
                hirlevel ? "igen" : "nem"));
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

    /** @return az életkor számléptető (teszteléshez) */
    JSpinner getEletkorSpinner() {
        return eletkorSpinner;
    }

    /** @return a hírlevél jelölőnégyzet (teszteléshez) */
    JCheckBox getHirlevelCheckBox() {
        return hirlevelCheckBox;
    }

    /** @return a nyelvek listája (teszteléshez) */
    JList<String> getNyelvLista() {
        return nyelvLista;
    }

    /** @return a város legördülő lista (teszteléshez) */
    JComboBox<String> getVarosCombo() {
        return varosCombo;
    }

    /** @return az összegzés gomb (teszteléshez) */
    JButton getOsszegzoGomb() {
        return osszegzoGomb;
    }

    /** @return az állapotsor (teszteléshez) */
    JLabel getAllapotCimke() {
        return allapotCimke;
    }

    /** @return az összegzés címkéje (teszteléshez) */
    JLabel getOsszegzoCimke() {
        return osszegzoCimke;
    }
}
