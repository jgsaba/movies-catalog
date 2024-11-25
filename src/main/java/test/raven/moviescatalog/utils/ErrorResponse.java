package test.raven.moviescatalog.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
