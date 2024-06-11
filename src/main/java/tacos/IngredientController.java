package tacos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@RequestMapping("/ingredient")
public class IngredientController {
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/get-for-object/{id}")
    public Ingredient getIngredientByIdWithGetForObject(String ingredientId) {
        return restTemplate.getForObject(
            "http://localhost:8080/ingredients/{id}",
            Ingredient.class,
            ingredientId
        );
    }

    @GetMapping("/get-for-entity/{id}")
    public Ingredient getIngredientByIdWithGetForEntity(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity = restTemplate.getForEntity("http://localhost:8080/ingredients/{id}", Ingredient.class, ingredientId);
        log.info("Fetched time: {}", responseEntity.getHeaders().getDate());
        return responseEntity.getBody();
    }

    @PostMapping("/update/{id}")
    public void updateIngredient(Ingredient ingredient) {
        restTemplate.put(
            "http://localhost:8080/ingredients/{id}",
            ingredient,
            ingredient.getId()
        );
    }

    @PostMapping("/delete/{id}")
    public void deleteIngredient(Ingredient ingredient) {
        restTemplate.delete(
            "http://localhost:8080/ingredients/{id}",
            ingredient.getId()
        );
    }

    @PostMapping("/create")
    public Ingredient createIngredient(Ingredient ingredient) {
        return restTemplate.postForObject(
            "http://localhost:8080/ingredients",
            ingredient,
            Ingredient.class
        );
    }

}
