package test.raven.moviescatalog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.raven.moviescatalog.utils.UserRole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private UserRole role;
}
