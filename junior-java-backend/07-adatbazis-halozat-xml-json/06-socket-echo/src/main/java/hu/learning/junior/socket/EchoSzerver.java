package hu.learning.junior.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Egyszerű TCP „visszhang” (echo) szerver: minden kapott sort visszaküld
 * {@code ECHO: } előtaggal. A {@code VEGE} sorra bontja a kapcsolatot.
 *
 * <p>Szerver–kliens modell: a szerver egy porton figyel
 * ({@link ServerSocket#accept()}), a kliens erre a portra kapcsolódik; a
 * kapcsolat két végén egy-egy {@link Socket} áll, amelyeken bemeneti és
 * kimeneti adatfolyamon (stream) kommunikálnak.</p>
 */
public class EchoSzerver {

    /** Alapértelmezett port. */
    public static final int ALAP_PORT = 5000;

    /**
     * Belépési pont: elindítja a szervert, és (Ctrl+C-ig) egymás után
     * szolgálja ki a klienseket.
     *
     * @param args opcionálisan a port száma
     * @throws IOException ha a port foglalt vagy hálózati hiba történik
     */
    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : ALAP_PORT;
        // A ServerSocket lefoglalja a portot; a try-with-resources a végén felszabadítja.
        try (ServerSocket szerver = new ServerSocket(port)) {
            System.out.println("Echo szerver fut a " + port + ". porton (leállítás: Ctrl+C)");
            futtat(szerver, Integer.MAX_VALUE);
        }
    }

    /**
     * Klienseket fogad és szolgál ki egymás után (egyszerre egyet).
     *
     * @param szerver      a figyelő szerver socket
     * @param kliensekSzama ennyi kliens kiszolgálása után visszatér (teszthez)
     * @throws IOException hálózati hiba esetén
     */
    public static void futtat(ServerSocket szerver, int kliensekSzama) throws IOException {
        for (int i = 0; i < kliensekSzama; i++) {
            // accept(): BLOKKOL, amíg egy kliens nem kapcsolódik.
            try (Socket kliens = szerver.accept()) {
                System.out.println("Kliens kapcsolódott: " + kliens.getRemoteSocketAddress());
                kiszolgal(kliens);
                System.out.println("Kliens lecsatlakozott.");
            }
        }
    }

    /**
     * Egy kliens kiszolgálása: soronként olvas, és visszaküldi a sort.
     *
     * @param kliens a kliens kapcsolata
     * @throws IOException hálózati hiba esetén
     */
    static void kiszolgal(Socket kliens) throws IOException {
        // A bájtfolyamokat (InputStream/OutputStream) soronként kezelhető
        // karakterfolyamokká csomagoljuk. A PrintWriter autoFlush=true: println után azonnal küld.
        BufferedReader be = new BufferedReader(new InputStreamReader(kliens.getInputStream(), StandardCharsets.UTF_8));
        PrintWriter ki = new PrintWriter(kliens.getOutputStream(), true, StandardCharsets.UTF_8);
        String sor;
        // readLine() null-t ad, ha a kliens lezárta a kapcsolatot.
        while ((sor = be.readLine()) != null) {
            if (sor.equals("VEGE")) {
                ki.println("Viszlát!");
                return;
            }
            ki.println("ECHO: " + sor);
        }
    }
}
