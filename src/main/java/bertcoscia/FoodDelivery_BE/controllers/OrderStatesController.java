package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.entities.OrderState;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.NewEntitiesRespDTO;
import bertcoscia.FoodDelivery_BE.payloads.NewOrderStatesDTO;
import bertcoscia.FoodDelivery_BE.services.OrderStatesService;
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
@RequestMapping("/orders")
public class OrderStatesController {
    @Autowired
    OrderStatesService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public NewEntitiesRespDTO save(@RequestBody @Validated NewOrderStatesDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(body).getIdOrderState());
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrderState> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/:{idOrderState}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderState findById(@PathVariable UUID idOrderState) {
        return this.service.findById(idOrderState);
    }

    @GetMapping("/:{orderState}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderState findByOrderState(@PathVariable String orderState) {
        return this.service.findByOrderState(orderState);
    }

    @DeleteMapping("/:{idOrderState}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idOrderState) {
        this.service.findByIdAndDelete(idOrderState);
    }

    @DeleteMapping("/:{orderState}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByOrderStateAndDelete(@PathVariable String orderState) {
        this.service.findByOrderStateAndDelete(orderState);
    }

    @PutMapping("/:{idOrderState}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderState findByIdAndUpdate(@PathVariable UUID idOrderState, @RequestBody @Validated OrderState body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idOrderState, body);
        }
    }
}
