package hu.learning.webshop.tesztautomatizalas.web.dto;

import java.math.BigDecimal;

/**
 * A {@code POST /checkout} sikeres válasza.
 *
 * @param orderId   a webshop rendelésazonosítója
 * @param paymentId a fizetés azonosítója a szolgáltatónál
 * @param status    a rendelés állapota ({@code PAID})
 * @param total     a kifizetett összeg
 * @param currency  pénznem
 */
public record CheckoutResponse(String orderId, String paymentId, String status, BigDecimal total, String currency) {
}
