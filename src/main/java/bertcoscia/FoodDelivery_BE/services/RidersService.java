package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.Rider;
import bertcoscia.FoodDelivery_BE.entities.User;
import bertcoscia.FoodDelivery_BE.entities.UserRole;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.EditUsersDTO;
import bertcoscia.FoodDelivery_BE.payloads.NewUsersDTO;
import bertcoscia.FoodDelivery_BE.repositories.UsersRepository;
import bertcoscia.FoodDelivery_BE.specs.UsersSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
public class RidersService {
    @Autowired
    UsersRepository repository;

    @Autowired
    UserRolesService userRolesService;

    @Autowired
    PasswordEncoder bcrypt;

    public Rider save(NewUsersDTO body) {
        if (this.repository.existsByEmail(body.email())) throw new BadRequestException("Email already used");
        if (this.repository.existsByPhoneNumber(body.phoneNumber())) throw new BadRequestException("Phone number already used");
        UserRole userRole = this.userRolesService.findByUserRole("RIDER");
        Rider newRider = new Rider(body.name(), body.surname(), body.email(), bcrypt.encode(body.password()), body.phoneNumber(), body.address(), body.city(), userRole);
        newRider.setAvatarUrl("https://ui-avatars.com/api/?name=" + newRider.getName() + "+" + newRider.getSurname() + "&background=048C7A&color=fff");
        return this.repository.save(newRider);
    }

    public Rider findById(UUID id) {
        return this.repository.findRiderByIdUser(id).orElseThrow(()-> new NotFoundException(id));
    }

    public Page<User> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 100) page = 100;
        Specification<User> spec = UsersSpec.isRider("rider");
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(spec, pageable);
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public Rider findByIdAndUpdate(UUID id, EditUsersDTO body) {
        Rider found = this.findById(id);
        if (this.repository.existsByPhoneNumber(body.phoneNumber()) && !found.getIdUser().equals(id)) throw new BadRequestException("Phone number already used");
        if (this.repository.existsByEmail(body.email()) && !found.getIdUser().equals(id)) throw new BadRequestException("Email already used");
        found.setName(body.name());
        found.setSurname(body.surname());
        found.setEmail(body.email());
        found.setPhoneNumber(body.phoneNumber());
        found.setAddress(body.address());
        found.setCity(body.city());
        if (found.getAvatarUrl().contains("https://ui-avatars.com/api/?name=")) {
            String encodedName;
            String encodedSurname;
            encodedName = URLEncoder.encode(body.name(), StandardCharsets.UTF_8);
            encodedSurname = URLEncoder.encode(body.surname(), StandardCharsets.UTF_8);
            found.setAvatarUrl("https://ui-avatars.com/api/?name=" + encodedName + "+" + encodedSurname + "&background=048C7A&color=fff");
        }
        return this.repository.save(found);
    }
}
