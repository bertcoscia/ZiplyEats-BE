package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.entities.Product;
import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.edit.EditRestaurantsDTO;
import bertcoscia.FoodDelivery_BE.services.ProductsService;
import bertcoscia.FoodDelivery_BE.services.RestaurantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurants")
public class RestaurantsController {
    @Autowired
    RestaurantsService service;

    @Autowired
    ProductsService productsService;

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

    @PutMapping("/my-restaurant")
    public Restaurant editMyRestaurant(@AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant, @RequestBody @Validated EditRestaurantsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(currentAuthenticatedRestaurant.getIdUser(), body);
        }
    }

    @DeleteMapping("/my-restaurant")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyRestaurant(@AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant) {
        this.service.findByIdAndDelete(currentAuthenticatedRestaurant.getIdUser());
    }

    @PutMapping("/{idRestaurant}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Restaurant findByIdAndUpdate(@PathVariable UUID idRestaurant, @RequestBody @Validated EditRestaurantsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idRestaurant, body);
        }
    }

    @DeleteMapping("/{idRestaurant}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idRestaurant) {
        this.service.findByIdAndDelete(idRestaurant);
    }

    @GetMapping("/{idRestaurant}/products")
    public Page<Product> findAllProductsByRestaurant(
            @PathVariable UUID idRestaurant,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam Map<String, String> params) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return this.productsService.findAllByRestaurant(idRestaurant, page, size, sortBy, direction, params);
    }

}
