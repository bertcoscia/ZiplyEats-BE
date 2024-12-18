package bertcoscia.ZiplyEats_BE.controllers;

import bertcoscia.ZiplyEats_BE.entities.User;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.payloads.edit.editUser.*;
import bertcoscia.ZiplyEats_BE.payloads.responses.CloudinaryRespDTO;
import bertcoscia.ZiplyEats_BE.payloads.responses.EditUsersPasswordRespDTO;
import bertcoscia.ZiplyEats_BE.services.UsersService;
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
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersService service;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "surname") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam Map<String, String> params) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return this.service.findAll(page, size, sortBy, direction, params);
    }

    @GetMapping("/{idUser}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findById(@PathVariable UUID idUser) {
        return this.service.findById(idUser);
    }

    @GetMapping("/me")
    public User getMyProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return this.service.findById(currentAuthenticatedUser.getIdUser());
    }

    @PutMapping("/me")
    public User updateMyProfile(
            @AuthenticationPrincipal User currentAuthenticatedUser,
            @RequestBody @Validated EditUsersDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        }
        return this.service.findByIdAndUpdate(currentAuthenticatedUser.getIdUser(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        this.service.findByIdAndDelete(currentAuthenticatedUser.getIdUser());
    }

    @DeleteMapping("/{idUser}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idUser) {
        this.service.findByIdAndDelete(idUser);
    }

    @PutMapping("/{idUser}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findByIdAndUpdate(
            @PathVariable UUID idUser,
            @RequestBody @Validated EditUsersDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("/ "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idUser, body);
        }
    }

    @PostMapping("/me/avatar")
    public CloudinaryRespDTO uploadImage(
            @RequestParam("avatar") MultipartFile image,
            @AuthenticationPrincipal User currentAuthenticatedUser) throws IOException {
        return this.service.uploadImage(image, currentAuthenticatedUser.getIdUser());
    }

    @PatchMapping("/me/edit-name+surname")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public User editMyNameAndSurname(
            @AuthenticationPrincipal User currentAuthenticatedUser,
            @RequestBody @Validated EditUsersNameAndSurnameDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyNameAndSurname(currentAuthenticatedUser.getIdUser(), body);
        }
    }

    @PatchMapping("/me/edit-email")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public User editMyEmail(
            @AuthenticationPrincipal User currentAuthenticatedUser,
            @RequestBody @Validated EditUsersEmailDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyEmail(currentAuthenticatedUser.getIdUser(), body);
        }
    }

    @PatchMapping("/me/edit-phoneNumber")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public User editMyPhoneNumber(
            @AuthenticationPrincipal User currentAuthenticatedUser,
            @RequestBody @Validated EditUsersPhoneNumberDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyPhoneNumber(currentAuthenticatedUser.getIdUser(), body);
        }
    }

    @PatchMapping("/me/edit-password")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public EditUsersPasswordRespDTO editMyPassword(
            @AuthenticationPrincipal User currentAuthenticatedUser,
            @RequestBody @Validated EditUsersPasswordDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyPassword(currentAuthenticatedUser.getIdUser(), body);
        }
    }

    @PatchMapping("/me/edit-address")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public User editMyAddress(
            @AuthenticationPrincipal User currentAuthenticatedUser,
            @RequestBody @Validated EditUsersAdressDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.editMyAddress(currentAuthenticatedUser.getIdUser(), body);
        }
    }
}
