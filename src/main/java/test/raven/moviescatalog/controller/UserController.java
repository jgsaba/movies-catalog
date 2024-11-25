package test.raven.moviescatalog.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.raven.moviescatalog.model.CreateUserDTO;
import test.raven.moviescatalog.services.UsersService;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UsersService service;

    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public void createUser(@RequestBody @Valid CreateUserDTO createUserDTO, @RequestHeader("Authorization") String authorizationHeader) {
        service.createNewUser(createUserDTO, authorizationHeader);
    }
}
