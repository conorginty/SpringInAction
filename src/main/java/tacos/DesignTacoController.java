package tacos;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient.Type;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j /* Same as doing:
       private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DesignTacoController.class); */
@Controller
@RequestMapping("/design")
/* SessionAttributes here indicates that the TacoOrder object that is put into the model a little later in the class,
should be maintained in session, which is important because creation of a taco is also the first step in creating
an order, and the order we create will need to be carried in the session so that it can span multiple requests. */
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    private IngredientRepository ingredientRepository;

    public DesignTacoController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    // All these methods are invoked when a request is handled (e.g. showDesignForm())

    @ModelAttribute
    // Constructs a list of Ingredient objects to be put into the model.
    /* Model is an object that ferries data between a controller and whatever view is charged with rendering that data.
    Ultimately, data that’s placed in Model attributes is copied into the servlet request attributes, where the view
    can find them and use them to render a page in the user’s browser.*/
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepository.findAll();

        Type[] types = Type.values();
        /* filters the list by ingredient type using a helper method, and the filtered list of ingredient types is
        then added as an attribute to the Model object that will be passed into showDesignForm() */
        for (Type type: types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }
    }

    private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) {
        return StreamSupport.stream(ingredients.spliterator(), false)
            .filter(ingredient -> ingredient.getType().equals(type))
            .collect(Collectors.toList());
    }

    /* The TacoOrder object, referred to earlier in the @SessionAttributes annotation, holds state for the order
    being built as the user creates tacos across multiple requests. */
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    /* As the form in design.html has method="POST", we need a controller handler method on the receiving end of
    that POST request */
    @PostMapping
    /* The @Valid annotation tells Spring MVC to perform validation on the submitted Taco object after it’s bound
    to the submitted form data and before the processTaco() method is called. If there are any validation errors,
    the details of those errors will be captured in an Errors object that’s passed into processTaco(). */
    /* The @ModelAttribute applied to the tacoOrder parameter indicates that it should use the TacoOrder object
    that was placed into the model via the above @ModelAttribute-annotated order() method */
    public String processTaco(@Valid Taco taco, Errors errors,
                              @ModelAttribute TacoOrder tacoOrder) {

        if (errors.hasErrors()) {
            return "design";
        }

        log.info("Processing taco: {}", taco);
        tacoOrder.addTaco(taco);
        log.info("Current taco order: {}", tacoOrder.getTacos());

        return "redirect:/orders/current";
    }

    // Below we inject the value of the property into the variable
    @Value("${greeting.welcome}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    @ResponseBody /* indicates that the return value of the method should be written directly to the HTTP response body,
                  and not resolved as a view name (so that HTML gets rendered on the resulting page */
    public String getWelcomeMessage() {
        return "<h1>" + this.welcomeMessage + "</h1>";
    }
}
