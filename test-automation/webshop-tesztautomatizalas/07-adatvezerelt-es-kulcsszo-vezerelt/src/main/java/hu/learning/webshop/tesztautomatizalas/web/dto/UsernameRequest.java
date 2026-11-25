package hu.learning.webshop.tesztautomatizalas.web.dto;

/**
 * A {@code POST /validate-username} kérés törzse.
 *
 * <p>Szándékosan nincs rajta {@code @NotBlank}: a hiányzó nevet is a validátor
 * értékeli (200 + {@code valid=false}), mert ez üzleti válasz, nem hibás kérés.</p>
 *
 * @param username az ellenőrizendő felhasználónév
 */
public record UsernameRequest(String username) {
}
