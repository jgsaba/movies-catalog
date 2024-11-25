package test.raven.moviescatalog.utils;

public enum OrderDirection {

    ASC,
    DESC;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
