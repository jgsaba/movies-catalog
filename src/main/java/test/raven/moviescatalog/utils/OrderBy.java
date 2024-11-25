package test.raven.moviescatalog.utils;

import io.swagger.v3.oas.annotations.media.Schema;

public enum OrderBy {

    NAME("name"),
    RELEASE_YEAR("releaseYear"),
    CREATED_AT("createdAt"),
    RATING("rating");

    final String value;

    OrderBy(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
