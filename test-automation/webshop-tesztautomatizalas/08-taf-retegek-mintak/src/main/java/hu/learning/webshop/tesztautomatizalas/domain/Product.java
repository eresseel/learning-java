package hu.learning.webshop.tesztautomatizalas.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Egy termék a webáruház katalógusában.
 *
 * <p>Megváltoztathatatlan (immutable) record. Az árat {@link BigDecimal}-ban tároljuk,
 * mert pénzösszegnél a {@code double} kerekítési hibái elfogadhatatlanok. Pénznem: HUF.</p>
 *
 * @param id    egyedi azonosító (pl. {@code P001})
 * @param name  megjelenített név
 * @param price bruttó egységár forintban, pozitív szám
 */
public record Product(String id, String name, BigDecimal price) {

    /**
     * Kompakt konstruktor: az objektum létrehozásakor ellenőrzi az invariánsokat,
     * így érvénytelen termék nem is jöhet létre.
     *
     * @throws IllegalArgumentException ha valamelyik mező hiányzik vagy az ár nem pozitív
     */
    public Product {
        // 1. Kötelező mezők
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("A termékazonosító kötelező.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A terméknév kötelező.");
        }
        // 2. Az ár pozitív kell legyen (0 Ft-os termék nincs)
        Objects.requireNonNull(price, "Az ár kötelező.");
        if (price.signum() <= 0) {
            throw new IllegalArgumentException("Az árnak pozitívnak kell lennie: " + price);
        }
        // 3. Egységesen 2 tizedesjegyre állítjuk (a fizetési szolgáltató is így várja majd)
        price = price.setScale(2);
    }
}
