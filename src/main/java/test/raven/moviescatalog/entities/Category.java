package test.raven.moviescatalog.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String category;

    public static String normalizeCategory(String category) {
        return category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase();
    }
}
