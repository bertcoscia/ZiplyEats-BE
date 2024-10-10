package bertcoscia.ZiplyEats_BE.services;

import bertcoscia.ZiplyEats_BE.entities.Restaurant;
import bertcoscia.ZiplyEats_BE.entities.RestaurantCategory;
import bertcoscia.ZiplyEats_BE.entities.UserRole;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.exceptions.NotFoundException;
import bertcoscia.ZiplyEats_BE.payloads.edit.EditRestaurantsDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewRestaurantsDTO;
import bertcoscia.ZiplyEats_BE.repositories.RestaurantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        RestaurantCategory restaurantCategory;
        if (body.idCategory() == null || body.idCategory().isEmpty()) {
            restaurantCategory = this.restaurantsCategoriesService.findByRestaurantCategory(body.restaurantCategory());
        } else {
            restaurantCategory = this.restaurantsCategoriesService.findById(UUID.fromString(body.idCategory()));
        }
        UserRole userRoleFound = this.userRolesService.findByUserRole("RESTAURANT");
        Restaurant newRestaurant = new Restaurant(body.name(), body.email(), bcrypt.encode(body.password()), body.phoneNumber(), body.address(), body.city(), userRoleFound, restaurantCategory);
        String encodedName;
        encodedName = URLEncoder.encode(newRestaurant.getName(), StandardCharsets.UTF_8);
        newRestaurant.setAvatarUrl("https://ui-avatars.com/api/?name=" + encodedName + "&background=048C7A&color=fff");
        return this.repository.save(newRestaurant);
    }

    public Restaurant findById(UUID id) {
        return this.repository.findByIdUser(id).orElseThrow(()-> new NotFoundException(id));
    }

    public Restaurant findByEmail(String email) {
        return this.repository.findByEmail(email).orElseThrow(()-> new NotFoundException("Could not find a restaurant with email " + email));
    }

    public Restaurant findByNameAndCity(String name, String city) {
        return this.repository.findByNameIgnoreCaseAndCityIgnoreCase(name, city).orElseThrow(()-> new NotFoundException("Could not find a restaurant called " + name));
    }

    public Page<Restaurant> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable);
    }

    public Page<Restaurant> findAllByCategory(String category, String city, int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 15) page = 15;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAllByCategoryAndCity(category, city, pageable);
    }

    public Page<Restaurant> findByNameAndCityAndSimilar(String name, String city, int page, int size, Map<String, String> params) {
        Restaurant found = this.findByNameAndCity(name, city);
        if (page > 15) page = 15;
        Pageable pageable = PageRequest.of(page, size);
        return this.repository.findByNameAndCityAndCategoryAndSimilar(name, city, found.getRestaurantCategory().getRestaurantCategory(), pageable);
    }

    public void findByIdAndDelete(UUID id) {
        Restaurant found = this.findById(id);
        this.repository.delete(found);
    }

    public Restaurant findByIdAndUpdate(UUID id, EditRestaurantsDTO body) {
        Restaurant found = this.findById(id);
        if (this.repository.existsByEmail(body.email()) && !found.getIdUser().equals(id)) throw new BadRequestException("Email already used");
        if (this.repository.existsByPhoneNumber(body.phoneNumber()) && !found.getIdUser().equals(id)) throw new BadRequestException("Phone number already used");
        if (this.repository.existsByNameAndAddressAndCity(body.name(), body.address(), body.city()) && !found.getIdUser().equals(id)) throw new BadRequestException("There is already a restaurant called " + body.name() + " in " + body.city() + " at the address " + body.address());
        found.setAddress(body.address());
        found.setCity(body.city());
        found.setName(body.name());
        found.setEmail(body.email());
        found.setPhoneNumber(body.phoneNumber());
        return this.repository.save(found);
    }

}
