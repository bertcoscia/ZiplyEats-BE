package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.entities.Order;
import bertcoscia.FoodDelivery_BE.entities.Rider;
import bertcoscia.FoodDelivery_BE.entities.User;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.EditOrdersDTO;
import bertcoscia.FoodDelivery_BE.payloads.NewEntitiesRespDTO;
import bertcoscia.FoodDelivery_BE.payloads.NewOrdersDTO;
import bertcoscia.FoodDelivery_BE.services.OrdersService;
import bertcoscia.FoodDelivery_BE.services.RestaurantsService;
import bertcoscia.FoodDelivery_BE.services.RidersService;
import bertcoscia.FoodDelivery_BE.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    OrdersService service;

    @Autowired
    RidersService ridersService;

    @Autowired
    UsersService usersService;

    @Autowired
    RestaurantsService restaurantsService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public NewEntitiesRespDTO save(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated NewOrdersDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(" ."));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(currentAuthenticatedUser.getIdUser(), body).getIdOrder());
        }
    }

    @PutMapping("/{idOrder}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order findByIdAndUpdate(@PathVariable UUID idOrder, @RequestBody @Validated EditOrdersDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(" ."));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idOrder, body);
        }
    }

    @PatchMapping("/{idOrder}")
    @PreAuthorize("hasAuthority('RIDER')")
    public Order assignRiderToOrder(@PathVariable UUID idOrder, @AuthenticationPrincipal Rider currentAuthenticatedRider) {
        return this.service.assignRiderToOrder(idOrder, currentAuthenticatedRider.getIdUser());
    }

    @PatchMapping("/{idOrder}/finalise")
    @PreAuthorize("hasAuthority('RIDER')")
    public Order finaliseOrder(@PathVariable UUID idOrder) {
        return this.service.finaliseOrder(idOrder);
    }

    @PatchMapping("/{idOrder}/cancel")
    @PreAuthorize("hasAuthority('RIDER')")
    public Order cancelOrder(@PathVariable UUID idOrder) {
        return this.service.cancelOrder(idOrder);
    }

    @DeleteMapping("/{idOrder}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idOrder) {
        this.service.findByIdAndDelete(idOrder);
    }

}
