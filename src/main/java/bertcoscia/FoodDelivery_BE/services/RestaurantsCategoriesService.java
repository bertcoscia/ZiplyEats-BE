package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.RestaurantCategory;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewRestaurantCategoriesDTO;
import bertcoscia.FoodDelivery_BE.repositories.RestaurantCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RestaurantsCategoriesService {
    @Autowired
    RestaurantCategoriesRepository repository;

    public RestaurantCategory save(NewRestaurantCategoriesDTO body) {
        if (this.repository.existsByRestaurantCategory(body.restaurantCategory())) throw new BadRequestException("Category " + body.restaurantCategory() + " already existing");
        return this.repository.save(new RestaurantCategory(body.restaurantCategory()));
    }

    public RestaurantCategory findByRestaurantCategory(String restaurantCategory) {
        return this.repository.findByRestaurantCategory(restaurantCategory).orElseThrow(()-> new NotFoundException("Could not find restaurant category " + restaurantCategory));
    }

    public RestaurantCategory findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }
}
