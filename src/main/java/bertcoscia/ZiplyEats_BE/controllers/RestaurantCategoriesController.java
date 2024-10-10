package bertcoscia.ZiplyEats_BE.controllers;

import bertcoscia.ZiplyEats_BE.entities.RestaurantCategory;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.payloads.responses.NewEntitiesRespDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewRestaurantCategoriesDTO;
import bertcoscia.ZiplyEats_BE.services.RestaurantsCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurant-categories")
public class RestaurantCategoriesController {
    @Autowired
    RestaurantsCategoriesService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public NewEntitiesRespDTO save(@RequestBody @Validated NewRestaurantCategoriesDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(body).getIdCategory());
        }
    }

    @GetMapping
    public Page<RestaurantCategory> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam Map<String, String> params) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortBy = "restaurantCategory";
        return this.service.findAll(page, size, sortBy, direction, params);
    }

    @GetMapping("/id/{idRestaurantCategory}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestaurantCategory findById(@PathVariable UUID idRestaurantCategory) {
        return this.service.findById(idRestaurantCategory);
    }

    @GetMapping("/category/{restaurantCategory}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestaurantCategory findByRestaurantCategory(@PathVariable String restaurantCategory) {
        return this.service.findByRestaurantCategory(restaurantCategory);
    }

    @DeleteMapping("/{idRestaurantCategory}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idRestaurantCategory) {
        this.service.findByIdAndDelete(idRestaurantCategory);
    }

    @PutMapping("/{idRestaurantCategory}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestaurantCategory findByIdAndUpdate(@PathVariable UUID idRestaurantCategory, @RequestBody @Validated RestaurantCategory body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idRestaurantCategory, body);
        }
    }
}
