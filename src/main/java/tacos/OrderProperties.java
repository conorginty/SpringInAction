package tacos;

/* @ConfigurationProperties are often placed on beans whose sole purpose in the application is to be holders of
configuration data. This keeps configuration-specific details out of the controllers and other application classes.
It also makes it easy to share common configuration properties among several beans that may make use of that information. */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "taco.orders") /* when setting the pageSize, or any other property, we need to use a
                                                 configuration property named taco.orders.pageSize. */
public class OrderProperties {
    private int pageSize = 20;
}
