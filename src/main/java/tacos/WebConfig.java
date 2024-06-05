package tacos;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* This class is simply a View Controller - one that does nothing but forward the request to a view. (rather than
others we built like DesignTacoController and OrderController, which populate models and process input
As such, it replaces HomeController, as it's doing its job, and thus we delete HomeController and update the
corresponding test to use the WebConfig class that's hosting the home view controller definition instead */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/login").setViewName("login");
    }
}
