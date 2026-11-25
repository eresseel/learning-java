package hu.learning.webshop.tesztautomatizalas.domain;

import java.util.List;
import java.util.Optional;

/**
 * A termékkatalógus absztrakciója (repository szerep).
 *
 * <p>Interfész, hogy a kosár ne egy konkrét tárolótól függjön (SOLID: függőség-inverzió).
 * Tesztben így bármikor lecserélhető egy egyszerű, memóriabeli megvalósításra.</p>
 */
public interface ProductCatalog {

    /**
     * @return az összes termék, azonosító szerint rendezve
     */
    List<Product> findAll();

    /**
     * @param id termékazonosító
     * @return a termék, vagy üres {@link Optional}, ha nincs ilyen
     */
    Optional<Product> findById(String id);

    /**
     * Kis- és nagybetűtől független keresés a terméknévben.
     *
     * @param query keresett szövegrész; üres vagy {@code null} esetén minden terméket visszaad
     * @return a találatok listája (lehet üres)
     */
    List<Product> search(String query);
}
