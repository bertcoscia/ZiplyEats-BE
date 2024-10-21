package bertcoscia.ZiplyEats_BE.controllers;

import bertcoscia.ZiplyEats_BE.entities.ProductCategory;
import bertcoscia.ZiplyEats_BE.entities.Restaurant;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.payloads.edit.EditProductCategoriesDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewProductCategoriesDTO;
import bertcoscia.ZiplyEats_BE.payloads.responses.NewEntitiesRespDTO;
import bertcoscia.ZiplyEats_BE.services.ProductCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product-categories")
public class ProductCategoriesController {
    @Autowired
    ProductCategoriesService service;

    @PostMapping
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public NewEntitiesRespDTO save(
            @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant,
            @RequestBody @Validated NewProductCategoriesDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(currentAuthenticatedRestaurant.getIdUser(), body).getIdProductCategory());
        }
    }

    @GetMapping("/my-product-categories")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public List<ProductCategory> getMyProductCategories(@AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant) {
        return this.service.findAllByRestaurant(currentAuthenticatedRestaurant.getIdUser());
    }

    @PatchMapping("/my-product-categories/{idProductCategory}")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public ProductCategory editMyProductCategory(
            @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant,
            @PathVariable UUID idProductCategory,
            @RequestBody @Validated EditProductCategoriesDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyProductCategory(currentAuthenticatedRestaurant.getIdUser(), idProductCategory, body);
        }
    }

    @DeleteMapping("/my-product-categories/{idProductCategory}")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public void deleteMyProductCategory(
            @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant,
            @PathVariable UUID idProductCategory) {
        this.service.deleteMyProductCategory(idProductCategory, currentAuthenticatedRestaurant.getIdUser());
    }
}
