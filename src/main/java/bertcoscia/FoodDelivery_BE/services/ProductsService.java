package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.Product;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewProductsDTO;
import bertcoscia.FoodDelivery_BE.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductsService {
    @Autowired
    ProductsRepository repository;

    public Product save(NewProductsDTO body) {
        if (this.repository.existsByName(body.name())) throw new BadRequestException("There is already a product called " + body.name());
        return this.repository.save(new Product(body.name(), body.price(), body.description()));
    }

    public Product findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public List<Product> findAllById(List<UUID> productIds) {
        return this.repository.findAllById(productIds);
    }


}
