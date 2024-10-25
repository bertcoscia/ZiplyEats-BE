package bertcoscia.ZiplyEats_BE.controllers;

import bertcoscia.ZiplyEats_BE.entities.Rider;
import bertcoscia.ZiplyEats_BE.entities.User;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.payloads.edit.editUser.*;
import bertcoscia.ZiplyEats_BE.payloads.responses.EditUsersPasswordRespDTO;
import bertcoscia.ZiplyEats_BE.services.RidersService;
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
@RequestMapping("/riders")
public class RidersController {
    @Autowired
    RidersService service;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam Map<String, String> params) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return this.service.findAll(page, size, sortBy, direction, params);
    }

    @GetMapping("/{idRider}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Rider findById(@PathVariable UUID idRider) {
        return this.service.findById(idRider);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('RIDER')")
    public Rider getMyProfile(@AuthenticationPrincipal Rider currentAuthenticatedRider) {
        return this.service.findById(currentAuthenticatedRider.getIdUser());
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('RIDER')")
    public Rider updateMyProfile(@AuthenticationPrincipal Rider currentAuthenticatedRider, @RequestBody @Validated EditUsersDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(currentAuthenticatedRider.getIdUser(), body);
        }
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasAuthority('RIDER')")
    public void deleteMyProfile(@AuthenticationPrincipal Rider currentAuthenticatedRider) {
        this.service.findById(currentAuthenticatedRider.getIdUser());
    }

    @DeleteMapping("/{idRider}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idRider) {
        this.service.findByIdAndDelete(idRider);
    }

    @PutMapping("/{idRider}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Rider findByIdAndUpdate(@PathVariable UUID idRider, @RequestBody @Validated EditUsersDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idRider, body);
        }
    }

    @PatchMapping("/me/edit-name+surname")
    @PreAuthorize("hasAuthority('RIDER')")
    public Rider editMyNameAndSurname(
            @AuthenticationPrincipal Rider currentAuthenticatedRider,
            @RequestBody @Validated EditUsersNameAndSurnameDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyNameAndSurname(currentAuthenticatedRider.getIdUser(), body);
        }
    }

    @PatchMapping("/me/edit-email")
    @PreAuthorize("hasAuthority('RIDER')")
    public Rider editMyEmail(
            @AuthenticationPrincipal Rider currentAuthenticatedRider,
            @RequestBody @Validated EditUsersEmailDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyEmail(currentAuthenticatedRider.getIdUser(), body);
        }
    }

    @PatchMapping("/me/edit-phoneNumber")
    @PreAuthorize("hasAuthority('RIDER')")
    public Rider editMyPhoneNumber(
            @AuthenticationPrincipal Rider currentAuthenticatedRider,
            @RequestBody @Validated EditUsersPhoneNumberDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyPhoneNumber(currentAuthenticatedRider.getIdUser(), body);
        }
    }

    @PatchMapping("/me/edit-password")
    @PreAuthorize("hasAuthority('RIDER')")
    public EditUsersPasswordRespDTO editMyPassword(
            @AuthenticationPrincipal Rider currentAuthenticatedRider,
            @RequestBody @Validated EditUsersPasswordDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyPassword(currentAuthenticatedRider.getIdUser(), body);
        }
    }

    @PatchMapping("/me/edit-address")
    @PreAuthorize("hasAuthority('RIDER')")
    public Rider editMyAddress(
            @AuthenticationPrincipal Rider currentAuthenticatedRider,
            @RequestBody @Validated EditUsersAdressDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyAddress(currentAuthenticatedRider.getIdUser(), body);
        }
    }

}
