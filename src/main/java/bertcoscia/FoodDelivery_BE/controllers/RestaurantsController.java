package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.entities.User;
import bertcoscia.FoodDelivery_BE.services.RestaurantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantsController {
    @Autowired
    RestaurantsService service;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Restaurant> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam Map<String, String> params) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return this.service.findAll(page, size, sortBy, direction, params);
    }

    @GetMapping("/{idRestaurant}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Restaurant findById(@PathVariable UUID idRestaurant) {
        return this.service.findById(idRestaurant);
    }

    @GetMapping("/my-restaurant")
    public Restaurant getMyRestaurant(@AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant) {
        return this.service.findById(currentAuthenticatedRestaurant.getIdUser());
    }
}
