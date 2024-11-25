package test.raven.moviescatalog.security.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.raven.moviescatalog.entities.RegularUser;
import test.raven.moviescatalog.security.data.AuthenticationDTO;
import test.raven.moviescatalog.security.data.LoginResponseDTO;
import test.raven.moviescatalog.services.UsersService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final UsersService service;

    private final  TokenService tokenService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((RegularUser) auth.getPrincipal());

        return new LoginResponseDTO(token);
    }

}

