package tacos;

import org.springframework.data.repository.CrudRepository;

// CrudRepository offers a dozen or so operations for creating, reading, updating, and deleting objects.
public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
