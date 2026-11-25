package hu.learning.webshop.tesztautomatizalas.config;

import hu.learning.webshop.tesztautomatizalas.domain.InMemoryProductCatalog;
import hu.learning.webshop.tesztautomatizalas.domain.ProductCatalog;
import hu.learning.webshop.tesztautomatizalas.validation.UsernameValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A keretrendszer-független domain-osztályok Spring bean-né tétele.
 *
 * <p>A domain (01–02. lépés) szándékosan nem tud a Springről: így a komponenstesztek
 * továbbra is keretrendszer nélkül, gyorsan futnak. Itt „drótozzuk be” őket.</p>
 */
@Configuration
public class WebshopConfig {

    /**
     * @return a memóriabeli termékkatalógus (repository) mintaadattal
     */
    @Bean
    public ProductCatalog productCatalog() {
        return InMemoryProductCatalog.withSampleData();
    }

    /**
     * @return a felhasználónév-validátor (aktuális szabály)
     */
    @Bean
    public UsernameValidator usernameValidator() {
        return new UsernameValidator();
    }
}
