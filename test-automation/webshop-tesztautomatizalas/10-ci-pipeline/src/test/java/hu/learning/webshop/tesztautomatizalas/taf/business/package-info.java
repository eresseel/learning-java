/**
 * <b>TAF 2. réteg – üzleti logika (business logic).</b>
 *
 * <p>A webshop fogalmaival dolgozik („tegyél 2 egeret a kosárba”, „fizess”), és ezeket a core
 * réteg HTTP hívásaira fordítja. A tesztszkriptek csak ezt a réteget látják. Ha a SUT API-ja
 * változik (pl. új útvonal), csak itt kell módosítani – a tesztszkriptek változatlanok maradnak.</p>
 *
 * <p>Elnevezési konvenció: a {@code xxx()} metódusok a sikeres utat várják és ellenőrzik (pl. 201);
 * a {@code tryXxx()} metódusok nyers választ adnak a negatív tesztekhez.</p>
 */
package hu.learning.webshop.tesztautomatizalas.taf.business;
