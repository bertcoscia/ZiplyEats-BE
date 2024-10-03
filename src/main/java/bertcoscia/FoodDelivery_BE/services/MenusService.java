package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.Menu;
import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.exceptions.UnauthorizedException;
import bertcoscia.FoodDelivery_BE.payloads.NewMenusDTO;
import bertcoscia.FoodDelivery_BE.repositories.MenusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class MenusService {
    @Autowired
    MenusRepository repository;

    @Autowired
    RestaurantsService restaurantsService;

    public Menu save(NewMenusDTO body) {
        Restaurant restaurantFound = this.restaurantsService.findById(UUID.fromString(body.idRestaurant()));
        if (this.repository.existsByRestaurantIdRestaurant(UUID.fromString(body.idRestaurant()))) throw new BadRequestException("The restaurant " + restaurantFound.getName() + " already has a menu");
        return this.repository.save(new Menu(restaurantFound));
    }

    public Menu findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public Menu findByResaturantId(UUID idRestaurant) {
        Restaurant restaurantFound = this.restaurantsService.findById(idRestaurant);
        return this.repository.findByRestaurantIdRestaurant(idRestaurant).orElseThrow(()-> new NotFoundException("Could not find menu for the restaurant " + restaurantFound.getName()));
    }

    public Page<Menu> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return repository.findAll(pageable);
    }

    public void findByIdAndDelete(UUID id) {
        Menu found = this.findById(id);
        this.repository.delete(found);
    }

    public Menu findByIdAndUpdate(UUID id, Menu body) {
        Menu found = this.findById(id);
        if (this.repository.existsByRestaurantIdRestaurant(body.getRestaurant().getIdRestaurant()) && !found.getIdMenu().equals(body.getIdMenu())) throw new BadRequestException("The restaurant " + found.getRestaurant().getName() + " already has a menu");
        found.setProductList(body.getProductList());
        return this.repository.save(found);
    }

    public Menu updateMyMenu(Menu myMenu, Menu body) {
        if (!myMenu.getRestaurant().getIdRestaurant().equals(body.getRestaurant().getIdRestaurant())) throw new UnauthorizedException("You do not have the permission to edit this item");
        myMenu.setProductList(body.getProductList());
        return this.repository.save(myMenu);
    }

    public void deleteMyMenu(Menu myMenu) {
        this.repository.delete(myMenu);
    }
}
