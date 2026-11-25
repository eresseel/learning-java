package hu.learning.junior.socket;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Az echo szerver tesztje valódi TCP kapcsolaton, a helyi gépen.
 */
class EchoSzerverTest {

    @Test
    @Timeout(10)
    void visszhangEsKilepes() throws Exception {
        // Arrange: 0-s port = az operációs rendszer választ egy szabad portot.
        try (ServerSocket szerver = new ServerSocket(0)) {
            // A szerver külön szálon fut, mert az accept() blokkol.
            Thread szal = new Thread(() -> {
                try {
                    EchoSzerver.futtat(szerver, 1);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            szal.start();

            // Act: kliensként kapcsolódunk és üzenünk.
            try (Socket s = new Socket("localhost", szerver.getLocalPort());
                 BufferedReader be = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter ki = new PrintWriter(s.getOutputStream(), true, StandardCharsets.UTF_8)) {
                ki.println("Helló, szerver!");
                String elso = be.readLine();
                ki.println("VEGE");
                String masodik = be.readLine();

                // Assert
                assertEquals("ECHO: Helló, szerver!", elso);
                assertEquals("Viszlát!", masodik);
            }
            szal.join(5000);
            assertFalse(szal.isAlive());
        }
    }
}
