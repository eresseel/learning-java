package hu.learning.webshop.tesztautomatizalas.keyword;

import java.util.List;

/**
 * Egy kulcsszóvezérelt tesztlépés: a táblázat egy sora.
 *
 * @param source  forráshely a hibaüzenetekhez (pl. {@code kosar-alap.csv:4})
 * @param keyword a kulcsszó (pl. {@code ADD_TO_CART})
 * @param args    a kulcsszó argumentumai
 */
public record KeywordStep(String source, String keyword, List<String> args) {

    /**
     * Az i-edik argumentum, érthető hibával, ha hiányzik.
     *
     * @param index 0-tól számozott index
     * @return az argumentum
     * @throws IllegalArgumentException ha nincs ennyi argumentum (TAS-hiba: hibás táblázat)
     */
    public String arg(int index) {
        if (index >= args.size()) {
            throw new IllegalArgumentException(source + ": a(z) " + keyword + " kulcsszónak legalább "
                    + (index + 1) + " argumentum kell, kapott: " + args);
        }
        return args.get(index);
    }

    @Override
    public String toString() {
        return source + " " + keyword + " " + args;
    }
}
