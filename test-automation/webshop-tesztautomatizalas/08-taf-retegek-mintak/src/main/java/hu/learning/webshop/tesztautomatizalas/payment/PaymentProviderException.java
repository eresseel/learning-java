package hu.learning.webshop.tesztautomatizalas.payment;

/** A szolgáltató hibás (5xx, váratlan) választ adott, vagy nem volt elérhető. */
public class PaymentProviderException extends PaymentException {

    /**
     * @param message a hiba leírása
     * @param cause   az eredeti kivétel (lehet {@code null})
     */
    public PaymentProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
