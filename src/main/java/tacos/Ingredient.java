package tacos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/* @Data annotation at the class level is provided by Lombok and tells Lombok to generate all the missing methods
like getters, setters, toString and hashCode, as well as a constructor that accepts all final properties as arguments. */
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient implements Persistable<String> {

    @Id
    private String id;
    private String name;
    private Type type;

    @Override
    public boolean isNew() {
        return true;
    }

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE;
    }
}
