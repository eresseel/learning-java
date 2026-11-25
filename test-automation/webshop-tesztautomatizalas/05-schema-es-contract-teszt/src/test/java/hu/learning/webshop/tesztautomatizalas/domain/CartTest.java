package hu.learning.webshop.tesztautomatizalas.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * A {@link Cart} komponenstesztjei – a VIII. feladat „Kosár” funkciójának
 * legalsó (komponens) tesztszintje. Ezek futnak a CI/CD pipeline build-szakaszában elsőként.
 */
@DisplayName("Kosár – komponensteszt")
class CartTest {

    /** A tesztelt kosár; minden teszt előtt üres. */
    private Cart cart;

    /** Arrange közös része: friss, üres kosár a mintakatalógussal. */
    @BeforeEach
    void setUp() {
        cart = new Cart(InMemoryProductCatalog.withSampleData());
    }

    /** Termék hozzáadása. */
    @Nested
    @DisplayName("Hozzáadás")
    class Hozzaadas {

        /** Az új kosár üres, végösszege 0.00. */
        @Test
        void newCartIsEmpty() {
            assertAll(
                    () -> assertTrue(cart.isEmpty()),
                    () -> assertEquals(new BigDecimal("0.00"), cart.total()),
                    () -> assertEquals(0, cart.itemCount()));
        }

        /** Egy termék hozzáadása után a végösszeg = ár × darab. */
        @Test
        void addSingleItem() {
            // Act – 2 db vezeték nélküli egér (7990 Ft)
            cart.addItem("P001", 2);
            // Assert – az assertAll minden ellenőrzést lefuttat, akkor is, ha egy elbukik
            assertAll(
                    () -> assertEquals(1, cart.getItems().size()),
                    () -> assertEquals(2, cart.itemCount()),
                    () -> assertEquals(new BigDecimal("15980.00"), cart.total()));
        }

        /** Ugyanaz a termék kétszer: a mennyiségek összeadódnak, egy tétel marad. */
        @Test
        void addingSameProductMergesQuantities() {
            cart.addItem("P003", 1);
            cart.addItem("P003", 4);
            assertEquals(1, cart.getItems().size());
            assertEquals(5, cart.getItems().getFirst().quantity());
        }

        /** Ismeretlen termék nem kerülhet a kosárba. */
        @Test
        void unknownProductIsRejected() {
            UnknownProductException ex = assertThrows(UnknownProductException.class,
                    () -> cart.addItem("P999", 1));
            assertEquals("Ismeretlen termék: P999", ex.getMessage());
            assertTrue(cart.isEmpty());
        }

        /** Határérték: 0 és 100 érvénytelen, 1 és 99 érvényes. */
        @ParameterizedTest(name = "mennyiség {0} elutasítva")
        @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 100})
        void quantityOutOfRangeIsRejected(int qty) {
            assertThrows(InvalidQuantityException.class, () -> cart.addItem("P001", qty));
        }

        /** Határérték alsó/felső széle elfogadott. */
        @ParameterizedTest(name = "mennyiség {0} elfogadva")
        @ValueSource(ints = {1, 99})
        void quantityOnBoundaryIsAccepted(int qty) {
            assertEquals(qty, cart.addItem("P001", qty).quantity());
        }

        /** Összeadás után 99 fölé menne: elutasítás, a kosár változatlan (atomikus művelet). */
        @Test
        void mergedQuantityAboveMaxIsRejectedAndCartUnchanged() {
            cart.addItem("P001", 60);
            assertThrows(InvalidQuantityException.class, () -> cart.addItem("P001", 40));
            assertEquals(60, cart.getItems().getFirst().quantity());
        }
    }

    /** Mennyiség módosítása. */
    @Nested
    @DisplayName("Mennyiség módosítása")
    class MennyisegModositas {

        /** A módosítás felülírja (nem növeli) a mennyiséget, és frissíti a végösszeget. */
        @Test
        void updateOverwritesQuantity() {
            // Arrange
            cart.addItem("P002", 1);
            cart.addItem("P006", 1);
            // Act
            cart.updateQuantity("P002", 3);
            // Assert – 3 × 24990 + 1 × 3990 = 78960
            assertEquals(new BigDecimal("78960.00"), cart.total());
        }

        /** Nem kosárban lévő tétel nem módosítható. */
        @Test
        void updateOfMissingItemIsRejected() {
            assertThrows(ItemNotInCartException.class, () -> cart.updateQuantity("P001", 2));
        }

        /** Módosításnál a 0 sem megengedett (arra a törlés való). */
        @Test
        void updateToZeroIsRejected() {
            cart.addItem("P001", 1);
            assertThrows(InvalidQuantityException.class, () -> cart.updateQuantity("P001", 0));
        }
    }

    /** Törlés és ürítés. */
    @Nested
    @DisplayName("Törlés")
    class Torles {

        /** Törlés után a tétel eltűnik és a végösszeg csökken. */
        @Test
        void removeItem() {
            cart.addItem("P001", 1);
            cart.addItem("P005", 2);
            cart.removeItem("P001");
            assertEquals(new BigDecimal("19980.00"), cart.total());
        }

        /** Nem létező tétel törlése hibát jelez. */
        @Test
        void removeMissingItemIsRejected() {
            assertThrows(ItemNotInCartException.class, () -> cart.removeItem("P001"));
        }

        /** A clear() mindent kiürít. */
        @Test
        void clearEmptiesCart() {
            cart.addItem("P001", 1);
            cart.clear();
            assertTrue(cart.isEmpty());
        }

        /** A getItems() másolatot ad: kívülről nem módosítható a kosár. */
        @Test
        void itemsListIsUnmodifiable() {
            cart.addItem("P001", 1);
            assertThrows(UnsupportedOperationException.class, () -> cart.getItems().clear());
        }
    }
}
