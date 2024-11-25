package test.raven.moviescatalog.exceptions;

public class MovieNotFoundException extends RuntimeException{

    public MovieNotFoundException(){
        super("Movie not found");
    }
}
