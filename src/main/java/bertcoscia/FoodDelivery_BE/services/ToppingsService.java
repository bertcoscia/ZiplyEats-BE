package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.Topping;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewToppingsDTO;
import bertcoscia.FoodDelivery_BE.repositories.ToppingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ToppingsService {
    @Autowired
    ToppingsRepository repository;

    public Topping save(NewToppingsDTO body) {
        if (this.repository.existsByName(body.name())) throw new BadRequestException("There is already a topping called " + body.name());
        return this.repository.save(new Topping(body.name(), body.price()));
    }

    public Topping findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public Page<Topping> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 15) page = 15;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable);
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public Topping findByIdAndUpdate(UUID id, Topping body) {
        Topping found = this.findById(id);
        if (this.repository.existsByName(body.getName()) && !found.getIdTopping().equals(body.getIdTopping())) throw new BadRequestException("There is already a topping called " + body.getName());
        found.setName(body.getName());
        found.setPrice(body.getPrice());
        return this.repository.save(found);
    }


}
