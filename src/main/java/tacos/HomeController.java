package tacos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/* Spring comes with a powerful web framework known as Spring MVC. At the center of Spring MVC is the concept
of a controller, a class that handles requests and responds with information of some sort. In the case of a
browser-facing application, a controller responds by optionally populating model data and passing the request
on to a view to produce HTML that’s returned to the browser. */

/* The @Controller doesn't do much and its primary purpose is to identify this class as a component for component
scanning. Because HomeController is annotated with @Controller, Spring’s component scanning automatically discovers
it and creates an instance of HomeController as a bean in the Spring application context.
In fact, a handful of other annotations (including @Component, @Service, and @Repository) serve a purpose similar to
@Controller, and we could have annotated HomeController with any of those other annotations, and it would have still
worked the same. The choice of @Controller is, however, more descriptive of this component’s role in the app. */
@Controller
public class HomeController {

    /* This annotation indicates that if an HTTP GET request is received for the root path /, then this method should
    handle that request. */
    @GetMapping("/")
    public String home() {
        /* This returns the view name. The resulting path for the template is /templates/home.html. Therefore,
        we’ll need to place the template in the project at /src/main/resources/templates/home.html. */
        return "home";
    }
}
