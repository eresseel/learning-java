package hu.learning.webshop.tesztautomatizalas.payment;

/** A fizetési hibák közös őse (futásidejű kivétel). */
public abstract class PaymentException extends RuntimeException {

    /**
     * @param message magyar nyelvű hibaüzenet
     * @param cause   az eredeti kivétel (lehet {@code null})
     */
    protected PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
