package test.raven.moviescatalog.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import test.raven.moviescatalog.entities.RegularUser;
import test.raven.moviescatalog.exceptions.UserNotFoundException;
import test.raven.moviescatalog.model.CreateUserDTO;
import test.raven.moviescatalog.repositories.UsersRepository;
import test.raven.moviescatalog.security.infra.TokenService;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    private final TokenService tokenService;

    public RegularUser getUserFromAuthorization(String authorizationHeader) {
        String subject = tokenService.getSubject(authorizationHeader);
        return usersRepository.findByEmail(subject)
                .orElseThrow(UserNotFoundException::new);
    }

    public void checkUserExists(String authorizationHeader) {
        String subject = tokenService.getSubject(authorizationHeader);
        if (!usersRepository.existsByEmail(subject)){
            throw new UserNotFoundException();
        }
    }

    public void createNewUser(CreateUserDTO userDTO, String authorizationHeader) {
        checkUserExists(authorizationHeader);

        String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.getPassword());
        RegularUser newUser = new RegularUser(userDTO.getEmail(), encryptedPassword, userDTO.getRole());

        usersRepository.save(newUser);
    }
}
