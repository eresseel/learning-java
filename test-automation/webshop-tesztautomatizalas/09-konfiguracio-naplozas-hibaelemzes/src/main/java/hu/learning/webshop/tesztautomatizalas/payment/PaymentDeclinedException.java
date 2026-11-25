package hu.learning.webshop.tesztautomatizalas.payment;

/** A szolgáltató 4xx-szel elutasította a fizetést (pl. fedezethiány, elutasított kártya). */
public class PaymentDeclinedException extends PaymentException {

    /**
     * @param status a szolgáltató HTTP státusza
     * @param body   a szolgáltató válaszának törzse (diagnosztikához)
     */
    public PaymentDeclinedException(int status, String body) {
        super("A fizetést a szolgáltató elutasította (HTTP " + status + "): " + body, null);
    }
}
