package bertcoscia.ZiplyEats_BE.services;

import bertcoscia.ZiplyEats_BE.entities.Restaurant;
import bertcoscia.ZiplyEats_BE.entities.User;
import bertcoscia.ZiplyEats_BE.exceptions.UnauthorizedException;
import bertcoscia.ZiplyEats_BE.payloads.login.LoginDTO;
import bertcoscia.ZiplyEats_BE.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UsersService usersService;
    @Autowired
    JWTTools jwtTools;
    @Autowired
    PasswordEncoder bcrypt;
    @Autowired
    RestaurantsService restaurantsService;

    public String userCheckCredentialsAndGenerateToken(LoginDTO body) {
        User found = this.usersService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createUserToken(found);
        } else {
            throw new UnauthorizedException("Password and/or email wrong");
        }
    }

    public String restaurantCheckCredentialsAndGenerateToken(LoginDTO body) {
        Restaurant found = this.restaurantsService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createRestaurantToken(found);
        } else {
            throw new UnauthorizedException("Password and/or email wrong");
        }
    }
}
