package tacos;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/current")
    public String showOrderForm() {
        return "orderForm";
    }

    @PostMapping
    /* The @Valid annotation tells Spring MVC to perform validation on the submitted Taco Order object after it’s bound
    to the submitted form data. If there are any validation errors, the details of those errors will be captured in an
    Errors object that’s passed into processOrder(). */
    public String processOrder(@Valid TacoOrder tacoOrder, Errors errors,
                               SessionStatus sessionStatus,
                               @AuthenticationPrincipal User user) {

        /* We have several ways to determine who the user is. A few of the most common ways follow:
            1. Inject a java.security.Principal object into the controller method.
            2. Inject an org.springframework.security.core.Authentication object into the controller method.
            3. Use org.springframework.security.core.context.SecurityContextHolder to get at the security context.
            4. Inject an @AuthenticationPrincipal annotated method parameter, which is from org.springframework.security.core.annotation package */

        if (errors.hasErrors()) {
            return "orderForm";
        }

        tacoOrder.setUser(user);

//        orderRepository.save(tacoOrder);
        log.info("Taco order complete BUT NOT SAVED FOR THE MOMENT DUE TO BUG: {}", tacoOrder);
        sessionStatus.setComplete(); // ensures the session is cleaned up and ready for a new order the next time the user creates a taco.

        return "redirect:/";
    }
}
