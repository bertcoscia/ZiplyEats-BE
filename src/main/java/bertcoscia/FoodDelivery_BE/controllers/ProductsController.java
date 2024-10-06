package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.NewEntitiesRespDTO;
import bertcoscia.FoodDelivery_BE.payloads.NewProductsDTO;
import bertcoscia.FoodDelivery_BE.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    ProductsService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'RESTAURANT')")
    public NewEntitiesRespDTO save(@RequestBody @Validated NewProductsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(new NewProductsDTO(body.name(), body.price(), body.description())).getIdProduct());
        }
    }
}
