package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.Rider;
import bertcoscia.FoodDelivery_BE.entities.UserRole;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.NewUsersDTO;
import bertcoscia.FoodDelivery_BE.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RidersService {
    @Autowired
    UsersRepository repository;

    @Autowired
    UserRolesService userRolesService;

    public Rider save(NewUsersDTO body) {
        if (this.repository.existsByEmail(body.email())) throw new BadRequestException("Email already used");
        if (this.repository.existsByPhoneNumber(body.phoneNumber())) throw new BadRequestException("Phone number already used");
        UserRole userRole = this.userRolesService.findByUserRole("RIDER");
        Rider newRider = new Rider(body.name(), body.surname(), body.email(), body.password(), body.phoneNumber(), body.address(), body.city(), userRole);
        newRider.setAvatarUrl("https://ui-avatars.com/api/?name=" + newRider.getName() + "+" + newRider.getSurname());
        return this.repository.save(newRider);
    }

    public Optional<Rider> findById(UUID id) {
        return this.repository.findById(id)
                .filter(Rider.class::isInstance)  // Filtro per Rider
                .map(Rider.class::cast);          // Cast a Rider
    }

    public Page<Rider> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable).map(Rider.class::cast);
    }
}
