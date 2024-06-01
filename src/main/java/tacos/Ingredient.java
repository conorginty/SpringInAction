package tacos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* @Data annotation at the class level is provided by Lombok and tells Lombok to generate all the missing methods
like getters, setters, toString and hashCode, as well as a constructor that accepts all final properties as arguments. */
/* The @Data annotation implicitly adds a required arguments constructor, but when a @NoArgsConstructor is used, that
constructor is removed. An explicit @RequiredArgsConstructor ensures that you’ll still have a required arguments
constructor, in addition to the private no-args constructor. */
@Data
@Entity
@AllArgsConstructor
/* JPA requires that entities have a no-args constructor And because you must set final properties, you also set the
force attribute to true, which results in the Lombok-generated constructor setting them to a default value of
null, 0, or false, depending on the property type. */
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
public class Ingredient {

    @Id
    private final String id;
    private final String name;
    private final Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE;
    }
}
