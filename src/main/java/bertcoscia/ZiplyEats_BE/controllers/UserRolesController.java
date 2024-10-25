package bertcoscia.ZiplyEats_BE.controllers;

import bertcoscia.ZiplyEats_BE.entities.UserRole;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.payloads.responses.NewEntitiesRespDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewUserRolesDTO;
import bertcoscia.ZiplyEats_BE.services.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-roles")
public class UserRolesController {
    @Autowired
    UserRolesService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public NewEntitiesRespDTO save(
            @RequestBody @Validated NewUserRolesDTO body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return new NewEntitiesRespDTO(this.service.save(body).getIdRole());
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<UserRole> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "userRole") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return this.service.findAll(page, size, sortBy, direction);
    }

    @GetMapping("/id/{idUserRole}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRole findById(@PathVariable UUID idUserRole) {
        return this.service.findById(idUserRole);
    }

    @GetMapping("/role/{userRole}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRole findByUserRole(@PathVariable String userRole) {
        return this.service.findByUserRole(userRole);
    }

    @DeleteMapping("/{idUserRole}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID idUserRole) {
        this.service.findByIdAndDelete(idUserRole);
    }

    @PutMapping("/{idUserRole}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRole findByIdAndUpdate(
            @PathVariable UUID idUserRole,
            @RequestBody @Validated UserRole body,
            BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messages = validationResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(messages);
        } else {
            return this.service.findByIdAndUpdate(idUserRole, body);
        }
    }

}
