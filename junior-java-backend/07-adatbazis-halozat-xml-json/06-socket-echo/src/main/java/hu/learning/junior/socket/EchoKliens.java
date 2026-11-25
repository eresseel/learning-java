package hu.learning.junior.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Az {@link EchoSzerver} kliense: a konzolon beírt sorokat elküldi a
 * szervernek, és kiírja a választ. A bemenet végén (vagy {@code VEGE}
 * sornál) kilép.
 */
public class EchoKliens {

    /**
     * Belépési pont.
     *
     * @param args opcionálisan: [gép] [port], alapértelmezés: localhost 5000
     * @throws IOException ha a szerver nem érhető el
     */
    public static void main(String[] args) throws IOException {
        String gep = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : EchoSzerver.ALAP_PORT;
        // 1. Kapcsolódás a szerverhez; minden erőforrást a try-with-resources zár le.
        try (Socket socket = new Socket(gep, port);
             BufferedReader be = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter ki = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
             Scanner konzol = new Scanner(System.in)) {
            System.out.println("Kapcsolódva: " + socket.getRemoteSocketAddress() + " (kilépés: VEGE)");
            // 2. Sorok olvasása a konzolról, küldés, válasz kiírása.
            while (konzol.hasNextLine()) {
                String sor = konzol.nextLine();
                ki.println(sor);
                String valasz = be.readLine();
                if (valasz == null) {
                    break; // a szerver bontotta a kapcsolatot
                }
                System.out.println(valasz);
                if (sor.equals("VEGE")) {
                    break;
                }
            }
        }
    }
}
