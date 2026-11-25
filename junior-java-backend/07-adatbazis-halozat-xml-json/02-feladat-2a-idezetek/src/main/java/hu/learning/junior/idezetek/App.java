package hu.learning.junior.idezetek;

import java.io.IOException;
import java.util.List;

/**
 * Feladat II.A – „Hallgatózzunk a világhálón! Mit üzen nekünk Charles Dickens
 * és George Bernard Shaw?” Mindkét szerzőtől lekér néhány idézetet HTTP-n.
 *
 * <p>Először a Quotable API-t próbálja; ha az nem elérhető vagy nem ad
 * találatot, a Wikiquote-ra vált (tartalék forrás). A {@code --wikiquote}
 * kapcsolóval a tartalék forrás közvetlenül kipróbálható.</p>
 */
public class App {

    /** Szerzőnként ennyi idézetet írunk ki. */
    private static final int DARAB = 3;

    /**
     * Egy szerző azonosítói a két forrásban.
     *
     * @param nev            megjelenítendő név
     * @param quotableSlug   azonosító a Quotable API-ban
     * @param wikiquoteOldal oldalcím a Wikiquote-on
     */
    private record Szerzo(String nev, String quotableSlug, String wikiquoteOldal) {
    }

    /**
     * Belépési pont.
     *
     * @param args {@code --wikiquote}: csak a tartalék forrást használja
     */
    public static void main(String[] args) {
        boolean csakWikiquote = args.length > 0 && args[0].equals("--wikiquote");
        IdezetKliens kliens = new IdezetKliens();
        List<Szerzo> szerzok = List.of(
                new Szerzo("Charles Dickens", "charles-dickens", "Charles_Dickens"),
                new Szerzo("George Bernard Shaw", "george-bernard-shaw", "George_Bernard_Shaw"));

        // Szerzőnként: idézetek lekérése és kiírása.
        for (Szerzo szerzo : szerzok) {
            System.out.println("=== Mit üzen nekünk " + szerzo.nev() + "? ===");
            List<Idezet> idezetek = lekeres(kliens, szerzo, csakWikiquote);
            if (idezetek.isEmpty()) {
                System.out.println("(Most egyik forrás sem válaszolt – nincs internet?)");
            }
            idezetek.forEach(i -> System.out.println(" - " + i));
            System.out.println();
        }
    }

    /**
     * Idézetek lekérése tartalék forrással: ha a Quotable hibát dob vagy üres
     * listát ad, a Wikiquote-ot próbálja.
     *
     * @param kliens        a HTTP kliens
     * @param szerzo        a szerző
     * @param csakWikiquote true esetén rögtön a Wikiquote-ot kérdezi
     * @return az idézetek (üres lista, ha egyik forrás sem működött)
     */
    private static List<Idezet> lekeres(IdezetKliens kliens, Szerzo szerzo, boolean csakWikiquote) {
        // 1. Elsődleges forrás (ha nem kapcsoltuk ki).
        if (!csakWikiquote) {
            try {
                List<Idezet> lista = kliens.quotable(szerzo.quotableSlug(), DARAB);
                if (!lista.isEmpty()) {
                    return lista;
                }
            } catch (IOException | RuntimeException e) {
                // RuntimeException: pl. váratlan JSON szerkezet (JsonParseException).
                System.out.println("(Quotable nem elérhető: " + e.getMessage() + " – Wikiquote következik)");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return List.of();
            }
        }
        // 2. Tartalék forrás.
        try {
            return kliens.wikiquote(szerzo.wikiquoteOldal(), szerzo.nev(), DARAB);
        } catch (IOException | RuntimeException e) {
            System.out.println("(Wikiquote sem elérhető: " + e.getMessage() + ")");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return List.of();
    }
}
