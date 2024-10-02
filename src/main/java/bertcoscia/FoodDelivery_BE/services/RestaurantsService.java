package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.entities.RestaurantCategory;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewRestaurantsDTO;
import bertcoscia.FoodDelivery_BE.repositories.RestaurantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RestaurantsService {
    @Autowired
    RestaurantsRepository repository;

    @Autowired
    RestaurantsCategoriesService restaurantsCategoriesService;

    public Restaurant save(NewRestaurantsDTO body) {
        if (this.repository.existsByEmail(body.email())) throw new BadRequestException("Email already used");
        if (this.repository.existsByNameAndAddressAndCity(body.name(), body.address(), body.city())) throw new BadRequestException("There is already a restaurant called " + body.name() + " in " + body.city() + " at the address " + body.address());
        RestaurantCategory restaurantCategory = this.restaurantsCategoriesService.findByRestaurantCategory(body.restaurantCategory());
        return new Restaurant(body.name(), body.address(), body.city(), body.email(), body.password(), body.phoneNumber(), restaurantCategory);
    }

    public Restaurant findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }
}
