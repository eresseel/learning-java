package hu.learning.webshop.tesztautomatizalas.config;

import hu.learning.webshop.tesztautomatizalas.payment.PaymentClient;
import hu.learning.webshop.tesztautomatizalas.payment.PaypalProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** A fizetési kliens bean-je a {@code paypal.*} beállítások alapján. */
@Configuration
@EnableConfigurationProperties(PaypalProperties.class)
public class PaymentConfig {

    /**
     * @param props a beolvasott {@code paypal.*} beállítások
     * @return a PayPal kliens
     */
    @Bean
    public PaymentClient paymentClient(PaypalProperties props) {
        return PaymentClient.create(props.baseUrl(), props.timeout());
    }
}
