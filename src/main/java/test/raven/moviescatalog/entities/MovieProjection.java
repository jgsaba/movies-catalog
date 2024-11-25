package test.raven.moviescatalog.entities;

import java.util.Set;

public interface MovieProjection {

    String getName();

    int getReleaseYear();

    String getSynopsis();

    float getRating();

    Set<Category> getCategories();

    String getPoster();
}
