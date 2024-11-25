package test.raven.moviescatalog.security.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import test.raven.moviescatalog.entities.RegularUser;
import test.raven.moviescatalog.exceptions.UserNotFoundException;
import test.raven.moviescatalog.repositories.UsersRepository;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UsersRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(UserNotFoundException::new);
    }
}
