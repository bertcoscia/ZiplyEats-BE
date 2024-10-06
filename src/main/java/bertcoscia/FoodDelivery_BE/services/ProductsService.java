package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.Product;
import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.exceptions.UnauthorizedException;
import bertcoscia.FoodDelivery_BE.payloads.EditProductsDTO;
import bertcoscia.FoodDelivery_BE.payloads.NewProductsDTO;
import bertcoscia.FoodDelivery_BE.repositories.ProductsRepository;
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
public class ProductsService {
    @Autowired
    ProductsRepository repository;

    @Autowired
    RestaurantsService restaurantsService;

    public Product save(UUID idRestaurant, NewProductsDTO body) {
        Restaurant restaurantFound = this.restaurantsService.findById(idRestaurant);
        if (this.repository.existsByNameAndRestaurantIdUser(body.name(), restaurantFound.getIdUser())) throw new BadRequestException("The restaurant " + restaurantFound.getName() + " already has a product called " + body.name());
        return this.repository.save(new Product(body.name(), body.price(), body.description(), restaurantFound));
    }

    public Product findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    // THIS METHOD IS USED IN ORDERSSERVICE TO FIND ALL PRODUCTS IN THE DTO LIST
    public List<Product> findAllById(List<UUID> productIds) {
        return this.repository.findAllById(productIds);
    }

    public Page<Product> findAllByRestaurant(UUID idRestaurant, int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAllByRestaurantIdUser(idRestaurant, pageable);
    }

    public Page<Product> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable);
    }

    public Product editMyProduct(UUID idRestaurant, UUID idProduct, EditProductsDTO body) {
        Product found = this.findById(idProduct);
        if (!found.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorized to edit this product.");
        if (this.repository.existsByNameAndRestaurantIdUser(body.name(), idRestaurant) && !found.getIdProduct().equals(idProduct)) throw new BadRequestException("The restaurant already has a product called " + body.name());
        found.setName(body.name());
        found.setPrice(body.price());
        found.setDescription(body.description());
        return this.repository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public void deleteMyProduct(UUID idProduct, UUID idRestaurant) {
        Product found = this.findById(idProduct);
        if (!found.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorized to delete this product.");
        this.repository.delete(found);
    }

    public Product findByIdAndUpdate(UUID idProduct, EditProductsDTO body) {
        Product found = this.findById(idProduct);
        if (this.repository.existsByNameAndRestaurantIdUserAndIdProductNot(body.name(), found.getRestaurant().getIdUser(), idProduct)) {
            throw new BadRequestException("The restaurant " + found.getRestaurant().getName() + " already has a product called " + body.name());
        }
        found.setPrice(body.price());
        found.setName(body.name());
        found.setDescription(body.description());
        return this.repository.save(found);
    }
}
