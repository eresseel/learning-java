package hu.learning.webshop.tesztautomatizalas.domain;

/**
 * A webáruház üzleti szabálysértéseinek közös ősosztálya.
 *
 * <p>Futásidejű (unchecked) kivétel, így a hívónak nem kötelező elkapnia. A közös ős
 * később (03. lépés) lehetővé teszi, hogy a REST réteg egy helyen alakítsa őket
 * HTTP hibaválasszá.</p>
 */
public abstract class WebshopException extends RuntimeException {

    /**
     * @param message magyar nyelvű, felhasználónak is megjeleníthető hibaüzenet
     */
    protected WebshopException(String message) {
        super(message);
    }
}
