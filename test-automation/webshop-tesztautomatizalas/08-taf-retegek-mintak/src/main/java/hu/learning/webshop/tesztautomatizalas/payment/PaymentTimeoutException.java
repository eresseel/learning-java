package hu.learning.webshop.tesztautomatizalas.payment;

/** A szolgáltató nem válaszolt a megadott időkorláton belül. */
public class PaymentTimeoutException extends PaymentException {

    /**
     * @param cause az eredeti időtúllépési kivétel
     */
    public PaymentTimeoutException(Throwable cause) {
        super("A fizetési szolgáltató nem válaszolt időben.", cause);
    }
}
