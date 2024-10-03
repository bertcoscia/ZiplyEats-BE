package bertcoscia.FoodDelivery_BE.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config {

    @Bean
    PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(12);
    }
}
