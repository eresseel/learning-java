package hu.learning.webshop.tesztautomatizalas.domain;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A vásárlói kosár – a VIII. feladat („Kosár” funkció: termék kosárba helyezése és
 * mennyiségének frissítése) üzleti logikája.
 *
 * <p><b>Üzleti szabályok</b> (saját meghatározás, a README is rögzíti):</p>
 * <ol>
 *   <li>Egy tétel mennyisége {@value #MIN_QUANTITY} és {@value #MAX_QUANTITY} közé esik.</li>
 *   <li>Csak a katalógusban létező termék tehető a kosárba ({@link UnknownProductException}).</li>
 *   <li>Már kosárban lévő termék újbóli hozzáadásakor a mennyiségek összeadódnak; ha az összeg
 *       túllépi a maximumot, a művelet elutasul és a kosár változatlan marad.</li>
 *   <li>Mennyiség módosítása és törlés csak kosárban lévő tételre lehetséges
 *       ({@link ItemNotInCartException}); módosításnál a 0 nem megengedett – arra a törlés való.</li>
 *   <li>A végösszeg a sorösszegek összege, 2 tizedesjegyre; üres kosárnál 0.00.</li>
 * </ol>
 */
public class Cart {

    /** Egy tétel minimális mennyisége. */
    public static final int MIN_QUANTITY = 1;
    /** Egy tétel maximális mennyisége. */
    public static final int MAX_QUANTITY = 99;

    /** A termékek forrása – a kosár csak azonosítót kap, a terméket innen oldja fel. */
    private final ProductCatalog catalog;
    /** Termékazonosító → tétel, beszúrási sorrendben. */
    private final Map<String, CartItem> items = new LinkedHashMap<>();

    /**
     * @param catalog a termékkatalógus, amelyből a termékeket feloldjuk
     */
    public Cart(ProductCatalog catalog) {
        this.catalog = Objects.requireNonNull(catalog);
    }

    /**
     * Terméket tesz a kosárba (vagy növeli a meglévő tétel mennyiségét).
     *
     * @param productId termékazonosító
     * @param quantity  hozzáadandó mennyiség
     * @return a frissített tétel
     * @throws InvalidQuantityException ha a mennyiség vagy az összeadott mennyiség a tartományon kívül esik
     * @throws UnknownProductException  ha a termék nincs a katalógusban
     */
    public CartItem addItem(String productId, int quantity) {
        // 1. Először a mennyiséget ellenőrizzük (olcsó ellenőrzés előre)
        requireValidQuantity(quantity);
        // 2. Termék feloldása – ismeretlen azonosítónál kivétel
        Product product = catalog.findById(productId)
                .orElseThrow(() -> new UnknownProductException(productId));
        // 3. Ha már van ilyen tétel, összeadjuk a mennyiségeket (3. szabály)
        CartItem existing = items.get(productId);
        int newQuantity = existing == null ? quantity : existing.quantity() + quantity;
        requireValidQuantity(newQuantity);
        // 4. Csak sikeres ellenőrzés után módosítjuk az állapotot → hibánál változatlan a kosár
        CartItem updated = new CartItem(product, newQuantity);
        items.put(productId, updated);
        return updated;
    }

    /**
     * Beállítja egy kosárban lévő tétel mennyiségét (nem hozzáad, hanem felülír).
     *
     * @param productId termékazonosító
     * @param quantity  az új mennyiség
     * @return a frissített tétel
     * @throws InvalidQuantityException ha a mennyiség a tartományon kívül esik
     * @throws ItemNotInCartException   ha a termék nincs a kosárban
     */
    public CartItem updateQuantity(String productId, int quantity) {
        requireValidQuantity(quantity);
        CartItem existing = items.get(productId);
        if (existing == null) {
            throw new ItemNotInCartException(productId);
        }
        CartItem updated = new CartItem(existing.product(), quantity);
        items.put(productId, updated);
        return updated;
    }

    /**
     * Eltávolít egy tételt a kosárból.
     *
     * @param productId termékazonosító
     * @throws ItemNotInCartException ha a termék nincs a kosárban
     */
    public void removeItem(String productId) {
        if (items.remove(productId) == null) {
            throw new ItemNotInCartException(productId);
        }
    }

    /** Kiüríti a kosarat (pl. sikeres fizetés után, vagy tesztek előkészítésekor). */
    public void clear() {
        items.clear();
    }

    /**
     * @return a tételek másolata beszúrási sorrendben (kívülről nem módosítható)
     */
    public List<CartItem> getItems() {
        return List.copyOf(items.values());
    }

    /**
     * @return a kosár végösszege 2 tizedesjegyre (üres kosárnál 0.00)
     */
    public BigDecimal total() {
        // reduce: a sorösszegeket 0-ból indulva összeadjuk
        return items.values().stream()
                .map(CartItem::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2);
    }

    /**
     * @return a kosárban lévő darabok száma összesen (nem a tételek száma)
     */
    public int itemCount() {
        return items.values().stream().mapToInt(CartItem::quantity).sum();
    }

    /**
     * @return {@code true}, ha nincs tétel a kosárban
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Közös mennyiség-ellenőrzés (1. szabály).
     *
     * @param quantity a vizsgált mennyiség
     * @throws InvalidQuantityException ha a tartományon kívül esik
     */
    private static void requireValidQuantity(int quantity) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            throw new InvalidQuantityException(quantity);
        }
    }
}
