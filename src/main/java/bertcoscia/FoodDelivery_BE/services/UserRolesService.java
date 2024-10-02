package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.entities.UserRole;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewUserRolesDTO;
import bertcoscia.FoodDelivery_BE.repositories.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class UserRolesService {
    @Autowired
    UserRolesRepository repository;

    public UserRole save(NewUserRolesDTO body) {
        if (this.repository.existsByUserRole(body.userRole())) throw new BadRequestException("Role " + body.userRole() + " already existing");
        return this.repository.save(new UserRole(body.userRole()));
    }

    public UserRole findById(UUID id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public UserRole findByUserRole(String userRole) {
        return this.repository.findByUserRole(userRole).orElseThrow(()-> new NotFoundException("Could not find role " + userRole));
    }

    public Page<UserRole> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 5) page = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable);
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public UserRole findByIdAndUpdate(UUID id, UserRole body) {
        UserRole found = this.findById(id);
        if (this.repository.existsByUserRole(body.getUserRole()) && !found.getIdRole().equals(body.getIdRole())) throw new BadRequestException("Role " + body.getUserRole() + " already existing");
        found.setUserRole(body.getUserRole());
        return this.repository.save(found);
    }


}
