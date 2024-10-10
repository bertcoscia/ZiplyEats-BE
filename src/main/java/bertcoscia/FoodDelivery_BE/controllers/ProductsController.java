package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.entities.Product;
import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.edit.EditProductsDTO;
import bertcoscia.FoodDelivery_BE.payloads.responses.CloudinaryRespDTO;
import bertcoscia.FoodDelivery_BE.payloads.responses.NewEntitiesRespDTO;
import bertcoscia.FoodDelivery_BE.payloads.newEntities.NewProductsDTO;
import bertcoscia.FoodDelivery_BE.services.ProductsService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    ProductsService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public NewEntitiesRespDTO save(@AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant, @RequestBody @Validated NewProductsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(currentAuthenticatedRestaurant.getIdUser(), body).getIdProduct());
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Product> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam Map<String, String> params) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return this.service.findAll(page, size, sortBy, direction, params);
    }

    @GetMapping("/{idProduct}")
    public Product findById(@PathVariable UUID idProduct) {
        return this.service.findById(idProduct);
    }

    @GetMapping("/my-products")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public Page<Product> getMyProducts(
            @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam Map<String, String> params) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return this.service.findAllByRestaurant(currentAuthenticatedRestaurant.getIdUser(), page, size, sortBy, direction, params);
    }

    @PutMapping("/my-products/{idProduct}")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public Product editMyProduct(@PathVariable UUID idProduct, @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant, @RequestBody @Validated EditProductsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyProduct(currentAuthenticatedRestaurant.getIdUser(), idProduct, body);
        }
    }

    @PostMapping("/my-products/{idProduct}")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public CloudinaryRespDTO uploadProductImage(
            @RequestParam("avatar") MultipartFile image,
            @PathVariable UUID idProduct,
            @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant) throws IOException {
        return this.service.uploadProductImage(image, idProduct, currentAuthenticatedRestaurant.getIdUser());
    }

    @DeleteMapping("/my-products/{idProduct}")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public void deleteMyProduct(@PathVariable UUID idProduct, @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant) {
        this.service.deleteMyProduct(idProduct, currentAuthenticatedRestaurant.getIdUser());
    }

    @PutMapping("/{idProduct}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product findByIdAndUpdate(@PathVariable UUID idProduct, @RequestBody @Validated EditProductsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idProduct, body);
        }
    }

    @DeleteMapping("/{idProduct}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idProduct) {
        this.service.findByIdAndDelete(idProduct);
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
        return this.service.findAllByRestaurant(idRestaurant, page, size, sortBy, direction, params);
    }

}
