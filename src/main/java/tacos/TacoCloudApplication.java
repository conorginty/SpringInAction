package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// the Spring Boot main class that boot-straps the project.

/* @SpringBootApplication is a composite annotation that combines the following 3 annotations:
1. @SpringBootConfiguration — Designates this class as a configuration class. Although there’s not much configuration
in the class yet, you can add Java-based Spring Framework configuration to this class if you need to. This annotation
is a specialized form of the @Configuration annotation.
2. @EnableAutoConfiguration — Enables Spring Boot automatic configuration. This annotation tells Spring Boot to
automatically configure any components that it thinks you’ll need.
3. @ComponentScan — Enables component scanning to allow you declare other classes with annotations like @Component,
@Controller, and @Service to have Spring automatically discover and register them as components in the Spring
application context. */
@SpringBootApplication
public class TacoCloudApplication {

	/* This is the method that will be run when the JAR file is executed. The main() method calls a static run()
	method on the SpringApplication class, which performs the actual bootstrapping of the application, creating
	the Spring application context. The 2 parameters passed to the run() method are a configuration class and the
	command line arguments. Although it’s not necessary that the configuration class passed to run() be the same as
	the bootstrap class, this is the most convenient and typical choice.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);
	}

}
