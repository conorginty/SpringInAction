package tacos;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private OrderRepository orderRepository;
    private OrderProperties orderProperties;

    public OrderController(OrderRepository orderRepository, OrderProperties orderProperties) {
        this.orderRepository = orderRepository;
        this.orderProperties = orderProperties;
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

//    @GetMapping
//    public String ordersForUserBetweenNowAndTenYearsAgo(
//        @AuthenticationPrincipal User user, Model model) {
//        model.addAttribute("orders", orderRepository.readOrdersByDeliveryZipAndPlacedAtBetween(
//            user.getZip(),
//            Date.from(Instant.now()),
//            Date.from(Instant.now().minus(10, ChronoUnit.YEARS)))
//        );
//        return "orderList";
//    }

    @GetMapping
    public String getOrderList(@AuthenticationPrincipal User user, Model model) {
        /* Pageable is Spring Data’s way of selecting some subset of the results by a page number and page size.
        Below we construct a PageRequest object that implements Pageable to request the 1st page (0) with a page size
        of 20 to get up to 20 of the most recently placed orders for the user */
        Pageable pageable = PageRequest.of(0, orderProperties.getPageSize());
        model.addAttribute("orders", orderRepository.findByUserOrderByPlacedAtDesc(user, pageable));

        return "orderList";
    }

    @PutMapping(path="/{orderId}", consumes="application/json")
    public TacoOrder putOrder(@PathVariable("orderId") Long orderId, @RequestBody TacoOrder tacoOrder) {
        tacoOrder.setId(orderId);
        return orderRepository.save(tacoOrder);
    }

    @PatchMapping(path="/{orderId}", consumes="application/json")
    public TacoOrder patchOrder(@PathVariable("orderId") Long orderId, @RequestBody TacoOrder patch) {
        TacoOrder order = orderRepository.findById(orderId).get();
        if (patch.getDeliveryName() != null) {
            order.setDeliveryName(patch.getDeliveryName());
        }
        if (patch.getDeliveryStreet() != null) {
            order.setDeliveryStreet(patch.getDeliveryStreet());
        }
        if (patch.getDeliveryCity() != null) {
            order.setDeliveryCity(patch.getDeliveryCity());
        }
        if (patch.getDeliveryState() != null) {
            order.setDeliveryState(patch.getDeliveryState());
        }
        if (patch.getDeliveryZip() != null) {
            order.setDeliveryZip(patch.getDeliveryZip());
        }
        if (patch.getCcNumber() != null) {
            order.setCcNumber(patch.getCcNumber());
        }
        if (patch.getCcExpiration() != null) {
            order.setCcExpiration(patch.getCcExpiration());
        }
        if (patch.getCcCVV() != null) {
            order.setCcCVV(patch.getCcCVV());
        }
        return orderRepository.save(order);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // HTTP status 204
    public void deleteOrder(@PathVariable("orderId") Long orderId) {
        try {
            orderRepository.deleteById(orderId);
        } catch (EmptyResultDataAccessException e) {

        }
    }
}
