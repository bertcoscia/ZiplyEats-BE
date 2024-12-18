package bertcoscia.ZiplyEats_BE.services;

import bertcoscia.ZiplyEats_BE.entities.Product;
import bertcoscia.ZiplyEats_BE.entities.ProductCategory;
import bertcoscia.ZiplyEats_BE.entities.Restaurant;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.exceptions.NotFoundException;
import bertcoscia.ZiplyEats_BE.exceptions.UnauthorizedException;
import bertcoscia.ZiplyEats_BE.payloads.edit.EditProductsDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewProductsDTO;
import bertcoscia.ZiplyEats_BE.payloads.responses.CloudinaryRespDTO;
import bertcoscia.ZiplyEats_BE.repositories.ProductsRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductsService {
    @Autowired
    ProductsRepository repository;

    @Autowired
    RestaurantsService restaurantsService;

    @Autowired
    ProductCategoriesService productCategoriesService;

    @Autowired
    Cloudinary cloudinary;

    public Product save(
            UUID idRestaurant,
            NewProductsDTO body) {
        Restaurant restaurantFound = this.restaurantsService.findById(idRestaurant);
        ProductCategory productCategoryFound = this.productCategoriesService.findByRestaurantAndProductCategory(idRestaurant, body.productCategory());
        if (this.repository.existsByNameAndRestaurantIdUser(body.name(), restaurantFound.getIdUser())) throw new BadRequestException("The restaurant " + restaurantFound.getName() + " already has a product called " + body.name());
        return this.repository.save(new Product(body.name(), body.price(), body.description(), restaurantFound, productCategoryFound, body.canHaveToppings()));
    }

    public Product findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    // THIS METHOD IS USED IN ORDERSSERVICE TO FIND ALL PRODUCTS IN THE DTO LIST
    public List<Product> findAllById(List<UUID> productIds) {
        return this.repository.findAllById(productIds);
    }

    public List<Product> findAllByRestaurant(UUID idRestaurant) {
        return this.repository.findAllByRestaurantIdUserAndDescriptionIsNotNull(idRestaurant);
    }

    public Page<Product> findAll(
            int page,
            int size,
            String sortBy,
            Sort.Direction direction,
            Map<String, String> params) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable);
    }

    public Product editMyProduct(
            UUID idRestaurant,
            UUID idProduct,
            EditProductsDTO body) {
        Product productFound = this.findById(idProduct);
        if (!productFound.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorized to edit this product.");
        if (this.repository.existsByNameAndRestaurantIdUser(body.name(), idRestaurant) && !productFound.getIdProduct().equals(idProduct)) throw new BadRequestException("The restaurant already has a product called " + body.name());
        ProductCategory productCategoryFound = this.productCategoriesService.findByRestaurantAndProductCategory(idRestaurant, body.productCategory());
        productFound.setName(body.name());
        productFound.setPrice(body.price());
        productFound.setDescription(body.description());
        productFound.setProductCategory(productCategoryFound);
        productFound.setCanHaveToppings(body.canHaveToppings());
        return this.repository.save(productFound);
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public void deleteMyProduct(
            UUID idProduct,
            UUID idRestaurant) {
        Product found = this.findById(idProduct);
        if (!found.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorized to delete this product.");
        this.repository.delete(found);
    }

    public Product findByIdAndUpdate(
            UUID idProduct,
            EditProductsDTO body) {
        Product found = this.findById(idProduct);
        if (this.repository.existsByNameAndRestaurantIdUserAndIdProductNot(body.name(), found.getRestaurant().getIdUser(), idProduct)) {
            throw new BadRequestException("The restaurant " + found.getRestaurant().getName() + " already has a product called " + body.name());
        }
        found.setPrice(body.price());
        found.setName(body.name());
        found.setDescription(body.description());
        found.setCanHaveToppings(body.canHaveToppings());
        return this.repository.save(found);
    }

    public CloudinaryRespDTO uploadProductImage(
            MultipartFile file,
            UUID idProduct,
            UUID idRestaurant) throws IOException {
        Product found = this.findById(idProduct);
        if (!found.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorised to edit this item");
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setImageUrl(url);
        this.repository.save(found);
        return new CloudinaryRespDTO("Product image successfully uploaded");
    }

}
