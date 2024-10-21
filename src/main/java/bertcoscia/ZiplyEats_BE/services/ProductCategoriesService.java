package bertcoscia.ZiplyEats_BE.services;

import bertcoscia.ZiplyEats_BE.entities.ProductCategory;
import bertcoscia.ZiplyEats_BE.entities.Restaurant;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.exceptions.NotFoundException;
import bertcoscia.ZiplyEats_BE.exceptions.UnauthorizedException;
import bertcoscia.ZiplyEats_BE.payloads.edit.EditProductCategoriesDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewProductCategoriesDTO;
import bertcoscia.ZiplyEats_BE.repositories.ProductCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductCategoriesService {
    @Autowired
    ProductCategoriesRepository repository;

    @Autowired
    RestaurantsService restaurantsService;

    public ProductCategory save(UUID idRestaurant, NewProductCategoriesDTO body) {
        Restaurant restaurantFound = this.restaurantsService.findById(idRestaurant);
        if (this.repository.existsByProductCategoryAndRestaurantIdUser(body.productCategory(), restaurantFound.getIdUser())) throw new BadRequestException("The restaurant " + restaurantFound.getName() + " already has a product category called " + body.productCategory());
        return this.repository.save(new ProductCategory(body.productCategory(), restaurantFound));
    }

    public List<ProductCategory> findAllByRestaurant (UUID idRestaurant) {
        return this.repository.findAllByRestaurantIdUserOrderByProductCategoryAsc(idRestaurant);
    }

    public ProductCategory findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public ProductCategory findByRestaurantAndProductCategory(UUID idRestaurant, String productCategory) {
        return this.repository.findByRestaurantIdUserAndProductCategoryIgnoreCase(idRestaurant, productCategory).orElseThrow(()-> new NotFoundException("Could not find the category " + productCategory));
    }

    public ProductCategory editMyProductCategory(UUID idRestaurant, UUID idProductCategory, EditProductCategoriesDTO body) {
        ProductCategory found = this.findById(idProductCategory);
        if (!found.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorized to edit this product category.");
        if (this.repository.existsByProductCategoryAndRestaurantIdUser(body.productCategory(), idRestaurant) && !found.getIdProductCategory().equals(idProductCategory)) throw new BadRequestException("The restaurant already has a product called " + body.productCategory());
        found.setProductCategory(body.productCategory());
        return this.repository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public void deleteMyProductCategory(UUID idProductCategory, UUID idRestaurant) {
        ProductCategory found = this.findById(idProductCategory);
        if (!found.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorized to delete this product category.");
        this.repository.delete(found);
    }




}
