package bertcoscia.FoodDelivery_BE.controllers;

import bertcoscia.FoodDelivery_BE.entities.Menu;
import bertcoscia.FoodDelivery_BE.entities.Restaurant;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.payloads.NewEntitiesRespDTO;
import bertcoscia.FoodDelivery_BE.payloads.NewMenusDTO;
import bertcoscia.FoodDelivery_BE.services.MenusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menus")
public class MenusController {
    @Autowired
    MenusService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('RESTAURANT', 'ADMIN')")
    public NewEntitiesRespDTO save(@RequestBody @Validated NewMenusDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(body).getIdMenu());
        }
    }

    @GetMapping("/my-menus")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public Menu findMyMenu(@AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant) {
        return this.service.findByResaturantId(currentAuthenticatedRestaurant.getIdUser());
    }

    @PutMapping("/my-menus")
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public Menu editMyMenu(@AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant, @RequestBody @Validated Menu body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            Menu myMenu = this.findMyMenu(currentAuthenticatedRestaurant);
            return this.service.updateMyMenu(myMenu, body);
        }
    }

    @PutMapping("/:{idMenu}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Menu findByIdAndUpdate(UUID idMenu, @RequestBody @Validated Menu body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idMenu, body);
        }
    }

    @DeleteMapping("/my-menus")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('RESTAURANT')")
    public void deleteMyMenu(@AuthenticationPrincipal Restaurant currentAuthenticatedRestaurant) {
        this.service.deleteMyMenu(this.findMyMenu(currentAuthenticatedRestaurant));
    }

    @DeleteMapping("/:{idMenu}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(UUID idMenu) {
        this.service.findByIdAndDelete(idMenu);
    }



}
