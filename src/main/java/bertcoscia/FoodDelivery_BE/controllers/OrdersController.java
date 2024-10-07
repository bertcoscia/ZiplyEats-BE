package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.entities.Order;
import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.entities.Rider;
import bertcoscia.FoodDelivery_BE.entities.User;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.edit.EditOrdersDTO;
import bertcoscia.FoodDelivery_BE.payloads.newEntities.NewOrdersDTO;
import bertcoscia.FoodDelivery_BE.payloads.responses.NewEntitiesRespDTO;
import bertcoscia.FoodDelivery_BE.services.OrdersService;
import bertcoscia.FoodDelivery_BE.services.RestaurantsService;
import bertcoscia.FoodDelivery_BE.services.RidersService;
import bertcoscia.FoodDelivery_BE.services.UsersService;
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

    @GetMapping("/{idOrder}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Order findById(@PathVariable UUID idOrder) {
        return this.service.findById(idOrder);
    }

    @GetMapping("/my-orders/{idOrder}")
    @PreAuthorize("hasAuthority('USER')")
    public Order findMyOrderById(@PathVariable UUID idOrder, @AuthenticationPrincipal User currentAuthenticatedUser) {
        return this.service.findMyOrderById(idOrder, currentAuthenticatedUser.getIdUser());
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

    @PatchMapping("/{idOrder}/restaurant-accepts")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public Order restaurantAcceptsOrder(@PathVariable UUID idOrder, @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant) {
        return this.service.restaurantAcceptsOrder(idOrder, currentAuthenticatedRestaurant.getIdUser());
    }

    @PatchMapping("/{idOrder}/restaurant-refuses")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public Order restaurantRefusesOrder(@PathVariable UUID idOrder, @AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant) {
        return this.service.restaurantRefusesOrder(idOrder, currentAuthenticatedRestaurant.getIdUser());
    }

    @PatchMapping("/{idOrder}/assign-rider")
    @PreAuthorize("hasAuthority('RIDER')")
    public Order assignRiderToOrder(@PathVariable UUID idOrder, @AuthenticationPrincipal Rider currentAuthenticatedRider) {
        return this.service.assignRiderToOrder(idOrder, currentAuthenticatedRider.getIdUser());
    }

    @PatchMapping("/{idOrder}/unassign-rider")
    @PreAuthorize("hasAuthority('RIDER')")
    public Order unassignRiderToOrder(@PathVariable UUID idOrder, @AuthenticationPrincipal Rider currentAuthenticatedRider) {
        return this.service.unassignRiderToOrder(idOrder, currentAuthenticatedRider.getIdUser());
    }

    @PatchMapping("{idOrder}/rider-pickup")
    @PreAuthorize("hasAuthority('RIDER')")
    public Order riderPicksUpOrder(@PathVariable UUID idOrder) {
        return this.service.riderPicksUpOrder(idOrder);
    }

    @PatchMapping("/{idOrder}/finalise")
    @PreAuthorize("hasAuthority('RIDER')")
    public Order finaliseOrder(@PathVariable UUID idOrder) {
        return this.service.finaliseOrder(idOrder);
    }

    @DeleteMapping("/{idOrder}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idOrder) {
        this.service.findByIdAndDelete(idOrder);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasAuthority('USER')")
    public Page<Order> findAllMyOrders(
            @AuthenticationPrincipal User currentAuthenticatedUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "creationDateTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam Map<String, String> params) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return this.service.findAllByUserId(currentAuthenticatedUser.getIdUser(), page, size, sortBy, direction, params);
    }
}
