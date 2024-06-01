package tacos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class IngredientRef {
    @Id
    private final Long id;
    private final String ingredient;
}
