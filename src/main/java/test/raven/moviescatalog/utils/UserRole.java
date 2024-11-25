package test.raven.moviescatalog.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("admin"),
    USER("user");

    final String value;

    UserRole(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserRole fromValue(String value) {
        for (UserRole role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }
}
