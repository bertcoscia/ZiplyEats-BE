package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.entities.RestaurantCategory;
import bertcoscia.FoodDelivery_BE.entities.UserRole;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewRestaurantsDTO;
import bertcoscia.FoodDelivery_BE.repositories.RestaurantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class RestaurantsService {
    @Autowired
    RestaurantsRepository repository;

    @Autowired
    RestaurantsCategoriesService restaurantsCategoriesService;

    @Autowired
    PasswordEncoder bcrypt;

    @Autowired
    UserRolesService userRolesService;

    public Restaurant save(NewRestaurantsDTO body) {
        if (this.repository.existsByEmail(body.email())) throw new BadRequestException("Email already used");
        if (this.repository.existsByPhoneNumber(body.phoneNumber())) throw new BadRequestException("Phone number already used");
        if (this.repository.existsByNameAndAddressAndCity(body.name(), body.address(), body.city())) throw new BadRequestException("There is already a restaurant called " + body.name() + " in " + body.city() + " at the address " + body.address());
        RestaurantCategory restaurantCategory = this.restaurantsCategoriesService.findByRestaurantCategory(body.restaurantCategory());
        UserRole userRoleFound = this.userRolesService.findByUserRole("RESTAURANT");
        return new Restaurant(body.name(), body.address(), body.city(), body.email(), bcrypt.encode(body.password()), body.phoneNumber(), restaurantCategory, userRoleFound);
    }

    public Restaurant findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public Page<Restaurant> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable);
    }

    public void findByIdAndDelete(UUID id) {
        Restaurant found = this.findById(id);
        this.repository.delete(found);
    }

    public Restaurant findByIdAndUpdate(UUID id, Restaurant body) {
        Restaurant found = this.findById(id);
        if (this.repository.existsByEmail(body.getEmail()) && !found.getIdRestaurant().equals(body.getIdRestaurant())) throw new BadRequestException("Email already used");
        if (this.repository.existsByPhoneNumber(body.getPhoneNumber()) && !found.getIdRestaurant().equals(body.getIdRestaurant())) throw new BadRequestException("Phone number already used");
        if (this.repository.existsByNameAndAddressAndCity(body.getName(), body.getAddress(), body.getCity()) && !found.getIdRestaurant().equals(body.getIdRestaurant())) throw new BadRequestException("There is already a restaurant called " + body.getName() + " in " + body.getCity() + " at the address " + body.getAddress());
        found.setAddress(body.getAddress());
        found.setCity(body.getCity());
        found.setName(body.getName());
        found.setMenu(body.getMenu());
        found.setEmail(body.getEmail());
        found.setPhoneNumber(body.getPhoneNumber());
        return this.repository.save(found);
    }
}
