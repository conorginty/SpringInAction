package tacos;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController /* this annotation signifies 2 main things. It tells Spring that:
                1. Marks this class for discovery by component scanning
                2. All handler methods in the controller should have their return value written directly to the body of
                the response, rather than being carried in the model to a view for rendering.
                (if we used @Controller instead, we'd have to annotate all methods with @ResponseBody to achieve the same result)*/
@RequestMapping(path = "/api/tacos", produces = "application/json")
/* The "produces" param above specifies that any of the handler methods in TacoController will handle requests only
if the client sends a request with an Accept header that includes "application/json", indicating that the client can
handle responses only in JSON.
It allows for other controllers to handle requests with the same paths, as long as those requests don’t require JSON output*/
@CrossOrigin(origins = "http://localhost:8080") /* Applies CORS (cross-origin resource sharing) headers in the server responses
                                                to stop the web browser from preventing the client from consuming this API*/
public class TacoController {
    private TacoRepository tacoRepository;
    private OrderRepository orderRepository;

    public TacoController(TacoRepository tacoRepository, OrderRepository orderRepository) {
        this.tacoRepository = tacoRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/{id}") // placeholder variable
    public ResponseEntity<Taco> getTacoById(@PathVariable Long id) {
        Optional<Taco> optionalTaco = tacoRepository.findById(id);
        return optionalTaco.map(taco ->
            new ResponseEntity<>(taco, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<Taco> getAllTacos() {
        return tacoRepository.findAll();
    }

    @PostMapping(consumes="application/json") // i.e. requests JSON as input
    @ResponseStatus(value = HttpStatus.CREATED) // HTTP 201 - not only was the request successful, but a resource was created as a result
    public Taco postTaco(@RequestBody Taco taco) { /* @RequestBody indicates that the body of the JSON request should
                                                   be converted to a Taco object and bound to the parameter.
                                                   Without it, Spring MVC would assume that we want request parameters
                                                   (either query params or form params) to be bound to the Taco object */
        return tacoRepository.save(taco);
    }
    /* Can create a new taco using the above POST request like this:
    curl
    --request POST
    --header "Content-Type: application/json"
    --data '{"id":10,"createdAt":"2024-06-06T05:59:52.145+00:00","name":"NEW ORDER","ingredients":[{"id":"FLTO","name":"Flour Tortilla","type":"WRAP"},{"id":"COTO","name":"Corn Tortilla","type":"WRAP"},{"id":"TMTO","name":"Diced Tomatoes","type":"VEGGIES"},{"id":"LETC","name":"Lettuce","type":"VEGGIES"},{"id":"SLSA","name":"Salsa","type":"SAUCE"}]}'
    http://localhost:8080/api/tacos

    HOWEVER, notice how I set id to 10? When we get all tacos, the taco's ID is replaced with the auto-increment, based on
    how many tacos are already in the data source.

    AND I also needed to disable csrf in the SecurityConfig file just to get a taco to save (TODO: look into why the ID doesn't get updated) */

    /* HTTP PUT is really intended to perform a wholesale replacement operation rather than an update operation.
    Semantically, PUT means "put this data at this URL" (essentially replacing any data that’s already there)
    In contrast, HTTP PATCH performs a patch/partial update of resource data.

    N.B. - As you'll see in the following PUT and PATCH endpoint examples, Spring MVC’s mapping annotations, including
    @PatchMapping and @PutMapping, specify only what kinds of requests a method should handle. These annotations
    don’t dictate how the request will be handled. Even though PUT implies and create or replace, while PATCH semantically
    implies a partial update, it’s up to you to write code in the handler method that actually performs such an update. */

    // e.g. the following PUT method requires the client to submit the complete taco data in the PUT request:
    @PutMapping(path="/{id}", consumes="application/json")
    public Taco putTaco(@PathVariable("id") Long id, @RequestBody Taco taco) {
        taco.setId(id);
        return tacoRepository.save(taco);
    }
    /* Can update an existing or create a new taco using the above PUT request like this:
    curl
    --request PUT
    --header "Content-Type: application/json"
    --data '{"id":10,"createdAt":"2024-06-06T05:59:52.145+00:00","name":"UPDATED ORDER NAME FOR ID 3","ingredients":[{"id":"FLTO","name":"Flour Tortilla","type":"WRAP"},{"id":"COTO","name":"Corn Tortilla","type":"WRAP"},{"id":"TMTO","name":"Diced Tomatoes","type":"VEGGIES"},{"id":"LETC","name":"Lettuce","type":"VEGGIES"},{"id":"SLSA","name":"Salsa","type":"SAUCE"}]}'
    http://localhost:8080/api/tacos/3

    N.B. - Note that if you updated or create a new taco (in the instance that it doesn't already exist), you
    cannot update the ID for some reason (TODO: look into why the ID doesn't get updated)*/

    // e.g. the following PATCH method also requires the taco, but it needn't be completely initialised:
    @PatchMapping(path="/{id}", consumes="application/json")
    public Taco patchTaco(@PathVariable("id") Long id, @RequestBody Taco patch) {
        Taco taco = tacoRepository.findById(id).get();
        if (patch.getCreatedAt() != null) {
            taco.setCreatedAt(patch.getCreatedAt());
        }
        if (patch.getIngredients() != null) {
            taco.setIngredients(patch.getIngredients());
        }
        if (patch.getName() != null) {
            taco.setName(patch.getName());
        }
        return tacoRepository.save(taco);
    }
    /* Can update an existing taco using the above PATCH request like this:
    curl
    --request PATCH
    --header "Content-Type: application/json"
    --data '{"name":"PATCHED Taco Name for id of 1","ingredients":[{"id":"FLTO","name":"Flour Tortilla","type":"WRAP"},{"id":"COTO","name":"Corn Tortilla","type":"WRAP"}]}'
    http://localhost:8080/api/tacos/1 */

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // HTTP status 204
    public void deleteTaco(@PathVariable("id") Long id) {
        try {
            tacoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {

        }
    }
    /* Can delete an existing taco using the above DELETE request like this:
    curl --request DELETE http://localhost:8080/api/tacos/1 */

    @GetMapping(params = "recent") // This endpoint could be accessed via: $ curl localhost:8080/api/tacos?recent
    public Iterable<Taco> getRecentTacos() {
        PageRequest mostRecentRequest = PageRequest.of(0, 3, Sort.by("createdAt").descending());
        return tacoRepository.findAll(mostRecentRequest).getContent();
    }

    /* now when I curl the endpoint using: $ curl localhost:8080/api/tacos?recent, I get the following sample result:

    [
      {
        "id": 3,
        "createdAt": "2024-06-06T03:16:03.678+00:00",
        "name": "Veg-Out",
        "ingredients": [
          {
            "id": "FLTO",
            "name": "Flour Tortilla",
            "type": "WRAP"
          },
          {
            "id": "COTO",
            "name": "Corn Tortilla",
            "type": "WRAP"
          },
          {
            "id": "TMTO",
            "name": "Diced Tomatoes",
            "type": "VEGGIES"
          },
          {
            "id": "LETC",
            "name": "Lettuce",
            "type": "VEGGIES"
          },
          {
            "id": "SLSA",
            "name": "Salsa",
            "type": "SAUCE"
          }
        ]
      },
      {
        "id": 2,
        "createdAt": "2024-06-06T03:16:03.672+00:00",
        "name": "Bovine Bounty",
        "ingredients": [
          {
            "id": "COTO",
            "name": "Corn Tortilla",
            "type": "WRAP"
          },
          {
            "id": "GRBF",
            "name": "Ground Beef",
            "type": "PROTEIN"
          },
          {
            "id": "CHED",
            "name": "Cheddar",
            "type": "CHEESE"
          },
          {
            "id": "JACK",
            "name": "Monterrey Jack",
            "type": "CHEESE"
          },
          {
            "id": "SRCR",
            "name": "Sour Cream",
            "type": "SAUCE"
          }
        ]
      },
      {
        "id": 1,
        "createdAt": "2024-06-06T03:16:03.606+00:00",
        "name": "Carnivore",
        "ingredients": [
          {
            "id": "FLTO",
            "name": "Flour Tortilla",
            "type": "WRAP"
          },
          {
            "id": "GRBF",
            "name": "Ground Beef",
            "type": "PROTEIN"
          },
          {
            "id": "CARN",
            "name": "Carnitas",
            "type": "PROTEIN"
          },
          {
            "id": "SRCR",
            "name": "Sour Cream",
            "type": "SAUCE"
          },
          {
            "id": "SLSA",
            "name": "Salsa",
            "type": "SAUCE"
          },
          {
            "id": "CHED",
            "name": "Cheddar",
            "type": "CHEESE"
          }
        ]
      }
    ]
    */
}
