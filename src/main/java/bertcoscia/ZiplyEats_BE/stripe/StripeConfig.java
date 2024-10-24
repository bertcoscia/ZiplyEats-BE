package bertcoscia.ZiplyEats_BE.stripe;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${STRIPE_KEY}")
    private String stripeKey;

   @PostConstruct
    public void init() {
        Stripe.apiKey = stripeKey;
    }
}
