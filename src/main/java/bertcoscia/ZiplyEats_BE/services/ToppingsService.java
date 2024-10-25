package bertcoscia.ZiplyEats_BE.services;

import bertcoscia.ZiplyEats_BE.entities.ProductCategory;
import bertcoscia.ZiplyEats_BE.entities.Restaurant;
import bertcoscia.ZiplyEats_BE.entities.Topping;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.exceptions.NotFoundException;
import bertcoscia.ZiplyEats_BE.exceptions.UnauthorizedException;
import bertcoscia.ZiplyEats_BE.payloads.edit.EditToppingsDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewToppingsDTO;
import bertcoscia.ZiplyEats_BE.repositories.ToppingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ToppingsService {
    @Autowired
    ToppingsRepository repository;

    @Autowired
    ProductCategoriesService productCategoriesService;

    @Autowired
    RestaurantsService restaurantsService;

    public Topping save(UUID idRestaurant, NewToppingsDTO body) {
        Restaurant restaurantFound = this.restaurantsService.findById(idRestaurant);
        if (this.repository.existsByNameAndRestaurantIdUser(body.name(), idRestaurant)) throw new BadRequestException("The restaurant " + restaurantFound.getName() + " already has a topping called " + body.name());
        ProductCategory productCategoryFound = this.productCategoriesService.findByRestaurantAndProductCategory(idRestaurant, body.productCategory());
        Topping newTopping = new Topping(body.name(), body.price(), restaurantFound, productCategoryFound);
        return this.repository.save(newTopping);
    }

    public Topping findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public Page<Topping> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 15) page = 15;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable);
    }

    public List<Topping> findAllMyToppings(UUID idRestaurant) {
        return this.repository.findAllByRestaurantIdUser(idRestaurant);
    }

    public List<Topping> findAllById(List<UUID> toppingIds) {
        return this.repository.findAllById(toppingIds);
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public Topping findByIdAndUpdate(UUID idTopping, EditToppingsDTO body) {
        Topping found = this.findById(idTopping);
        if (this.repository.existsByNameAndRestaurantIdUserAndIdProductNot(body.name(), found.getIdProduct(), idTopping)) throw new BadRequestException("The restaurant " + found.getRestaurant().getName() + " already has a topping called " + body.name());
        found.setName(body.name());
        found.setPrice(body.price());
        return this.repository.save(found);
    }

    public Topping editMyTopping(UUID idRestaurant, UUID idTopping, EditToppingsDTO body) {
        Topping found = this.findById(idTopping);
        if (!found.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorized to edit this topping");
        if (this.repository.existsByNameAndRestaurantIdUser(body.name(), idRestaurant) && !found.getRestaurant().getIdUser().equals(idRestaurant)) throw new BadRequestException("The restaurant " + found.getRestaurant().getName() + " already has a topping called " + body.name());
        found.setName(body.name());
        found.setPrice(body.price());
        return this.repository.save(found);
    }


    public List<Topping> findAllByIdRestaurant(UUID idRestaurant) {
        return this.repository.findByRestaurantIdUser(idRestaurant);
    }

    public void deleteMyTopping(UUID idRestaurant, UUID idTopping) {
        Topping found = this.findById(idTopping);
        if (!found.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorized to delete this topping");
        this.repository.delete(found);
    }
}
