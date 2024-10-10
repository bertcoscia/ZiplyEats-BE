package bertcoscia.ZiplyEats_BE.controllers;

import bertcoscia.ZiplyEats_BE.entities.Restaurant;
import bertcoscia.ZiplyEats_BE.entities.Topping;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.payloads.edit.EditToppingsDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewToppingsDTO;
import bertcoscia.ZiplyEats_BE.payloads.responses.NewEntitiesRespDTO;
import bertcoscia.ZiplyEats_BE.services.ToppingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/toppings")
public class ToppingsController {
    @Autowired
    ToppingsService service;

    @PostMapping()
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public NewEntitiesRespDTO save(@AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant, @RequestBody @Validated NewToppingsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(currentAuthenticatedRestaurant.getIdUser(), body).getIdProduct());
        }
    }

    @GetMapping("/my-toppings")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public Page<Topping> findAllMyToppings(
            @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Map<String, String> params) {
        String sortBy = "name";
        Sort.Direction direction = Sort.Direction.ASC;
        return this.service.findAllMyToppings(currentAuthenticatedRestaurant.getIdUser(), page, size, sortBy, direction, params);
    }

    @GetMapping("/{idRestaurant}")
    public Page<Topping> findAllToppingsByIdRestaurant(
            @PathVariable UUID idRestaurant,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Map<String, String> params) {
        String sortBy = "name";
        Sort.Direction direction = Sort.Direction.ASC;
        return this.service.findAllByIdRestaurant(idRestaurant, page, size, sortBy, direction, params);
    }

    @PutMapping("/my-toppings/{idTopping}")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public Topping editMyTopping(
            @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant,
            @PathVariable UUID idTopping,
            @RequestBody @Validated EditToppingsDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyTopping(currentAuthenticatedRestaurant.getIdUser(), idTopping, body);
        }
    }

    @PutMapping("/{idTopping}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Topping findByIdAndUpdate(
            @PathVariable UUID idTopping,
            @RequestBody @Validated EditToppingsDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idTopping, body);
        }
    }

    @DeleteMapping("my-toppings/{idTopping}")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public void deleteMyTopping(
            @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant,
            @PathVariable UUID idTopping) {
        this.service.deleteMyTopping(currentAuthenticatedRestaurant.getIdUser(), idTopping);
    }

    @DeleteMapping("/{idTopping}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idTopping) {
        this.service.findByIdAndDelete(idTopping);
    }

}
