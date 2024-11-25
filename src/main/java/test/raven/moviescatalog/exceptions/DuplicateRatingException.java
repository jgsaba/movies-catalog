package test.raven.moviescatalog.exceptions;

public class DuplicateRatingException extends RuntimeException{

    public DuplicateRatingException(){
        super("User can rate a movie only once");
    }
}
