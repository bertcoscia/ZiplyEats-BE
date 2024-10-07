package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.login.LoginDTO;
import bertcoscia.FoodDelivery_BE.payloads.login.LoginRespDTO;
import bertcoscia.FoodDelivery_BE.payloads.newEntities.NewRestaurantsDTO;
import bertcoscia.FoodDelivery_BE.payloads.newEntities.NewUsersDTO;
import bertcoscia.FoodDelivery_BE.payloads.responses.NewEntitiesRespDTO;
import bertcoscia.FoodDelivery_BE.services.AuthService;
import bertcoscia.FoodDelivery_BE.services.RestaurantsService;
import bertcoscia.FoodDelivery_BE.services.RidersService;
import bertcoscia.FoodDelivery_BE.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService service;

    @Autowired
    UsersService usersService;

    @Autowired
    RidersService ridersService;

    @Autowired
    RestaurantsService restaurantsService;

    @PostMapping("/users-signup")
    @ResponseStatus(HttpStatus.CREATED)
    public NewEntitiesRespDTO userSignup(@RequestBody @Validated NewUsersDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.usersService.save(body).getIdUser());
        }
    }

    @PostMapping("/riders-signup")
    @ResponseStatus(HttpStatus.CREATED)
    public NewEntitiesRespDTO riderSignup(@RequestBody @Validated NewUsersDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.ridersService.save(body).getIdUser());
        }
    }

    @PostMapping("/restaurants-signup")
    @ResponseStatus(HttpStatus.CREATED)
    public NewEntitiesRespDTO restaurantsSignup(@RequestBody @Validated NewRestaurantsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.restaurantsService.save(body).getIdUser());
        }
    }

    @PostMapping("/users-login")
    public LoginRespDTO userLogin(@RequestBody @Validated LoginDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new LoginRespDTO(this.service.userCheckCredentialsAndGenerateToken(body));
        }
    }

    @PostMapping("/restaurants-login")
    public LoginRespDTO restaurantLogin(@RequestBody @Validated LoginDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new LoginRespDTO(this.service.restaurantCheckCredentialsAndGenerateToken(body));
        }
    }



}
