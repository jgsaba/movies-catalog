package test.raven.moviescatalog.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import test.raven.moviescatalog.entities.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {
}
