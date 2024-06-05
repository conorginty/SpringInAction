package tacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import tacos.Ingredient.Type;

@Slf4j
@Component
public class IngredientsLoader implements ApplicationRunner {
    private IngredientRepository repository;

    public IngredientsLoader(IngredientRepository repository){
        this.repository = repository;
    }

    @Override
    @Profile("dev") /* Only load the ingredient data when the application starts in a dev deployment
                    OR if you just didn't want production, but you wanted every other deployment to run this:
                    @Profile("!prod") */
    public void run(ApplicationArguments args) throws Exception {
        repository.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
        repository.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
        repository.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
        repository.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
        repository.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
        repository.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
        repository.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
        repository.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
        repository.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
        repository.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));

        log.info("repository contents: {}", repository.findAll());
    }
}
