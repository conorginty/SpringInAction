package tacos;

import lombok.Data;

/* @Data annotation at the class level is provided by Lombok and tells Lombok to generate all the missing methods
like getters, setters, toString and hashCode, as well as a constructor that accepts all final properties as arguments. */
@Data
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE;
    }
}
