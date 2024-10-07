package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.entities.OrderStatus;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.responses.NewEntitiesRespDTO;
import bertcoscia.FoodDelivery_BE.payloads.newEntities.NewOrderStatusesDTO;
import bertcoscia.FoodDelivery_BE.services.OrderStatusesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order-statuses")
public class OrderStatusesController {
    @Autowired
    OrderStatusesService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public NewEntitiesRespDTO save(@RequestBody @Validated NewOrderStatusesDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(body).getIdOrderStatus());
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrderStatus> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/id/{idOrderStatus}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderStatus findById(@PathVariable UUID idOrderStatus) {
        return this.service.findById(idOrderStatus);
    }

    @GetMapping("/status/{orderStatus}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderStatus findByOrderStatus(@PathVariable String orderStatus) {
        return this.service.findByOrderStatus(orderStatus);
    }

    @DeleteMapping("/{idOrderStatus}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idOrderStatus) {
        this.service.findByIdAndDelete(idOrderStatus);
    }

    @PutMapping("/{idOrderStatus}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderStatus findByIdAndUpdate(@PathVariable UUID idOrderStatus, @RequestBody @Validated OrderStatus body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idOrderStatus, body);
        }
    }
}
