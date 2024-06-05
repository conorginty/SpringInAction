package tacos;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserRepository userRepository;

    // @PreAuthorize annotation takes a SpEL expression, and, if the expression evaluates to true, the method will be invoked
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users")
    public String deleteAllUsers() {
        userRepository.deleteAll();
        return "redirect:/admin";
    }
}
