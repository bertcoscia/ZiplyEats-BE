package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.User;
import bertcoscia.FoodDelivery_BE.entities.UserRole;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.EditUsersDTO;
import bertcoscia.FoodDelivery_BE.payloads.NewUsersDTO;
import bertcoscia.FoodDelivery_BE.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    UsersRepository repository;

    @Autowired
    UserRolesService userRolesService;

    @Autowired
    PasswordEncoder bcrypt;

    public User save(NewUsersDTO body) {
        if (this.repository.existsByEmail(body.email())) throw new BadRequestException("Email already used");
        if (this.repository.existsByPhoneNumber(body.phoneNumber())) throw new BadRequestException("Phone number already used");
        UserRole userRole = this.userRolesService.findByUserRole("USER");
        User newUser = new User(body.name(), body.surname(), body.email(), bcrypt.encode(body.password()), body.phoneNumber(), body.address(), body.city(), userRole);
        String encodedName;
        String encodedSurname;
        encodedName = URLEncoder.encode(newUser.getName(), StandardCharsets.UTF_8);
        encodedSurname = URLEncoder.encode(newUser.getSurname(), StandardCharsets.UTF_8);
        String defaultAvatarPrefix = "https://ui-avatars.com/api/?name=";
        String defaultAvatarBackground = "&background=048C7A&color=fff";
        newUser.setAvatarUrl(defaultAvatarPrefix + encodedName + "+" + encodedSurname + defaultAvatarBackground);
        return this.repository.save(newUser);
    }

    public User findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public Page<User> findAll(int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAll(pageable);
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public User findByIdAndUpdate(UUID id, EditUsersDTO body) {
        User found = this.findById(id);
        if (this.repository.existsByEmail(body.email()) && !found.getIdUser().equals(id)) throw new BadRequestException("Email already used");
        if (this.repository.existsByPhoneNumber(body.phoneNumber()) && !found.getIdUser().equals(id)) throw new BadRequestException("Phone number already used");
        found.setName(body.name());
        found.setSurname(body.surname());
        found.setEmail(body.email());
        found.setPhoneNumber(body.phoneNumber());
        found.setAddress(body.address());
        found.setCity(body.city());
        String defaultAvatarPrefix = "https://ui-avatars.com/api/?name=";
        String defaultAvatarBackground = "&background=048C7A&color=fff";
        boolean nameOrSurnameChanged = !found.getName().equals(body.name()) || !found.getSurname().equals(body.surname());
        if (found.getAvatarUrl().startsWith(defaultAvatarPrefix) && nameOrSurnameChanged) {
            String encodedName = URLEncoder.encode(body.name(), StandardCharsets.UTF_8);
            String encodedSurname = URLEncoder.encode(body.surname(), StandardCharsets.UTF_8);
            found.setAvatarUrl(defaultAvatarPrefix + encodedName + "+" + encodedSurname + defaultAvatarBackground);
        }
        return this.repository.save(found);
    }

    public User findByEmail(String email) {
        return this.repository.findByEmail(email).orElseThrow(()-> new NotFoundException("Could not find a user with email " + email));
    }
}
