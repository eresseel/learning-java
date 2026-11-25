package hu.learning.webshop.tesztautomatizalas.taf.business;

/**
 * A szerződésben hivatkozott „szolgáltatói állapotok” (provider states) beállítása.
 *
 * <p>Az állapotok üzleti fogalmak („a kosárban 1 db P001 van”), ezért az üzleti rétegben vannak,
 * és a {@link ShopFacade}-et használják – nem közvetlen HTTP hívásokat.</p>
 */
public final class ProviderStates {

    /** Segédosztály. */
    private ProviderStates() {
    }

    /**
     * Beállítja a megnevezett állapotot.
     *
     * @param state a szerződésben szereplő állapotnév
     * @param shop  a webshop facade
     * @throws IllegalArgumentException ismeretlen állapotnál (TAS-hiba: a szerződés és a verifikáló kód nincs szinkronban)
     */
    public static void setUp(String state, ShopFacade shop) {
        switch (state) {
            case "nincs előfeltétel", "a katalógus a mintatermékeket tartalmazza" -> {
                // A katalógus fix mintaadat, nincs teendő
            }
            case "a kosár üres" -> shop.startWithEmptyCart();
            case "a kosárban 1 db P001 van" -> {
                shop.startWithEmptyCart();
                shop.cart().add("P001", 1);
            }
            default -> throw new IllegalArgumentException("Ismeretlen provider state: " + state);
        }
    }
}
