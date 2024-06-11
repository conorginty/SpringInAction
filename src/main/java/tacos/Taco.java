package tacos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
/* @RestResource lets you give the entity any relation name and path you want, thus overriding the default path name
that the REST starter package gave us of: "http://localhost:8080/data-api/tacoes{?page,size,sort}"
(Note the plural of taco defaulted to "tacoes" as opposed to "tacos")*/
@RestResource(rel="tacos", path="tacos")
public class Taco {

    @Id
    /* To get the database to automatically generate the ID value, we annotate the id property with @GeneratedValue,
    specifying a strategy of AUTO. */
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt = new Date();

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Size(min = 1, message = "You must choose at least 1 ingredient")
    /* To declare the relationship between a Taco and its associated Ingredient list, you annotate ingredients with
    @ManyToMany. A Taco can have many Ingredient objects, and an Ingredient can be a part of many Tacos. */
    @ManyToMany
    private List<Ingredient> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }
}
