package tacos;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/* Spring Boot autoconfiguration will discover this Converter bean, and will automatically register it with
Spring MVC to be used when the conversion of relevant request parameters to bound properties is needed. */
@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    private final IngredientRepository ingredientRepository;

    public IngredientByIdConverter(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient convert(String id) {
        return this.ingredientRepository.findById(id).orElse(null);
    }
}
