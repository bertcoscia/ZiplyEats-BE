package bertcoscia.ZiplyEats_BE.services;

import bertcoscia.ZiplyEats_BE.entities.RestaurantCategory;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.exceptions.NotFoundException;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewRestaurantCategoriesDTO;
import bertcoscia.ZiplyEats_BE.repositories.RestaurantCategoriesRepository;
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
public class RestaurantsCategoriesService {
    @Autowired
    RestaurantCategoriesRepository repository;

    public RestaurantCategory save(NewRestaurantCategoriesDTO body) {
        if (this.repository.existsByRestaurantCategory(body.restaurantCategory())) throw new BadRequestException("Category " + body.restaurantCategory() + " already existing");
        return this.repository.save(new RestaurantCategory(body.restaurantCategory()));
    }

    public RestaurantCategory findByRestaurantCategory(String restaurantCategory) {
        return this.repository.findByRestaurantCategoryIgnoreCase(restaurantCategory).orElseThrow(()-> new NotFoundException("Could not find restaurant category " + restaurantCategory));
    }

    public RestaurantCategory findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public Page<RestaurantCategory> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable);
    }

    public void findByIdAndDelete(UUID idRestaurantCategory) {
        this.repository.delete(this.findById(idRestaurantCategory));
    }

    public RestaurantCategory findByIdAndUpdate(UUID idRestaurantCategory, RestaurantCategory body) {
        RestaurantCategory found = this.findById(idRestaurantCategory);
        if (this.repository.existsByRestaurantCategory(body.getRestaurantCategory()) && !found.getIdCategory().equals(body.getIdCategory())) throw new BadRequestException("Category " + body.getRestaurantCategory() + " already existing");
        found.setRestaurantCategory(body.getRestaurantCategory());
        return this.repository.save(found);
    }

    public List<RestaurantCategory> findAllList() {
        return this.repository.findAll();
    }
}
