package test.raven.moviescatalog.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import test.raven.moviescatalog.entities.RegularUser;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<RegularUser, Long> {

    Optional<RegularUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
